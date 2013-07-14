package dk.diku.pcsd.exam.partition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import dk.diku.pcsd.exam.common.exceptions.InexistentAccountException;

/**
 * Branch with a simple implementation of Kung-Robinson optimistic concurrency
 * control. For each transaction, works on a copy of the data in the read phase,
 * checks if the transaction conflicts with other concurrent transactions in the
 * validate phase, otherwise writes the changes in the write phase.
 * 
 * Validate and write phase happen in a separate thread, since only one Xact may
 * be validated at once.
 * 
 * @author marco
 * 
 */
public class OptimisticBranch implements Branch {
	// queue containing transactions that should be validated and written
	private BlockingQueue<Xact> toValidate = new LinkedBlockingQueue<Xact>();

	// controls the validation thread
	private volatile boolean execute = true;
	private ValidateThread validateThread;

	// set of all recent transactions past their read phase, sorted so that the
	// one that started last comes first
	private SortedSet<Xact> transactions = new ConcurrentSkipListSet<Xact>(
			new Comparator<Xact>() {

				public int compare(Xact o1, Xact o2) {
					return (int) (o2.getStart() - o1.getStart());
				}
			});

	// set of currently running transactions, sorted so that the one that
	// started first comes first
	private SortedSet<Xact> runningXacts = new ConcurrentSkipListSet<Xact>();

	// map of accountIds to their respective balances
	private Map<Integer, Double> balances = new HashMap<Integer, Double>();

	/**
	 * Created a new branch with optimistic concurrency control, which in turn
	 * creates and starts its own validation thread.
	 */
	public OptimisticBranch() {
		validateThread = new ValidateThread();
		validateThread.start();
	}

	/**
	 * Stops the validation thread
	 */
	@Override
	protected void finalize() throws Throwable {
		execute = false;
		validateThread.interrupt();
		super.finalize();
	}

	/**
	 * Attempts to credit a given account, throws an exception if the
	 * transaction conflicts with another one.
	 */
	private void tryCredit(int accountId, double amount)
			throws InexistentAccountException, OptimisticExecutionException {
		// create a new transaction object with the account in the read and
		// write set
		Xact transaction = new Xact(accountId);

		runningXacts.add(transaction);
		Double currentBalance = balances.get(accountId);

		// if the account exists
		if (currentBalance != null) {
			// calculate the new balance
			double newBalance = currentBalance + amount;
			transaction.addChange(new Account(accountId, newBalance));
			transactions.add(transaction);
			// read phase is over
			transaction.setEndRead(System.nanoTime());

			boolean valid = validate(transaction);
			if (!valid) {
				// conflict, so remove transaction from all list
				transactions.remove(transaction);
				runningXacts.remove(transaction);
				// and throw an exception
				throw new OptimisticExecutionException();
			}

		} else {
			runningXacts.remove(transaction);
			throw new InexistentAccountException(accountId);
		}
	}

	/**
	 * Attempts to debit a given account, throws an exception if the transaction
	 * conflicts with another one.
	 */
	private void tryDebit(int accountId, double amount)
			throws InexistentAccountException, OptimisticExecutionException {
		// create a new transaction object with the account in the read and
		// write set
		Xact transaction = new Xact(accountId);
		runningXacts.add(transaction);
		Double currentBalance = balances.get(accountId);

		// if the account exists
		if (currentBalance != null) {
			// calculate the new balance
			double newBalance = currentBalance - amount;
			transaction.addChange(new Account(accountId, newBalance));
			transactions.add(transaction);

			// read phase over
			transaction.setEndRead(System.nanoTime());

			boolean valid = validate(transaction);
			if (!valid) {
				// conflict
				transactions.remove(transaction);
				runningXacts.remove(transaction);
				throw new OptimisticExecutionException();
			}
		} else {
			runningXacts.remove(transaction);
			throw new InexistentAccountException(accountId);
		}
	}

	/**
	 * Attempts to transfer money from one to another account, throws an
	 * exception if the transaction conflicts with another one.
	 */
	private void tryTransfer(int accountIdOrig, int accountIdDest, double amount)
			throws InexistentAccountException, OptimisticExecutionException {
		// create read and write sets
		HashSet<Integer> readWriteSet = new HashSet<Integer>();
		readWriteSet.add(accountIdOrig);
		readWriteSet.add(accountIdDest);
		Xact transaction = new Xact(readWriteSet, readWriteSet);
		runningXacts.add(transaction);
		Double origBalance = balances.get(accountIdOrig);
		Double destBalance = balances.get(accountIdDest);

		// if both accounts exist
		if (origBalance != null) {
			if (destBalance != null) {
				// if source and dest account are not the same
				if (accountIdOrig != accountIdDest) {
					// calculate new balances
					double newOrig = origBalance - amount;
					double newDest = destBalance + amount;

					transaction.addChange(new Account(accountIdOrig, newOrig));
					transaction.addChange(new Account(accountIdDest, newDest));
					transactions.add(transaction);
					// read phase over
					transaction.setEndRead(System.nanoTime());

					boolean valid = validate(transaction);
					if (!valid) {
						// conflict
						transactions.remove(transaction);
						runningXacts.remove(transaction);
						throw new OptimisticExecutionException();
					}
				} else {
					runningXacts.remove(transaction);
				}
			} else {
				runningXacts.remove(transaction);
				throw new InexistentAccountException(accountIdDest);
			}
		} else {
			runningXacts.remove(transaction);
			throw new InexistentAccountException(accountIdOrig);
		}
	}

	/**
	 * Attempts to calculate this branch's exposure, throws an exception if the
	 * transaction conflicts with another one.
	 */
	private double tryCalculateExposure() throws OptimisticExecutionException {
		// read set contains all accounts, write set is empty
		Xact transaction = new Xact(new HashSet<Integer>(balances.keySet()),
				new HashSet<Integer>());
		runningXacts.add(transaction);

		// add up exposure
		double result = 0.0;
		for (Integer i : balances.keySet()) {
			double balance = balances.get(i);
			if (balance < 0.0)
				result += balance;
		}
		transactions.add(transaction);
		// read phase over
		transaction.setEndRead(System.nanoTime());

		if (validate(transaction)) {
			return Math.abs(result);
		} else {
			// conflict
			transactions.remove(transaction);
			runningXacts.remove(transaction);
			throw new OptimisticExecutionException();
		}
	}

	@Override
	public void setBalance(int accountId, double balance) {
		balances.put(accountId, balance);
	}

	@Override
	public double getBalance(int accountId) throws InexistentAccountException {
		Double result = balances.get(accountId);
		if (result != null)
			return result;
		else
			throw new InexistentAccountException(accountId);
	}

	/**
	 * Calls tryCredit, retries if there is a conflict.
	 */
	@Override
	public void credit(int accountId, double amount)
			throws InexistentAccountException {
		boolean success = false;
		while (!success) {
			try {
				tryCredit(accountId, amount);
				success = true;
			} catch (OptimisticExecutionException e) {
			}
		}
	}

	/**
	 * Calls tryDebit, retries if there is a conflict.
	 */
	@Override
	public void debit(int accountId, double amount)
			throws InexistentAccountException {
		boolean success = false;
		while (!success) {
			try {
				tryDebit(accountId, amount);
				success = true;
			} catch (OptimisticExecutionException e) {
			}
		}
	}

	/**
	 * Calls tryTransfer, retries if there is a conflict.
	 */
	@Override
	public void transfer(int accountIdOrig, int accountIdDest, double amount)
			throws InexistentAccountException {
		boolean success = false;
		while (!success) {
			try {
				tryTransfer(accountIdOrig, accountIdDest, amount);
				success = true;
			} catch (OptimisticExecutionException e) {
			}
		}
	}

	/**
	 * Calls tryCalculateExposure, retries if there is a conflict.
	 */
	@Override
	public double calculateExposure() {
		boolean success = false;
		double result = 0.0;
		while (!success) {
			try {
				result = tryCalculateExposure();
				success = true;
			} catch (OptimisticExecutionException e) {
			}
		}
		return result;
	}

	/**
	 * Puts a transaction in the validation queue, returns the result when
	 * validation is done in the vlidation thread.
	 * 
	 * @param transaction
	 * @return false if the transaction conflicts with another one
	 */
	private boolean validate(Xact transaction) {
		FutureResult f = new FutureResult(transaction.getStart());
		transaction.setValid(f);
		toValidate.add(transaction);
		try {
			return f.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Thread that validates transactions one at a time
	 * @author marco
	 *
	 */
	private class ValidateThread extends Thread {
		@Override
		public void run() {
			// keep polling the queue while execute is true
			while (execute) {
				Xact transaction;
				try {
					transaction = toValidate
							.poll(Long.MAX_VALUE, TimeUnit.DAYS);
					if (transaction != null) {
						boolean result = validate(transaction);
						if (result) {
							// write if no conflict
							write(transaction);
							transaction.setEndWrite(System.nanoTime());
						}
						transaction.getValid().signalAll(result);
					}
				} catch (InterruptedException e) {
				}
			}
		}

		private boolean validate(Xact transaction) {
			// for each recent xact past its write phase
			for (Iterator<Xact> i = transactions.iterator(); i.hasNext();) {
				Xact current = i.next();
				if (current != transaction
						&& current.getEndRead() < transaction.getEndRead()) {
					// if a transaction is so old it cannot have influenced
					// those
					// that are validated in the future, throw it out
					if (current.getEndWrite() < runningXacts.first().getStart()) {
						i.remove();
						// if a transaction has not completed before this one
						// started,
						// but is completed NOW, check whether read and write
						// set
						// overlap
					} else if (current.getEndWrite() > transaction.getStart()
							&& current.getEndWrite() < System.nanoTime()) {
						Set<Integer> intersection = new HashSet<Integer>(
								transaction.getReadSet());
						intersection.retainAll(current.getWriteSet());
						if (!intersection.isEmpty())
							return false;
						// if all we know is that the other transaction's read phase ended before this one's
						// make sure there are no overlaps between the other's write sets and this xact.
					} else if (current.getEndWrite() > transaction.getStart()) {
						Set<Integer> intersection1 = new HashSet<Integer>(
								transaction.getReadSet());
						intersection1.retainAll(current.getWriteSet());
						Set<Integer> intersection2 = new HashSet<Integer>(
								transaction.getWriteSet());
						intersection2.retainAll(current.getWriteSet());

						if (!intersection1.isEmpty()
								|| !intersection2.isEmpty())
							return false;
					}
				}
			}
			return true;
		}

		/**
		 * Makes a transaction's changes permanent
		 * @param transaction
		 */
		private void write(Xact transaction) {
			for (Account a : transaction.getChanges()) {
				balances.put(a.getId(), a.getBalance());
			}
			runningXacts.remove(transaction);
		}
	}
}
