package dk.diku.pcsd.exam.experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.namespace.QName;

import dk.diku.pcsd.exam.proxy.AccountManagerProxyImpl;
import dk.diku.pcsd.exam.proxy.AccountManagerProxyImplService;
import dk.diku.pcsd.exam.proxy.InexistentAccountException_Exception;
import dk.diku.pcsd.exam.proxy.InexistentBranchException_Exception;
import dk.diku.pcsd.exam.proxy.NegativeAmountException_Exception;

/**
 * Carries out the experiment as described in the report. Starts n threads which
 * credit, debit, transfer and calculate the exposure of a randomly chosen
 * account. Outputs throughput in a file.
 * 
 * When run from the command line, takes the proxy's wsdl URL as the first
 * argument and, optionally, the number n of threads to start as the second
 * argument.
 * 
 * @author marco
 * 
 */
public class Experiment {
	private static int[] threadNos = new int[] { 1, 1, 1, 2, 3, 4, 5, 1, 1, 1,
			2, 3, 4, 5, 6, 7, 8 };

	// number of requests each thread performs
	private static final int requestsPerThread = 1000;

	// skew in the usage of branches; 0.3 means 30% of branches get 70% of all
	// requests (self-similar distribution is used)
	private static final double branchSkew = 0.3;

	// skew in the usage of accounts; 0.2 means 20% of branches in an account
	// get 80% of all requests (self-similar distribution is used)
	private static final double accountSkew = 0.2;
	private static final String resultFile = "results.txt";

	private String wsdlPath;

	private Map<Integer, List<Integer>> accounts;
	private List<Integer> branches;

	private BufferedWriter br;

	/**
	 * Takes optionally the URL of the proxy's wsdl and a value for noOfThreads,
	 * which is then performed three times to even out random fluctuations.
	 * 
	 */
	public static void main(String[] args) {
		Experiment e;
		if (args.length > 0) {
			e = new Experiment(args[0]);
			if (args.length > 1) {
				int clients = Integer.parseInt(args[1]);
				threadNos = new int[] { clients, clients, clients };
			}

		} else {
			e = new Experiment();
		}
		e.run(threadNos);
	}

	public Experiment(String path) {
		accounts = new InitDataReader().getBranches();
		branches = new ArrayList<Integer>(accounts.keySet());
		wsdlPath = path;
	}

	public Experiment() {
		accounts = new InitDataReader().getBranches();
		branches = new ArrayList<Integer>(accounts.keySet());
		wsdlPath = null;
	}

	/**
	 * Runs the experiment with different numbers of threads
	 * 
	 * @param noOfThreads
	 *            an array containing the numbers of threads with which to run
	 *            the experiment
	 */
	public void run(int[] noOfThreads) {
		try {
			br = new BufferedWriter(new FileWriter(resultFile, true));

			for (int i = 0; i < noOfThreads.length; i++) {
				Result r = run(noOfThreads[i]);
				br.write(""
						+ noOfThreads[i]
						+ " Threads, Throughput: "
						+ (1000000000000L * (noOfThreads[i] * requestsPerThread) / r
								.getTotalRuntime()) + "requests / ks");
				br.write("\n");
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Runs the experiment with a specific number of client threads
	 * 
	 * @param noOfThreads
	 * @return a Result object containing the parameters of the experiment and
	 *         several latency measurements
	 */
	private Result run(int noOfThreads) {
		ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
		List<Long> threadTimes = new ArrayList<Long>();

		List<Future<Long>> results;

		List<Callable<Long>> threadList = new ArrayList<Callable<Long>>();
		for (int i = 0; i < noOfThreads; i++) {
			threadList.add(new TransferThread(requestsPerThread, i % 4,
					i % 2 == 0 ? 1 : -1));
		}

		try {
			long absoluteStart = System.nanoTime();
			results = executor.invokeAll(threadList);

			for (Future<Long> f : results) {
				threadTimes.add(f.get());
			}
			long absoluteEnd = System.nanoTime();

			executor.shutdown();

			return new Result(noOfThreads, requestsPerThread, threadTimes,
					absoluteEnd - absoluteStart);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return null;
	}

	private class TransferThread implements Callable<Long> {
		/**
		 * The id of the next operation that should be run Initialized
		 * differently in all threads to avoid that all threads do the same at
		 * the same time.
		 */
		private int currentOp;

		/**
		 * 
		 * States if currentOp should be incremented or decremented
		 */
		private int delta;

		private int transfers;
		private RandomNumberGenerator zipf = new RandomNumberGenerator();

		public TransferThread(int transfers, int currentOp, int delta) {
			this.transfers = transfers;
			this.currentOp = currentOp;
			this.delta = delta;
		}

		@Override
		public Long call() {
			AccountManagerProxyImplService ams;
			if (wsdlPath != null) {
				URL baseUrl;
				baseUrl = dk.diku.pcsd.exam.proxy.AccountManagerProxyImplService.class
						.getResource(".");
				URL url = null;
				try {
					url = new URL(baseUrl, wsdlPath);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}

				QName qn = new QName("http://proxy.exam.pcsd.diku.dk/",
						"AccountManagerProxyImplService");

				ams = new AccountManagerProxyImplService(url, qn);
			} else {
				ams = new AccountManagerProxyImplService();
			}
			AccountManagerProxyImpl am = ams.getAccountManagerProxyImplPort();

			long start = System.nanoTime();

			for (int i = 0; i < transfers; i++) {
				int branch = branches.get(zipf.selfSimilar(branches.size(),
						branchSkew) - 1);
				List<Integer> accountIds = accounts.get(branch);

				int account1 = accountIds.get(zipf.selfSimilar(
						accountIds.size(), accountSkew) - 1);
				int account2 = accountIds.get(zipf.selfSimilar(
						accountIds.size(), accountSkew) - 1);

				double amount = Math.round(Math.random() * 10000000.0) / 100.0;

				try {
					switch ((currentOp += delta) % 4) {
					case 0:
						am.calculateExposure(branch);
						break;
					case 1:
						am.credit(branch, account1, amount);
						break;
					case 2:
						am.debit(branch, account2, amount);
						break;
					case 3:
						am.transfer(branch, account1, account2, amount);
						break;
					}
				} catch (InexistentAccountException_Exception e) {
					e.printStackTrace();
				} catch (InexistentBranchException_Exception e) {
					e.printStackTrace();
				} catch (NegativeAmountException_Exception e) {
					e.printStackTrace();
				}
			}

			return System.nanoTime() - start;
		}
	}
}
