package dk.diku.pcsd.exam.partition;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dk.diku.pcsd.exam.common.exceptions.InexistentAccountException;

/**
 * Branch implementation that uses locks to guarantee serializability. Used
 * Conservative Strict 2PL and always locks accounts with lower ID first to
 * avoid deadlocks.
 * 
 * @author marco
 * 
 */
public class PessimisticBranch implements Branch {
	// one ReadWriteLock for each account
	private Map<Integer, ReadWriteLock> locks = new HashMap<Integer, ReadWriteLock>();

	// a map of the balance of each account
	private Map<Integer, Double> balances = new HashMap<Integer, Double>();

	/**
	 * Credits an amount to an account
	 */
	@Override
	public void credit(int accountId, double amount)
			throws InexistentAccountException {
		ReadWriteLock lock = locks.get(accountId);
		// if account exists
		if (lock != null) {
			// exclusive lock
			lock.writeLock().lock();

			try {
				double currentBalance = balances.get(accountId);
				double newBalance = currentBalance + amount;

				// save new balance
				balances.put(accountId, newBalance);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			throw new InexistentAccountException(accountId);
		}
	}

	/**
	 * Debits an amount from an account
	 */
	@Override
	public void debit(int accountId, double amount)
			throws InexistentAccountException {
		ReadWriteLock lock = locks.get(accountId);
		// if the account exists
		if (lock != null) {
			// exclusive lock
			lock.writeLock().lock();

			try {
				double currentBalance = balances.get(accountId);
				double newBalance = currentBalance - amount;

				// save new balance
				balances.put(accountId, newBalance);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			throw new InexistentAccountException(accountId);
		}
	}

	/**
	 * Transfers an amount from one account to another.
	 */
	@Override
	public void transfer(int accountIdOrig, int accountIdDest, double amount)
			throws InexistentAccountException {
		ReadWriteLock lockOrig = locks.get(accountIdOrig);
		ReadWriteLock lockDest = locks.get(accountIdDest);

		// if both accounts exist
		if (lockOrig != null) {
			if (lockDest != null) {
				// lock the account with the lower ID first
				if (accountIdOrig < accountIdDest) {
					lockOrig.writeLock().lock();
					lockDest.writeLock().lock();
				} else {
					lockDest.writeLock().lock();
					lockOrig.writeLock().lock();
				}
				try {

					// completely update source account first
					double origBalance = balances.get(accountIdOrig);
					double newOrig = origBalance - amount;
					balances.put(accountIdOrig, newOrig);

					// then update destination account
					double destBalance = balances.get(accountIdDest);
					double newDest = destBalance + amount;
					balances.put(accountIdDest, newDest);
				} finally {
					lockOrig.writeLock().unlock();
					lockDest.writeLock().unlock();
				}
			} else {
				throw new InexistentAccountException(accountIdDest);
			}
		} else {
			throw new InexistentAccountException(accountIdOrig);
		}
	}

	/**
	 * Calculates this branch's exposure
	 */
	@Override
	public double calculateExposure() {
		// sort accounts from lowest ID to highest
		SortedSet<Integer> accounts = new TreeSet<Integer>(locks.keySet());

		// lock them in that order
		for (Integer i : accounts) {
			locks.get(i).readLock().lock();
		}

		double result = 0.0;
		try {
			// add up exposure
			for (Integer i : accounts) {
				double balance = balances.get(i);
				if (balance < 0.0)
					result += balance;
			}
		} finally {
			for (Integer i : accounts) {
				locks.get(i).readLock().unlock();
			}
		}

		return Math.abs(result);
	}

	@Override
	public void setBalance(int accountId, double balance) {
		balances.put(accountId, balance);
		locks.put(accountId, new ReentrantReadWriteLock(true));
	}

	@Override
	public double getBalance(int accountId) throws InexistentAccountException {
		Double result = balances.get(accountId);
		if (result != null)
			return result;
		else
			throw new InexistentAccountException(accountId);
	}

}
