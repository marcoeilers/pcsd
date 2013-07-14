package test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.diku.pcsd.assignment3.proxy.impl.Configuration;
import dk.diku.pcsd.assignment3.proxy.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.KeyImpl;
import dk.diku.pcsd.assignment3.proxy.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.KeyValueBaseProxyImplService;
import dk.diku.pcsd.assignment3.proxy.impl.KeyValueBaseProxyImplServiceService;
import dk.diku.pcsd.assignment3.proxy.impl.ServiceAlreadyConfiguredException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment3.proxy.impl.ValueImpl;
import dk.diku.pcsd.assignment3.proxy.impl.ValueListImpl;

public class MultiReadWriteTest {
	static KeyValueBaseProxyImplServiceService kvbiss;
	static KeyValueBaseProxyImplService kvbis;

	static HashMap<String, String> testMap = new HashMap<String, String>();
	static ArrayList<String> keys;

	static boolean testSuccessfull = true;

	@BeforeClass
	public static void testSetup() {
		kvbiss = new KeyValueBaseProxyImplServiceService();
		kvbis = kvbiss.getKeyValueBaseProxyImplServicePort();
		Configuration conf = new Configuration();
		conf.setMaster("http://localhost:8080/master/keyvaluebasemaster?wsdl");
		conf.getSlaves().add(
				"http://localhost:8080/slave/keyvaluebaseslave?wsdl");

		try {
			kvbis.config(conf);
			kvbis.init("");
		} catch (dk.diku.pcsd.assignment3.proxy.impl.FileNotFoundException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (dk.diku.pcsd.assignment3.proxy.impl.ServiceAlreadyInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (dk.diku.pcsd.assignment3.proxy.impl.ServiceInitializingException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceAlreadyConfiguredException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void parallelRead() {
		// how often before
		int N = 1000;

		// how many threads
		int h = 10;
		// how many writes per thread (makes 10 reads per write))
		int n = 100;

		Random rnd = new Random();

		// write N key values in hashmap and store
		for (int i = 0; i < N; i++) {
			String keyValue = String.valueOf(rnd.nextInt(999999));
			String resultValue = String.valueOf(rnd.nextInt(99999));

			KeyImpl key = new KeyImpl();
			key.setKey(keyValue);

			ValueImpl value = new ValueImpl();
			value.setValue(resultValue);

			ValueListImpl valueList = new ValueListImpl();
			valueList.getValueList().add(value);
			try {
				
					kvbis.insert(key, valueList);
					testMap.put(keyValue, resultValue);
			} catch (KeyAlreadyPresentException_Exception e) {
				assertTrue(testMap.containsKey(keyValue));
			} catch (IOException_Exception e) {
				e.printStackTrace();
			} catch (ServiceNotInitializedException_Exception e) {
				e.printStackTrace();
			}
		}

		keys = new ArrayList<String>(testMap.keySet());

		// make threads and let them read and write
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

		// if one read failed fail test case
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

			for (int i = 0; i < this.n; i++) {
				// write here
				String keyValue = String.valueOf(rnd.nextInt(999999));
				String resultValue = String.valueOf(rnd.nextInt(99999));

				KeyImpl key = new KeyImpl();
				key.setKey(keyValue);

				ValueImpl value = new ValueImpl();
				value.setValue(resultValue);

				ValueListImpl valueList = new ValueListImpl();
				valueList.getValueList().add(value);
				try {
					kvbis.insert(key, valueList);
					testMap.put(keyValue, resultValue);
				} catch (KeyAlreadyPresentException_Exception e) {
					assertTrue(testMap.containsKey(keyValue));
				} catch (IOException_Exception e) {
					e.printStackTrace();
				} catch (ServiceNotInitializedException_Exception e) {
					e.printStackTrace();
				}

				// read here
				for (int j = 0; j < 10; j++) {
					try {
						String randomReadKey = keys
								.get(rnd.nextInt(keys.size()));

						KeyImpl keyRead = new KeyImpl();
						keyRead.setKey(randomReadKey);

						expectedValue = testMap.get(randomReadKey);
						actualValue = kvbis.read(keyRead).getValueList().get(0)
								.getValue().toString();

					} catch (IOException_Exception e) {
						e.printStackTrace();
					} catch (KeyNotFoundException_Exception e) {
						e.printStackTrace();
					} catch (ServiceNotInitializedException_Exception e) {
						e.printStackTrace();
					}

					if (!expectedValue.equals(actualValue)) {
						testSuccessfull = false;
						System.out.println("Fehler: " + expectedValue + " "
								+ actualValue);
					}
				}

			}
		}
	}
}
