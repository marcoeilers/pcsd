package dk.diku.pcsd.exam.test;

import java.util.Map;

public class Credit extends Transaction {
	private int accountId;
	private double amount;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Credit(int accountId, double amount) {
		super();
		this.accountId = accountId;
		this.amount = amount;
	}

	@Override
	public void execute(Map<Integer, Double> balances) {
		balances.put(accountId, balances.get(accountId) + amount);
	}

}
