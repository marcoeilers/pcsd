package test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment1.impl.StringLengthPredicate;
import dk.diku.pcsd.assignment1.impl.ValueImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;

public class ScanTest {
	
	// used for making all keys equally long
	private static final int keyLength = 5;
	
	private static final KeyValueBaseImplService kvbis = new KeyValueBaseImplServiceService().getKeyValueBaseImplServicePort();
	
	@BeforeClass
	public static void setup() {
		try {
			kvbis.init(null);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Test
	public void testScanUnderUpdate() {
		final int fromIndex = 0;
		final int numKeys = 5000;
		final int toIndex = fromIndex + numKeys - 1;
		
		// initialize our range with value that is found by the predicate
		insertRange(fromIndex, toIndex, "initialValue");
		
		// prepare search keys
		final KeyImpl from = new KeyImpl();
		from.setKey(padZeros(Integer.toString(fromIndex), keyLength));
		
		final KeyImpl to = new KeyImpl();
		to.setKey(padZeros(Integer.toString(toIndex), keyLength));
		
		// start the scan
		final List<ValueListImpl> scanResult = new ArrayList<ValueListImpl>();
		Thread scanner = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scanResult.addAll(kvbis.scan(from, to, new StringLengthPredicate()));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		});
		scanner.start();
		
		// updater thread
		Thread updater = new Thread(new Runnable() {
			@Override
			public void run() {
				// update with shorter value so they will not be found
				updateRange(fromIndex, toIndex, "newV");
			}
		});
		updater.start();
		
		try {
			updater.join();
			scanner.join();
		} catch(InterruptedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Assert.assertTrue("Invalid size of result set: " + scanResult.size() + ", expecting less than " + numKeys, scanResult.size() < numKeys);
	}
	
	@Test
	public void testScanUnderDelete() {
		final int fromIndex = 5000;
		final int numKeys = 5000;
		final int toIndex = fromIndex + numKeys - 1;
		
		// initialize our range with value that is found by the predicate
		insertRange(fromIndex, toIndex, "initialValue");
		
		// prepare search keys
		final KeyImpl from = new KeyImpl();
		from.setKey(padZeros(Integer.toString(fromIndex), keyLength));
		
		final KeyImpl to = new KeyImpl();
		to.setKey(padZeros(Integer.toString(toIndex), keyLength));
		
		// start the scan
		final List<ValueListImpl> scanResult = new ArrayList<ValueListImpl>();
		Thread scanner = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scanResult.addAll(kvbis.scan(from, to, new StringLengthPredicate()));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		});
		scanner.start();
		
		// deleter thread
		Thread deleter = new Thread(new Runnable() {
			@Override
			public void run() {
				deleteRange(fromIndex, toIndex);
			}
		});
		deleter.start();
		
		try {
			deleter.join();
			scanner.join();
		} catch(InterruptedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Assert.assertTrue("Invalid size of result set: " + scanResult.size() + ", expecting less than " + numKeys, scanResult.size() < numKeys);
	}
	
	@Test
	public void testAtomicScanUnderUpdate() {
		final int fromIndex = 10000;
		final int numKeys = 20000;
		final int toIndex = fromIndex + numKeys - 1;
		
		insertRange(fromIndex, toIndex, "initialValue");
		
		// prepare search keys
		final KeyImpl from = new KeyImpl();
		from.setKey(padZeros(Integer.toString(fromIndex), keyLength));
		
		final KeyImpl to = new KeyImpl();
		to.setKey(padZeros(Integer.toString(toIndex), keyLength));
		
		// start the scan
		final List<ValueListImpl> scanResult = new ArrayList<ValueListImpl>();
		Thread scanner = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scanResult.addAll(kvbis.atomicScan(from, to, new StringLengthPredicate()));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		});
		scanner.start();
		
		// give the scanner a chance to start
		try {
			Thread.sleep(500);
		} catch(InterruptedException e) {
			
		}
		
		// updater thread
		Thread updater = new Thread(new Runnable() {
			@Override
			public void run() {
				// update with shorter value so they will not be found
				updateRange(fromIndex, toIndex, "newV");
			}
		});
		updater.start();
		
		try {
			updater.join();
			scanner.join();
		} catch(InterruptedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Assert.assertTrue("Invalid size of result set: " + scanResult.size() + ", expecting " + numKeys, scanResult.size() == numKeys);
	}
	
	@Test
	public void testAtomicScanUnderDelete() {
		final int fromIndex = 30000;
		final int numKeys = 20000;
		final int toIndex = fromIndex + numKeys - 1;
		
		insertRange(fromIndex, toIndex, "initialValue");
		
		// prepare search keys
		final KeyImpl from = new KeyImpl();
		from.setKey(padZeros(Integer.toString(fromIndex), keyLength));
		
		final KeyImpl to = new KeyImpl();
		to.setKey(padZeros(Integer.toString(toIndex), keyLength));
		
		// start the scan
		final List<ValueListImpl> scanResult = new ArrayList<ValueListImpl>();
		Thread scanner = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scanResult.addAll(kvbis.atomicScan(from, to, new StringLengthPredicate()));
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e.getCause());
				}
			}
		});
		scanner.start();
		
		// give the scanner a chance to start
		try {
			Thread.sleep(500);
		} catch(InterruptedException e) {
			
		}
		
		// deleter thread
		Thread deleter = new Thread(new Runnable() {
			@Override
			public void run() {
				deleteRange(fromIndex, toIndex);
			}
		});
		deleter.start();
		
		try {
			deleter.join();
			scanner.join();
		} catch(InterruptedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		Assert.assertTrue("Invalid size of result set: " + scanResult.size() + ", expecting " + numKeys, scanResult.size() == numKeys);
	}
	
	private void insertRange(int from, int to, String v) {
		for(int i = from; i <= to; ++i) {
			KeyImpl key = new KeyImpl();
			key.setKey(padZeros(Integer.toString(i), keyLength));
			
			ValueListImpl valueList = new ValueListImpl();
			ValueImpl value = new ValueImpl();
			value.setValue(v);
			valueList.getValueList().add(value);
			
			try {
				kvbis.insert(key, valueList);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} 
		}
	}
	
	private void updateRange(int from, int to, String newV) {
		for(int i = from; i <= to; ++i) {
			KeyImpl key = new KeyImpl();
			key.setKey(padZeros(Integer.toString(i), keyLength));
			
			ValueListImpl valueList = new ValueListImpl();
			ValueImpl value = new ValueImpl();
			value.setValue(newV);
			valueList.getValueList().add(value);
			
			try {
				kvbis.update(key, valueList);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} 
		}
	}
	
	private void deleteRange(int from, int to) {
		for(int i = from; i <= to; ++i) {
			KeyImpl key = new KeyImpl();
			key.setKey(padZeros(Integer.toString(i), keyLength));
			
			try {
				kvbis.delete(key);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} 
		}
	}
	
	private String padZeros(String s, int length) {
		while(s.length() < length)
			s = "0" + s;
		return s;
	}

}
