package assg2test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dk.diku.pcsd.assignment1.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.IOException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment1.impl.Pair;
import dk.diku.pcsd.assignment1.impl.PairImpl;
import dk.diku.pcsd.assignment1.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ValueImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;

public class BasicRecoveryTest {

	private static String storeFile = "/home/marco/apache-tomcat-7.0.33/temp/store.mmf";
	private static String logFile = "/home/marco/kvstore.log";
	private static String indexFile = "/home/marco/kvindex.ind";

	private static final String tomcatBinDir = "/home/marco/apache-tomcat-7.0.33/bin/";

	private static void waitForProc(Process p) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		try {
			while ((reader.readLine()) != null) {
			}
			p.waitFor();
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}

	}
	
	@Test
	public void checkWrittenFiles(){
		deleteFiles();
		
		startServer();
		
		KeyValueBaseImplService kvbis = getWebService();
		
		try {
			kvbis.init("");
			
			KeyImpl insertKey = new KeyImpl();
			insertKey.setKey("newKey1");

			KeyImpl insertKey2 = new KeyImpl();
			insertKey2.setKey("newKey2");

			ValueListImpl vl1 = new ValueListImpl();
			ValueImpl v1 = new ValueImpl();
			v1.setValue("value1");
			vl1.getValueList().add(v1);

			ValueListImpl vl2 = new ValueListImpl();
			ValueImpl v2 = new ValueImpl();
			v2.setValue("value2");
			vl2.getValueList().add(v2);
			
			kvbis.insert(insertKey, vl1);
			kvbis.update(insertKey, vl2);
			kvbis.insert(insertKey2, vl2);
			kvbis.delete(insertKey2);
			
			File log = new File(logFile);
			assertTrue(log.exists());
			
			File store = new File(storeFile);
			assertTrue(store.exists());
			
			File index = new File(indexFile);
			assertTrue(!index.exists());
			
			Thread.sleep(18000);
			
			stopServer();
			
			log = new File(logFile);
			assertTrue(log.exists());
			
			store = new File(storeFile);
			assertTrue(store.exists());
			
			index = new File(indexFile);
			assertTrue(index.exists());
			
			
		} catch (FileNotFoundException_Exception e) {
			fail();
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			fail();
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			fail();
			e.printStackTrace();
		} catch (IOException_Exception e) {
			fail();
			e.printStackTrace();
		} catch (KeyAlreadyPresentException_Exception e) {
			fail();
			e.printStackTrace();
		} catch (ServiceNotInitializedException_Exception e) {
			fail();
			e.printStackTrace();
		} catch (KeyNotFoundException_Exception e) {
			fail();
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}
		
		
		
		
	}

	@Test
	public void testDirectRecovery() {
		testRecovery(1500);
	}

	@Test
	public void testCheckpointRecovery() {
		testRecovery(20000);
	}

	@Test
	public void testInitialization() {
		boolean result = false;
		try {
			startServer();

			Thread.sleep(6000);
			KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
			KeyValueBaseImplService kvbis = kvbiss
					.getKeyValueBaseImplServicePort();

			try {
				kvbis.init("testData.txt");
				fail();
			} catch (FileNotFoundException_Exception e) {
				fail();
				e.printStackTrace();
			} catch (ServiceAlreadyInitializedException_Exception e) {
				result = true;
			} catch (ServiceInitializingException_Exception e) {
				fail();
				e.printStackTrace();
			}finally{
				stopServer();
			}
			
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		
		assertTrue(result);
	}

	public void testRecovery(long sleepTime) {

		deleteFiles();
		
		boolean result = false;

		try {
			startServer();
			KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
			KeyValueBaseImplService kvbis = kvbiss
					.getKeyValueBaseImplServicePort();

			kvbis.init("testData.txt");

			KeyImpl key = new KeyImpl();
			key.setKey("15");

			KeyImpl key16 = new KeyImpl();
			key16.setKey("16");

			KeyImpl key18 = new KeyImpl();
			key18.setKey("18");

			KeyImpl insertKey = new KeyImpl();
			insertKey.setKey("newKey");

			KeyImpl insertKey2 = new KeyImpl();
			insertKey2.setKey("newKey2");

			ValueListImpl vl1 = new ValueListImpl();
			ValueImpl v1 = new ValueImpl();
			v1.setValue("aaaaaaa");
			vl1.getValueList().add(v1);

			ValueListImpl vl2 = new ValueListImpl();
			ValueImpl v2 = new ValueImpl();
			v2.setValue("aaaaaaaAAAAAAAAA32234AASd");
			vl2.getValueList().add(v2);

			ValueListImpl vl3 = new ValueListImpl();
			ValueImpl v3 = new ValueImpl();
			v3.setValue("insertedValue");
			vl3.getValueList().add(v3);
			
			List<Pair> pairs = new ArrayList<Pair>();
			
			KeyImpl bulkKey = new KeyImpl();
			bulkKey.setKey("bulk1");

			ValueListImpl bvall = new ValueListImpl();
			ValueImpl bval = new ValueImpl();
			bval.setValue("bulkVal1");
			bvall.getValueList().add(bval);
			
			PairImpl p = new PairImpl();
			p.setKey(bulkKey);
			p.setValue(bvall);
			
			KeyImpl bulkKey2 = new KeyImpl();
			bulkKey2.setKey("bulk2");

			ValueListImpl bvall2 = new ValueListImpl();
			ValueImpl bval2 = new ValueImpl();
			bval2.setValue("bulkVal2");
			bvall2.getValueList().add(bval2);
			
			PairImpl p2 = new PairImpl();
			p2.setKey(bulkKey2);
			p2.setValue(bvall2);
			
			pairs.add(p);	
			pairs.add(p2);

			kvbis.insert(insertKey, vl1);

			kvbis.update(insertKey, vl2);

			kvbis.update(key, vl1);

			kvbis.delete(key18);

			kvbis.insert(insertKey2, vl3);
			
			kvbis.bulkPut(pairs);

			Thread.sleep(sleepTime);
			
			restartServer();
			
			Thread.sleep(7000);

			KeyValueBaseImplServiceService kvbiss2 = new KeyValueBaseImplServiceService();
			KeyValueBaseImplService kvbis2 = kvbiss2
					.getKeyValueBaseImplServicePort();

			assertEquals("aaaaaaa", kvbis2.read(key).getValueList().get(0)
					.getValue());
			assertEquals("aaaaaaaAAAAAAAAA32234AASd", kvbis2.read(insertKey)
					.getValueList().get(0).getValue());
			assertEquals("78s", kvbis2.read(key16).getValueList().get(0)
					.getValue());
			assertEquals("insertedValue", kvbis2.read(insertKey2)
					.getValueList().get(0).getValue());
			assertEquals("bulkVal1", kvbis2.read(bulkKey)
					.getValueList().get(0).getValue());

			try {
				kvbis2.read(key18);
				fail();
			} catch (KeyNotFoundException_Exception e) {
				result = true;
			}

		} catch (InterruptedException e1) {
			fail();
			e1.printStackTrace();
		} catch (IOException_Exception e1) {
			fail();
			e1.printStackTrace();
		} catch (KeyNotFoundException_Exception e1) {
			e1.printStackTrace();
			fail();
		} catch (ServiceNotInitializedException_Exception e1) {
			fail();
		} catch (KeyAlreadyPresentException_Exception e1) {
			fail();
		} catch (FileNotFoundException_Exception e1) {
			fail("Init file not found : "+e1.getMessage());
			e1.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e1) {
			fail();
		} catch (ServiceInitializingException_Exception e1) {
			fail();
		} finally {
			stopServer();
		}
		
		assertTrue(result);
	}
	
	private void startServer(){
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(
					tomcatBinDir + "startup.sh");
			waitForProc(proc);

			Thread.sleep(6000);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	private void stopServer(){
		Process proc;
		try {
			proc = Runtime.getRuntime().exec(tomcatBinDir + "shutdown.sh");
			waitForProc(proc);
			Thread.sleep(2000);
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	private void restartServer(){
		stopServer();
		startServer();
	}

	private void deleteFiles(){
		File store = new File(storeFile);
		if (store.exists())
			store.delete();

		File log = new File(logFile);
		if (log.exists())
			log.delete();

		File index = new File(indexFile);
		if (index.exists())
			index.delete();
	}
	
	private KeyValueBaseImplService getWebService(){
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss
				.getKeyValueBaseImplServicePort();
		return kvbis;
	}
}
