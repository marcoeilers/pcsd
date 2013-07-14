package test;

import dk.diku.pcsd.assignment3.master.impl.Configuration;
import dk.diku.pcsd.assignment3.master.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyImpl;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyConfiguredException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ValueImpl;
import dk.diku.pcsd.assignment3.master.impl.ValueListImpl;
import dk.diku.pcsd.assignment3.slave.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplServiceService;
import dk.diku.pcsd.assignment3.slave.impl.Pair;
import dk.diku.pcsd.assignment3.slave.impl.TimestampPair;

public class TestReplication {
	public static void main(String[] args){
		KeyValueBaseMasterImplServiceService kvbiss = new KeyValueBaseMasterImplServiceService();
		KeyValueBaseMasterImplService kvbis = kvbiss.getKeyValueBaseMasterImplServicePort();
		
		KeyValueBaseSlaveImplServiceService slave = new KeyValueBaseSlaveImplServiceService();
		KeyValueBaseSlaveImplService slav = slave.getKeyValueBaseSlaveImplServicePort();
		
		
		
		KeyImpl key = new KeyImpl();
		dk.diku.pcsd.assignment3.slave.impl.KeyImpl keyS = new dk.diku.pcsd.assignment3.slave.impl.KeyImpl();
	
		
		ValueListImpl vl = new ValueListImpl();
		ValueImpl v = new ValueImpl();
		v.setValue("result");
		vl.getValueList().add(v);
		
		key.setKey("key");
		keyS.setKey("key");
		
		try {
			Configuration c = new Configuration();
			c.getSlaves().add("http://localhost:8080/slave/keyvaluebaseslave?wsdl");
			kvbis.config(c);
			
			kvbis.init("");
			
			kvbis.insert(key, vl);
			
			Thread.sleep(2000);
			Pair result = slav.read(keyS);
			if (result instanceof TimestampPair){
				TimestampPair tsp = (TimestampPair) result;
				System.out.println(tsp.getV().getValueList().get(0).getValue());
			}
		} catch (FileNotFoundException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyAlreadyPresentException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (dk.diku.pcsd.assignment3.slave.impl.IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyNotFoundException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (dk.diku.pcsd.assignment3.slave.impl.ServiceNotInitializedException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceAlreadyConfiguredException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
