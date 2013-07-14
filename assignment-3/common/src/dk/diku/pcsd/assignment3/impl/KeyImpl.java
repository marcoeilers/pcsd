package dk.diku.pcsd.assignment3.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dk.diku.pcsd.keyvaluebase.interfaces.Key;

public class KeyImpl implements Key<KeyImpl>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2576953308178505105L;

	private static HashMap<String, ReadWriteLock> locks = new HashMap<String, ReadWriteLock>();
	
	private String key;
	
	public KeyImpl() {
	}
	
	public KeyImpl(String key){
		this.key=key;
	}

	@Override
	public int compareTo(KeyImpl k) {
		return key.compareTo(k.getKey());
	}
	
	public String toString() {
		return key.toString();
	}

	public String getKey(){
		return key;
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof KeyImpl){
			KeyImpl k =(KeyImpl) o;
			return key.equals(k.getKey());
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return key.hashCode();
	}
	
	public Lock getReadLock(){
		synchronized(key.intern()){
			ReadWriteLock lock = locks.get(key);
			if (lock == null){
				ReadWriteLock newLock = new ReentrantReadWriteLock(true); 
				locks.put(key, newLock);
			}
			return locks.get(key).readLock();
		}
	}
	
	public Lock getWriteLock(){
		synchronized(key.intern()){
			ReadWriteLock lock = locks.get(key);
			if (lock == null){
				ReadWriteLock newLock = new ReentrantReadWriteLock(true); 
				locks.put(key, newLock);
			}
			return locks.get(key).writeLock();
		}
	}
	
	public void removeLock(){
		synchronized(key.intern()){
			ReadWriteLock lock = locks.get(key);
			if (lock != null){
				locks.remove(lock);
			}
		}
	}

}
