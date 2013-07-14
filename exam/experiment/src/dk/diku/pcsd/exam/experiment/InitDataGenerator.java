package dk.diku.pcsd.exam.experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Generates random test data for initializing the AccountManager service.
 * Uses gaussian distributions for balances and the number of accounts per branch.
 * 
 * @author marco
 *
 */
public class InitDataGenerator {
	// name of the output file that should be generated in user.home
	private static final String outputFile = "account.init";
	private static final int noOfBranches = 100;
	
	// mean number of accounts per branch
	private static final double accountsPerBranchMean = 5000.0;
	// standard deviation of the accounts per branch
	private static final double accountsPerBranchStdDev = 2000.0;	

	private Random rnd = new Random();
	private Set<Integer> accounts = new HashSet<Integer>();
	private Set<Integer> branches = new HashSet<Integer>();

	/**
	 * Optionally takes as an argument the name of the output file 
	 */
	public static void main(String[] args) {
		InitDataGenerator gen = new InitDataGenerator();

		String filename = args.length > 0 ? args[0] : outputFile;

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			for (int i=0; i<noOfBranches; i++){
				int branch = gen.getBranchId();
				bw.write("branch\t"+branch+"\n");
				int noOfAccounts = -1;
				while (noOfAccounts < 1)
					noOfAccounts = gen.getNormalDistInt(accountsPerBranchMean, accountsPerBranchStdDev);
				for (int j =0; j<noOfAccounts; j++){
					int account= gen.getAccountId();
					double balance = gen.getRandomBalance();
					bw.write(""+account+"\t"+balance+"\n");
				}
				gen.resetAccounts();
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets a random value from a gaussian distribution with the specified properties.
	 * 
	 * @param mean the gaussian's mean
	 * @param stddev the gaussian's standard deviation
	 * @return a random value from the distribution
	 */
	private double getNormalDistVal(double mean, double stddev) {
		return rnd.nextGaussian() * stddev + mean;
	}
	
	/**
	 * Same as getNormalDistVal, but rounds to the next lower integer
	 * @param mean
	 * @param stddev
	 * @return
	 */
	private int getNormalDistInt(double mean, double stddev){
		return (int) getNormalDistVal(mean, stddev);
	}

	private double getRandomBalance() {
		// balances are taken from a gaussian with mean 7000
		// and standard deviation 15000
		double balance = getNormalDistVal(7000.0, 15000.0);
		return (Math.round(balance * 100.0) / 100.0);
	}

	private int getAccountId() {
		// account IDs are integers smaller than 100000
		int newInt = rnd.nextInt(100000);
		if (accounts.contains(newInt)) {
			return getAccountId();
		} else {
			accounts.add(newInt);
			return newInt;
		}
	}

	private int getBranchId() {
		// branch IDs are integers smaller than 10000
		int newInt = rnd.nextInt(10000);
		if (branches.contains(newInt)) {
			return getBranchId();
		} else {
			branches.add(newInt);
			return newInt;
		}
	}
	
	private void resetAccounts(){
		accounts = new HashSet<Integer>();
	}
}
