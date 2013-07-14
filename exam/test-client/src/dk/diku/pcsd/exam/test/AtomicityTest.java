package dk.diku.pcsd.exam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.exam.proxy.AccountManagerProxyImpl;
import dk.diku.pcsd.exam.proxy.AccountManagerProxyImplService;
import dk.diku.pcsd.exam.proxy.InexistentBranchException_Exception;

/**
 * Verifies that the service's operations are indeed atomic. 
 * Functionality as spelled out in the report.
 * @author marco
 *
 */
public class AtomicityTest {
	private static AccountManagerProxyImpl am;
	private int branchId = 20;
	private int[] accountIds = new int[] { 1, 2, 3, 4, 5, 6 };
	private int transfersPerThread = 200;
	private int transferThreads = 10;
	private int exposureThreads = 4;
	private volatile int runningThreads = transferThreads;
	private double exposure = 100000.0;

	private Queue<Transaction> q = new LinkedList<Transaction>();
	private int transactionsPerThread = 300;
	private int transactionThreads = 14;
	private int transactionsBranchId = 9;

	@BeforeClass
	public static void initialize() {
		AccountManagerProxyImplService ams = new AccountManagerProxyImplService();
		am = ams.getAccountManagerProxyImplPort();
	}

	@Test
	public void transferExposureAtomicity() {
		ExecutorService executor = Executors
				.newFixedThreadPool(transferThreads);

		List<Future<Boolean>> results;

		List<Callable<Boolean>> threads = new ArrayList<Callable<Boolean>>();

		for (int i = 0; i < transferThreads; i++) {
			int from = accountIds[(int) (Math.random() * accountIds.length)];
			int to = accountIds[(int) (Math.random() * accountIds.length)];
			threads.add(new TransferThread(from, to));
		}

		for (int i = 0; i < exposureThreads; i++) {
			threads.add(new ExposureThread());
		}

		try {
			results = executor.invokeAll(threads);

			for (Future<Boolean> f : results) {
				assertTrue(f.get());
			}

			executor.shutdown();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void generalAtomicity() {
		ExecutorService executor = Executors
				.newFixedThreadPool(transactionThreads);

		List<Future<Boolean>> results;

		List<Callable<Boolean>> threads = new ArrayList<Callable<Boolean>>();

		for (int i = 0; i < transactionThreads; i++) {
			threads.add(new TransactionThread());
		}

		try {
			results = executor.invokeAll(threads);

			for (Future<Boolean> f : results) {
				assertTrue(f.get());
			}
			executor.shutdown();

			Map<Integer, Double> balances = new HashMap<Integer, Double>();
			balances.put(1, 0.0);
			balances.put(2, 100.0);
			balances.put(3, -120.0);

			Transaction t = q.poll();
			while (t != null) {
				t.execute(balances);
				t = q.poll();
			}

			double expos = 0.0;
			for (int i = 1; i <= 3; i++) {
				if (balances.get(i) < 0)
					expos -= balances.get(i);
			}

			assertEquals(expos, am.calculateExposure(transactionsBranchId),
					0.01);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InexistentBranchException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class TransferThread implements Callable<Boolean> {
		private int from;
		private int to;

		public TransferThread(int from, int to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public Boolean call() {
			boolean success = true;

			for (int i = 0; i < transfersPerThread; i++) {
				double amount = Math.round(Math.random() * 40);
				try {
					am.transfer(branchId, from, to, amount);
					am.transfer(branchId, to, from, amount);
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
			}
			runningThreads--;
			return success;
		}
	}

	private class ExposureThread implements Callable<Boolean> {

		@Override
		public Boolean call() {
			boolean success = true;

			while (runningThreads > 0) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					double exp = am.calculateExposure(branchId);
					if (exp < (exposure - 0.01) || exp < (exposure - 0.01)) {
						System.out.println("False exposure: " + exp);
						success = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
			}

			return success;
		}
	}

	private class TransactionThread implements Callable<Boolean> {
		@Override
		public Boolean call() {
			boolean success = true;

			for (int i = 0; i < transactionsPerThread; i++) {
				switch ((int) (Math.random() * 3)) {
				case 0:
					int accountId = (int) (Math.random() * 3) + 1;
					double amount = Math.round(Math.random() * 300.0);
					Credit c = new Credit(accountId, amount);
					try {
						am.credit(transactionsBranchId, accountId, amount);
						q.add(c);
					} catch (Exception e) {
						success = false;
						e.printStackTrace();
					}
					break;
				case 1:
					int accountIdD = (int) (Math.random() * 3) + 1;
					double amountD = Math.round(Math.random() * 40.0);
					Debit d = new Debit(accountIdD, amountD);
					try {
						am.debit(transactionsBranchId, accountIdD, amountD);
						q.add(d);
					} catch (Exception e) {
						success = false;
						e.printStackTrace();
					}
					break;
				case 2:
					int accountIdOrig = (int) (Math.random() * 3) + 1;
					int accountIdDest = (int) (Math.random() * 3) + 1;
					double amountT = Math.round(Math.random() * 300.0);
					Transfer t = new Transfer(accountIdOrig, accountIdDest,
							amountT);
					try {
						am.transfer(transactionsBranchId, accountIdOrig,
								accountIdDest, amountT);
						q.add(t);
					} catch (Exception e) {
						success = false;
						e.printStackTrace();
					}
					break;
				}
			}
			return success;
		}
	}
}
