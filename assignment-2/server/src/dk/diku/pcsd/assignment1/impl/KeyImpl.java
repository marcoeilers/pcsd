package dk.diku.pcsd.assignment1.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dk.diku.pcsd.keyvaluebase.interfaces.Key;

/**
 * Stores a key (which has to be a string). A key is comparable: The comparison
 * will have the same result as a direct comparison of the contained strings.
 * This class also manages ReadWriteLocks for every single key currently present
 * in the store.
 * 
 */
public class KeyImpl implements Key<KeyImpl>, Serializable {
	private static final long serialVersionUID = -7705948534566626626L;

	// locks for all keys in the store
	private static HashMap<String, ReadWriteLock> locks = new HashMap<String, ReadWriteLock>();

	private String key;

	public KeyImpl() {
	}

	public KeyImpl(String key) {
		this.key = key;
	}

	@Override
	public int compareTo(KeyImpl k) {
		return key.compareTo(k.getKey());
	}

	public String toString() {
		return key.toString();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof KeyImpl) {
			KeyImpl k = (KeyImpl) o;
			return key.equals(k.getKey());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	/**
	 * Returns a read lock for this key. Creates it if there is none. Does NOT
	 * actually lock the key, this has to be done manually with the returned
	 * lock.
	 * 
	 * @return a read lock for this key
	 */
	public Lock getReadLock() {
		synchronized (key.intern()) {
			ReadWriteLock lock = locks.get(key);
			if (lock == null) {
				ReadWriteLock newLock = new ReentrantReadWriteLock(true);
				locks.put(key, newLock);
				return newLock.readLock();
			}
			return locks.get(key).readLock();
		}
	}

	/**
	 * Returns a write lock for this key. Creates it if there is none. Does NOT
	 * actually lock the key, this has to be done manually with the returned
	 * lock.
	 * 
	 * @return a write lock for this key
	 */
	public Lock getWriteLock() {
		synchronized (key.intern()) {
			ReadWriteLock lock = locks.get(key);
			if (lock == null) {
				ReadWriteLock newLock = new ReentrantReadWriteLock(true);
				locks.put(key, newLock);
				return newLock.writeLock();
			}
			return locks.get(key).writeLock();
		}
	}
	
	/**
	 * Removes this key's lock from the lock list. Should only be used when a
	 * key-value-pair is deleted from the store.
	 */
	public void removeLock() {
		synchronized (key.intern()) {
			ReadWriteLock lock = locks.get(key);
			if (lock != null) {
				locks.remove(lock);
			}
		}
	}

}
