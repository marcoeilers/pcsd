package dk.diku.pcsd.exam.partition;

import dk.diku.pcsd.exam.common.exceptions.InexistentAccountException;

/**
 * Interface that describes the functions a branch has to implement. Implemented
 * by OptimisticBranch and PessimisticBranch with different types of concurrency
 * control.
 * 
 * @author marco
 * 
 */
public interface Branch {
	public void credit(int accountId, double amount)
			throws InexistentAccountException;

	public void debit(int accountId, double amount)
			throws InexistentAccountException;

	public void transfer(int accountIdOrig, int accountIdDest, double amount)
			throws InexistentAccountException;

	public double calculateExposure();

	public void setBalance(int accountId, double balance);

	public double getBalance(int accountId) throws InexistentAccountException;
}
