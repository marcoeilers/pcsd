package dk.diku.pcsd.exam.partition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dk.diku.pcsd.exam.common.exceptions.InexistentAccountException;
import dk.diku.pcsd.exam.common.exceptions.InexistentBranchException;

/**
 * A partition contains several branches and is only used by the proxy. Exposed
 * as a web service, but not directly used by the client.
 * 
 * Manages branches, but dDelegates most actual work to those branches.
 * 
 * @author marco
 * 
 */
@WebService
public class AccountManagerPartitionImpl {
	// determines if optimistic concurrency control should be used
	boolean optimistic = false;

	// booleans determining the partition's status, i.e. initialized,
	// initializing or not initialized
	boolean initializing = false;
	boolean initialized = false;

	// maps branchIds to branch objects
	Map<Integer, Branch> branches;

	public AccountManagerPartitionImpl() {
		branches = new HashMap<Integer, Branch>();

		// read configuration file to check if optimistic concurrency control
		// should be used
		String home = System.getProperty("user.home");
		if (!home.endsWith(File.separator))
			home += File.separator;

		// name is expected to be account.conf in user.home
		String configFile = home + "account.conf";

		BufferedReader confReader;
		try {
			confReader = new BufferedReader(new FileReader(configFile));

			String next = confReader.readLine();

			while (next != null) {
				if (!next.startsWith("%")) {
					String[] separated = next.split("\t", 2);

					if (separated.length == 2) {
						switch (separated[0]) {
						case "optimistic":
							optimistic = Boolean.parseBoolean(separated[1]);
							break;
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
	}

	/**
	 * Adds an amount to an account. Does NOT check if amount is negative, this
	 * has to be done by the proxy.
	 * 
	 * @param branchId
	 *            the ID of the account's branch
	 * @param accountId
	 *            the ID of the account
	 * @param amount
	 *            how much to credit, expected to be positive
	 * @throws InexistentBranchException
	 *             if the branchId is unknown on this partition
	 * @throws InexistentAccountException
	 *             if the accountId does not exist in this branch
	 */
	@WebMethod
	public void credit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException {
		if (initialized) {
			Branch b = branches.get(branchId);
			if (b != null) {
				b.credit(accountId, amount);
			} else {
				throw new InexistentBranchException(branchId);
			}
		} else {
			throw new InexistentBranchException(
					"Partition not yet initialized.", branchId);
		}

	}

	/**
	 * Subtracts an amount from an account. Does NOT check if amount is
	 * negative, this has to be done by the proxy.
	 * 
	 * @param branchId
	 *            the ID of the account's branch
	 * @param accountId
	 *            the ID of the account
	 * @param amount
	 *            how much to credit, expected to be positive
	 * @throws InexistentBranchException
	 *             if the branchId is unknown on this partition
	 * @throws InexistentAccountException
	 *             if the accountId does not exist in this branch
	 */
	@WebMethod
	public void debit(int branchId, int accountId, double amount)
			throws InexistentBranchException, InexistentAccountException {
		if (initialized) {
			Branch b = branches.get(branchId);
			if (b != null) {
				b.debit(accountId, amount);
			} else {
				throw new InexistentBranchException(branchId);
			}
		} else {
			throw new InexistentBranchException(
					"Partition not yet initialized.", branchId);
		}
	}

	/**
	 * Transfers an amount from one account to another one in the same branch.
	 * Does NOT check if amount is negative, this has to be done by the proxy.
	 * 
	 * @param branchId
	 *            the ID of the accounts' branch
	 * @param accountIdOrig
	 *            the ID of the account from which to transfer money
	 * @param accountIdDest
	 *            the ID of the account to which to transfer money
	 * @param amount
	 *            how much to credit, expected to be positive
	 * @throws InexistentBranchException
	 *             if the branchId is unknown on this partition
	 * @throws InexistentAccountException
	 *             if the accountId does not exist in this branch
	 */
	@WebMethod
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,
			double amount) throws InexistentBranchException,
			InexistentAccountException {
		if (initialized) {
			Branch b = branches.get(branchId);
			if (b != null) {
				b.transfer(accountIdOrig, accountIdDest, amount);
			} else {
				throw new InexistentBranchException(branchId);
			}
		} else {
			throw new InexistentBranchException(
					"Partition not yet initialized.", branchId);
		}
	}

	/**
	 * Calculates the exposure for a branch, i.e. sums up the balances of all
	 * accounts with negative balances and returns the absolute value.
	 * 
	 * @param branchId
	 *            the ID of the branch from which to calculate the exposure
	 * @return the branche's exposure
	 * @throws InexistentBranchException
	 *             if the branch ID is unknown in this partition
	 */
	@WebMethod
	public double calculateExposure(int branchId)
			throws InexistentBranchException {
		if (initialized) {
			Branch b = branches.get(branchId);
			if (b != null) {
				return b.calculateExposure();
			} else {
				throw new InexistentBranchException(branchId);
			}
		} else {
			throw new InexistentBranchException(
					"Partition not yet initialized.", branchId);
		}
	}

	/**
	 * Sets the balance for a specific account; creates the account if it does
	 * not exist.
	 * 
	 */
	private void setBalance(int branchId, int accountId, double balance) {
		branches.get(branchId).setBalance(accountId, balance);
	}

	/**
	 * Creates the specified accounts and sets their balances. Exposed as a web
	 * service, only to be used by the proxy during initialization. Can only be
	 * used between calls to startInitializing and setInitialized.
	 * 
	 * @param branchId
	 *            the ID of the branch to which the accounts should be added
	 * @param accounts
	 *            a list of accounts that should be added to the branch
	 */
	@WebMethod
	public void setBalances(int branchId, List<Account> accounts) {
		if (!initialized && initializing) {
			if (!branches.containsKey(branchId)) {
				Branch b;
				
				// create an optimistic branch if specified
				if (optimistic)
					b = new OptimisticBranch();
				else
					b = new PessimisticBranch();
				branches.put(branchId, b);
			}
			for (Account a : accounts) {
				setBalance(branchId, a.getId(), a.getBalance());
			}
		}
	}

	/**
	 * Sets this partition's status to initialized. Partition can only be used
	 * after this has been called. Can only be called after startInitializing is
	 * called. Should only be used by the proxy.
	 */
	@WebMethod
	public void setInitialized() {
		if (initializing) {
			System.out.println("Partition initialized!");
			System.out.println(Calendar.getInstance().toString());
			initialized = true;
			initializing = false;
		}
	}

	/**
	 * If this partition has not yet been initialized and is not currently being
	 * initialized by some proxy, changes this partition's status to
	 * initializing. Needs to be called before setBalances can be used.
	 * 
	 * @return true if this partition can be intialized, false if it is already
	 *         ititialized or is currently initializing
	 */
	@WebMethod
	public boolean startInitializing() {
		if (initialized || initializing)
			return false;
		initializing = true;
		return true;
	}
}
