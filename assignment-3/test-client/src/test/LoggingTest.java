package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dk.diku.pcsd.assignment3.master.impl.Configuration;
import dk.diku.pcsd.assignment3.master.impl.KeyImpl;
import dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.Pair;
import dk.diku.pcsd.assignment3.master.impl.PairImpl;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.TimestampPair;
import dk.diku.pcsd.assignment3.master.impl.ValueImpl;
import dk.diku.pcsd.assignment3.master.impl.ValueListImpl;

public class LoggingTest {
	
	private KeyValueBaseMasterImplServiceService kvbiss;
	private KeyValueBaseMasterImplService kvbis;
	
	@Before
	public void setup() {
		startTomcat();
		kvbiss = new KeyValueBaseMasterImplServiceService();
		kvbis = kvbiss.getKeyValueBaseMasterImplServicePort();
	}
	
	@Test
	public void testServiceAlreadyInitialized() {
		System.out.println("testServiceAlreadyInitialized");
		
		// initialize server the first time
		try {
			Configuration cfg = new Configuration();
			cfg.getSlaves().add("http://localhost:8081/pcsd-assignment3-slave/keyvaluebaseslave?wsdl");
			kvbis.config(cfg);
			kvbis.init("dk/init.list");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		// restart it
		stopTomcat();
		startTomcat();
		
		boolean failed = false;
		try {
			kvbis.init("dk/init.list");
		} catch (Exception e) {
			failed = e instanceof ServiceAlreadyInitializedException_Exception;
		}
		
		Assert.assertTrue("Was expecting ServiceAlreadyInitializedException", failed);
	}
	
	@Test
	public void testInitializedValue() {
		System.out.println("testInitializedValue");
		
		KeyImpl key = new KeyImpl();
		key.setKey("42");
		try {
			ValueImpl v = ((TimestampPair) kvbis.read(key)).getV().getValueList().get(0);
			Assert.assertEquals(84, v.getValue());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Test
	public void testUpdate() {
		System.out.println("testUpdate");
		
		KeyImpl key = new KeyImpl();
		key.setKey("42");
		
		ValueListImpl valueList = new ValueListImpl();
		ValueImpl value = new ValueImpl();
		value.setValue(8484);
		valueList.getValueList().add(value);
		
		try {
			kvbis.update(key, valueList);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		stopTomcat();
		startTomcat();
		
		try {
			ValueImpl v = ((TimestampPair) kvbis.read(key)).getV().getValueList().get(0);
			Assert.assertEquals(8484, v.getValue());
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Test
	public void testCheckpointer() {
		System.out.println("testCheckpointer");
		
		KeyImpl key = new KeyImpl();
		key.setKey("42");
		
		ValueListImpl valueList = new ValueListImpl();
		ValueImpl value = new ValueImpl();
		value.setValue(4242);
		valueList.getValueList().add(value);
		
		try {
			kvbis.update(key, valueList);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		// make the checkpointer run
		try {
			Thread.sleep(40000);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		try {
			ValueImpl v = ((TimestampPair) kvbis.read(key)).getV().getValueList().get(0);
			Assert.assertEquals(4242, v.getValue());
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		stopTomcat();
		startTomcat();
		
		try {
			ValueImpl v = ((TimestampPair) kvbis.read(key)).getV().getValueList().get(0);
			Assert.assertEquals(4242, v.getValue());
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	// @Test
	public void testBulkPut() {
		System.out.println("testBulkPut");
		
		Random rnd = new Random(1);
		List<Pair> pairs = new ArrayList<Pair>();
		for(int i = 0; i < 20; ++i) {			
			KeyImpl key = new KeyImpl();
			key.setKey(Integer.toString(rnd.nextInt(99999)));
			
			ValueListImpl valueList = new ValueListImpl();
			ValueImpl value = new ValueImpl();
			value.setValue(i);
			valueList.getValueList().add(value);
			
			PairImpl pair = new PairImpl();
			pair.setK(key);
			pair.setV(valueList);
			pairs.add(pair);
		}
		
		try {
			kvbis.bulkPut(pairs);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		stopTomcat();
		startTomcat();
		
		try {
			for(int i = 0; i < pairs.size(); ++i) {
				PairImpl pair = (PairImpl) pairs.get(i);
				Assert.assertEquals(i, ((TimestampPair) kvbis.read(pair.getK())).getV().getValueList().get(0));
			}
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Test
	public void testDelete() {
		System.out.println("testDelete");
		
		KeyImpl key = new KeyImpl();
		key.setKey("42");
		
		try {
			kvbis.delete(key);
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		stopTomcat();
		startTomcat();
		
		boolean failed = false;
		try {
			kvbis.read(key);
		} catch(Exception e) {
			failed = e instanceof KeyNotFoundException_Exception;
		}
		Assert.assertTrue("Was expecting KeyNotFoundException", failed);
	}
	
	@After
	public void cleanup() {
		stopTomcat();
	}
	
	private void startTomcat() {
		System.out.print("Starting Tomcat... ");
		try {
			new ProcessBuilder("/home/robert/apache-tomcat-7.0.33/bin/startup.sh").start().waitFor();
			Thread.sleep(12000);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		System.out.println("done");
	}
	
	private void stopTomcat() {
		System.out.print("Stopping Tomcat... ");
		try {
			new ProcessBuilder("/home/robert/apache-tomcat-7.0.33/bin/shutdown.sh").start().waitFor();
			Thread.sleep(12000);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		System.out.println("done");
	}

}
