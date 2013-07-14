package dk.diku.pcsd.exam.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import dk.diku.pcsd.exam.common.exceptions.InexistentAccountException;
import dk.diku.pcsd.exam.common.exceptions.InexistentBranchException;
import dk.diku.pcsd.exam.common.exceptions.NegativeAmountException;
import dk.diku.pcsd.exam.common.interfaces.AccountManager;
import dk.diku.pcsd.exam.partition.Account;
import dk.diku.pcsd.exam.partition.AccountManagerPartitionImpl;
import dk.diku.pcsd.exam.partition.AccountManagerPartitionImplService;
import dk.diku.pcsd.exam.partition.InexistentAccountException_Exception;
import dk.diku.pcsd.exam.partition.InexistentBranchException_Exception;

/**
 * The proxy communicates with clients and partitions. It mostly manages
 * partitions and forwards function calls to the relevant partition service.
 * 
 * Initializes itself and its partitions by reading the init file specified in
 * the config file "account.conf", which is expected to be in user.home.
 * Initialization is done via an additional thread that is started in the
 * constructor, which messages all account data to the partitions.
 * 
 * @author marco
 * 
 */
@WebService
public class AccountManagerProxyImpl implements AccountManager {
	// how long the init thread should wait before initializing partitions
	private static long initWait = 10000;
	private String initFile;

	// timeout for requests to partitions
	private int requestTimeout = 15000;

	// partitioner which assigns branches to partitions
	private Partitioner partitioner;

	// list of partition service stubs
	private List<AccountManagerPartitionImpl> partitions;

	public AccountManagerProxyImpl() {
		System.out.println("Proxy starting!");
		partitioner = new HashModPartitioner();

		// load configuration file
		String home = System.getProperty("user.home");
		if (!home.endsWith(File.separator))
			home += File.separator;
		String configFile = home + "account.conf";
		initFile = home + "account.init";

		List<String> partitionWSDLs = new ArrayList<String>();

		try {
			partitions = new ArrayList<AccountManagerPartitionImpl>();

			BufferedReader confReader = new BufferedReader(new FileReader(
					configFile));

			String next = confReader.readLine();

			while (next != null) {
				if (!next.startsWith("%")) {
					String[] separated = next.split("\t", 2);

					if (separated.length == 2) {
						switch (separated[0]) {
						case "partition":
							// add another partition service to the list
							partitionWSDLs.add(separated[1]);
							break;
						case "initTime":
							// set the waiting time for the init thread
							try {
								initWait = Long.parseLong(separated[1]);
							} catch (NumberFormatException e) {
							}
							break;
						case "initFile":
							// set the name of the init file
							initFile = home + separated[1];
							break;
						case "timeout":
							// change the timeout for requests to the partitions
							try {
								requestTimeout = Integer.parseInt(separated[1]);
							} catch (NumberFormatException e) {
							}
						default:
							break;
						}
					}
				}
				next = confReader.readLine();
			}

			confReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// start the init thread
		// doing this in a separate thread is not the most beautiful solution,
		// but accessing partitions in the constructor is impossible,
		// initializing on the first client request unnecessarily delays that
		// request
		// and letting the partitions initialize themselves with separate init
		// files
		// would violate separation of concerns: only the partioner in the proxy
		// should
		// decide which branch goes where.
		System.out.println("Starting init thread");
		new InitThread(partitionWSDLs).start();
	}

	/**
	 * Credits an amount of money to a given account.
	 */
	@Override
	@WebMethod
	public void credit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		// check for negative amounts right away
		if (amount < 0.0)
			throw new NegativeAmountException(amount);
		try {
			// forward request to partition
			partitioner.getPartition(branchId, partitions).credit(branchId,
					accountId, amount);
		} catch (WebServiceException e) {
			// partition cannot be reached
			throw new InexistentBranchException("Branch " + branchId
					+ " cannot be reached.", branchId);
		} catch (InexistentAccountException_Exception e) {
			throw translateException(e);
		} catch (InexistentBranchException_Exception e) {
			throw translateException(e);
		}
	}

	/**
	 * Debits an amount of money from a given account.
	 */
	@Override
	@WebMethod
	public void debit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException,
			NegativeAmountException {
		// check for negative amount
		if (amount < 0.0)
			throw new NegativeAmountException(amount);
		try {
			// forward to partition
			partitioner.getPartition(branchId, partitions).debit(branchId,
					accountId, amount);
		} catch (WebServiceException e) {
			// partition not reachable
			partitioner.setUnavailable(branchId, partitions);
			throw new InexistentBranchException("Branch " + branchId
					+ " cannot be reached.", branchId);
		} catch (InexistentAccountException_Exception e) {
			throw translateException(e);
		} catch (InexistentBranchException_Exception e) {
			throw translateException(e);
		}
	}

	/**
	 * Transfers money from one account to the other
	 */
	@Override
	@WebMethod
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,
			double amount) throws InexistentBranchException,
			InexistentAccountException, NegativeAmountException {
		if (amount < 0.0)
			throw new NegativeAmountException(amount);
		try {
			partitioner.getPartition(branchId, partitions).transfer(branchId,
					accountIdOrig, accountIdDest, amount);
		} catch (WebServiceException e) {
			throw new InexistentBranchException("Branch " + branchId
					+ " cannot be reached.", branchId);
		} catch (InexistentAccountException_Exception e) {
			throw translateException(e);
		} catch (InexistentBranchException_Exception e) {
			throw translateException(e);
		}

	}

	/**
	 * Calculates a branch's exposure, i.e. sums up all negative balances and
	 * returns their absolute value
	 */
	@Override
	@WebMethod
	public double calculateExposure(int branchId)
			throws InexistentBranchException {
		try {
			return partitioner.getPartition(branchId, partitions)
					.calculateExposure(branchId);
		} catch (WebServiceException e) {
			throw new InexistentBranchException("Branch " + branchId
					+ " cannot be reached.", branchId);
		} catch (InexistentBranchException_Exception e) {
			throw translateException(e);
		}
	}

	/**
	 * A couple of methods that translate the generated exception objects into
	 * the ones the client expects
	 */
	private InexistentBranchException translateException(
			InexistentBranchException_Exception e) {
		return new InexistentBranchException(e.getFaultInfo().getMessage(), e
				.getFaultInfo().getBranchId());
	}

	private InexistentAccountException translateException(
			InexistentAccountException_Exception e) {
		return new InexistentAccountException(e.getFaultInfo().getMessage(), e
				.getFaultInfo().getAccountId());
	}

	/**
	 * Thread that initializes the partitions
	 * 
	 * @author marco
	 * 
	 */
	private class InitThread extends Thread {
		List<String> wsdls;

		/**
		 * Takes a list of strings specifying the partitions wsdl URLs.
		 * 
		 * @param wsdls
		 */
		public InitThread(List<String> wsdls) {
			this.wsdls = wsdls;
		}

		@Override
		public void run() {

			// wait until the server has finished starting
			try {
				Thread.sleep(initWait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// create all partition service stubs
			for (String w : wsdls) {
				try {
					URL baseUrl;
					baseUrl = dk.diku.pcsd.exam.partition.AccountManagerPartitionImplService.class
							.getResource(".");
					URL url = new URL(baseUrl, w);
					QName qn = new QName("http://partition.exam.pcsd.diku.dk/",
							"AccountManagerPartitionImplService");
					AccountManagerPartitionImplService s = new AccountManagerPartitionImplService(
							url, qn);

					AccountManagerPartitionImpl newPartition = s
							.getAccountManagerPartitionImplPort();

					Map<String, Object> requestContext = ((BindingProvider) newPartition)
							.getRequestContext();
					requestContext.put("com.sun.xml.ws.connect.timeout",
							requestTimeout);
					requestContext.put("com.sun.xml.ws.request.timeout",
							requestTimeout);

					partitions.add(newPartition);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (WebServiceException e) {
					// partition not reachable, do not add to list
				}
			}

			System.out.println("Initializing AccountManager");

			// check if all partitions are uninitialized
			boolean shouldInitialize = true;
			for (AccountManagerPartitionImpl p : partitions) {
				shouldInitialize = shouldInitialize && p.startInitializing();
			}

			// if so
			if (shouldInitialize) {
				try {
					// read init file
					BufferedReader initReader = new BufferedReader(
							new FileReader(initFile));
					String next = initReader.readLine();

					int branch = -1;
					List<Account> toBeAdded = null;

					while (next != null) {
						// ignore comments
						if (!next.startsWith("%")) {
							String[] separated = next.split("\t", 2);

							if (separated.length == 2) {
								switch (separated[0]) {
								case "branch":
									// new branch starts
									try {
										if (branch != -1)
											setBalances(branch, toBeAdded);
										branch = Integer.parseInt(separated[1]);
										toBeAdded = new ArrayList<Account>();
									} catch (NumberFormatException e) {
									}
									break;
								default:
									// account ID and balance
									if (branch != -1) {
										try {
											int accountId = Integer
													.parseInt(separated[0]);
											double balance = Double
													.parseDouble(separated[1]);
											Account acc = new Account();
											acc.setBalance(balance);
											acc.setId(accountId);
											toBeAdded.add(acc);
										} catch (NumberFormatException e) {
										}
									}
									break;
								}
							}
						}
						next = initReader.readLine();
					}
					if (branch != -1)
						setBalances(branch, toBeAdded);

					initReader.close();
					for (AccountManagerPartitionImpl p : partitions) {
						p.setInitialized();
					}
					System.out.println("Finished initializing");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Sends a list of the account IDs and balances of a branch to the
		 * partition that should manage this branch
		 * 
		 */
		private void setBalances(int branchId, List<Account> toBeAdded) {
			try {
				partitioner.getPartition(branchId, partitions).setBalances(
						branchId, toBeAdded);
			} catch (InexistentBranchException e) {
				// should never happen; branchIds in the init file
				// should always be valid
				e.printStackTrace();
			}
		}
	}

}
