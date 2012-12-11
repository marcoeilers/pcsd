package assg1test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.assignment1.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.IOException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment1.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ValueImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;

public class MultiReadTest {
	static KeyValueBaseImplServiceService kvbiss;
	static KeyValueBaseImplService kvbis;
	
	static HashMap<String, String> testMap = new HashMap<String, String>();
	static ArrayList<String> keys;
	
	static boolean testSuccessfull = true;
	
	
	@BeforeClass 
	public static void testSetup(){
		kvbiss = new KeyValueBaseImplServiceService();
		kvbis = kvbiss.getKeyValueBaseImplServicePort();
		try {
			kvbis.init(null);
		} catch (FileNotFoundException_Exception e) {
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void parallelRead() {
		//how often
		int N = 1000;
		
		//how many threads
		int h = 10;
		//how many reads per thread
		int n = N / h;
		
		Random rnd = new Random();		
		
		//write N key values in hashmap and store
		for (int i=0; i<N; i++) {
				String keyValue = String.valueOf(rnd.nextInt(999999));
				String resultValue = String.valueOf(rnd.nextInt(99999));
				
				KeyImpl key = new KeyImpl();
				key.setKey(keyValue);
				
				ValueImpl value = new ValueImpl();
				value.setValue(resultValue);
				
				ValueListImpl valueList = new ValueListImpl();
				valueList.getValueList().add(value);
				try{
				kvbis.insert(key, valueList);
				testMap.put(keyValue, resultValue);
				} catch (KeyAlreadyPresentException_Exception e) {
					//do nothing
				} catch (IOException_Exception e) {
					e.printStackTrace();
				} catch (ServiceNotInitializedException_Exception e) {
					e.printStackTrace();
				}
			}
			
			keys = new ArrayList<String>(testMap.keySet());
		
			//make threads and let them read
			ExecutorService executor = Executors.newFixedThreadPool(h);
		    for (int i = 0; i < h; i++) {
		      Runnable worker = new ReadThread(n);
		      executor.execute(worker);
		    }
		    // This will make the executor accept no new threads
		    // and finish all existing threads in the queue
		    executor.shutdown();
		    // Wait until all threads are finish
		    while (!executor.isTerminated()) {

		    }
		    		
		    //if one read failed fail test case
		    assertTrue("Result", testSuccessfull);
		
	}
	
	public static class ReadThread implements Runnable {
		int n;
		
		ReadThread(int n) {
			  this.n = n;
		}

		@Override
		public void run() {
			Random rnd = new Random();
			String actualValue = "";
			String expectedValue = "";
			
			//get a random key value pair from hash map
			for (int i=0;i<this.n;i++){
				try {
				String randomReadKey = keys.get(rnd.nextInt(keys.size()));
				
				KeyImpl keyRead = new KeyImpl();
				keyRead.setKey(randomReadKey);
				
				expectedValue = testMap.get(randomReadKey);
				actualValue = kvbis.read(keyRead).getValueList().get(0).getValue().toString();
				} catch (IOException_Exception e) {
					e.printStackTrace();
				} catch (KeyNotFoundException_Exception e) {
					e.printStackTrace();
				} catch (ServiceNotInitializedException_Exception e) {
					e.printStackTrace();
				}
				
				if (!expectedValue.equals(actualValue)) {
					testSuccessfull = false;
					System.out.print("Fehler: " + expectedValue + actualValue);
				}
				
			}	
		}
	} 
}
