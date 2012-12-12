package dk.diku.pcsd.assignment2.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Central lock that has to be acquired for all requests. Used to pause the
 * store for creating checkpoints. Normal operations get shared lock, exclusive
 * lock is used by checkpointer to quiesce the store.
 * 
 */
public class MasterLock {
	// use the fair scheduler so that the checkpointer will eventually get its lock.
	private static ReadWriteLock lock = new ReentrantReadWriteLock(true);

	public static Lock getSharedLock() {
		return lock.readLock();
	}

	public static Lock getExclusiveLock() {
		return lock.writeLock();
	}

}
