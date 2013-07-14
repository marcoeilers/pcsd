package dk.diku.pcsd.exam.test;

import java.util.Map;

public abstract class Transaction {
	public abstract void execute(Map<Integer, Double> balances);
}
