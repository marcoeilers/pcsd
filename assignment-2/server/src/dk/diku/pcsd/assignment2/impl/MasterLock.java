package dk.diku.pcsd.assignment2.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MasterLock {
	private static ReadWriteLock lock = new ReentrantReadWriteLock(true);
	
	public static Lock getSharedLock(){
		return lock.readLock();
	}
	
	public static Lock getExclusiveLock(){
		return lock.writeLock();
	}
	
}
