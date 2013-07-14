package dk.diku.pcsd.exam.partition;

/**
 * Container for the data that defines an account, i.e. its accountId and
 * balance.
 * 
 * @author marco
 * 
 */
public class Account {
	private Double balance;
	private int id;

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Account(int id, Double balance) {
		this.id = id;
		this.balance = balance;
	}

	public Account() {
	}
}
