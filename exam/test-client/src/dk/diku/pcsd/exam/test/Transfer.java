package dk.diku.pcsd.exam.test;

import java.util.Map;

public class Transfer extends Transaction {
	private int accountIdOrig;
	private int accountIdDest;
	private double amount;
	public int getAccountIdOrig() {
		return accountIdOrig;
	}
	public void setAccountIdOrig(int accountIdOrig) {
		this.accountIdOrig = accountIdOrig;
	}
	public int getAccountIdDest() {
		return accountIdDest;
	}
	public void setAccountIdDest(int accountIdDest) {
		this.accountIdDest = accountIdDest;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Transfer(int accountIdOrig, int accountIdDest, double amount) {
		super();
		this.accountIdOrig = accountIdOrig;
		this.accountIdDest = accountIdDest;
		this.amount = amount;
	}
	
	@Override
	public void execute(Map<Integer, Double> balances) {
		balances.put(accountIdOrig, balances.get(accountIdOrig) - amount);
		balances.put(accountIdDest, balances.get(accountIdDest) + amount);
	}
}
