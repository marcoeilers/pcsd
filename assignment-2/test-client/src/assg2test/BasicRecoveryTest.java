package assg2test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import dk.diku.pcsd.assignment1.impl.*;

public class BasicRecoveryTest {
	
	private static File storeFile = new File("/tmp/store.mmf");
	private static File logFile = new File("/home/marco/kvstore.log");
	private static File indexFile = new File("/home/marco/kvindex.ind");
	
	private static final String tomcatBinDir = "/home/marco/apache-tomcat-7.0.33/bin/";
	
	private static void waitForProc(Process p){
		BufferedReader reader =
		        new BufferedReader(new InputStreamReader(p.getInputStream()));
		        try {
					while ((reader.readLine()) != null) {}
					p.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
	}

	@Test
	public void testRecovery() {
		
		if (storeFile.exists())
			storeFile.delete();
		
		if (logFile.exists())
			logFile.delete();
		
		if (indexFile.exists())
			indexFile.delete();
		
		
		try {
			//Process proc = Runtime.getRuntime().exec("/home/marco/apache-tomcat-7.0.33/bin/shutdown.sh");
			//waitForProc(proc);
			Process proc = Runtime.getRuntime().exec(tomcatBinDir+"startup.sh");
			waitForProc(proc);
			
			Thread.sleep(6000);
			KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
			KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
			
			kvbis.init("testData.txt");
		
			KeyImpl key = new KeyImpl();
			key.setKey("15");
			
			KeyImpl key16 = new KeyImpl();
			key16.setKey("16");
			
			KeyImpl insertKey = new KeyImpl();
			insertKey.setKey("newKey");
			
			ValueListImpl vl1 = new ValueListImpl();
			ValueImpl v1 = new ValueImpl();
			v1.setValue("aaaaaaa");
			vl1.getValueList().add(v1);
			
			ValueListImpl vl2 = new ValueListImpl();
			ValueImpl v2 = new ValueImpl();
			v2.setValue("aaaaaaaAAAAAAAAA32234AASd");
			vl2.getValueList().add(v2);
			
			kvbis.insert(insertKey, vl1);
			
			kvbis.update(insertKey, vl2);
			
			kvbis.update(key, vl1);
			
			
			
			//System.out.println("Output: " + kvbis.read(key).getValueList().get(0).getValue());
			
			Thread.sleep(7000);
			proc = Runtime.getRuntime().exec(tomcatBinDir+"shutdown.sh");
			waitForProc(proc);
			Thread.sleep(2000);
			proc = Runtime.getRuntime().exec(tomcatBinDir+"startup.sh");
			waitForProc(proc);
			Thread.sleep(7000);
			
			KeyValueBaseImplServiceService kvbiss2 = new KeyValueBaseImplServiceService();
			KeyValueBaseImplService kvbis2 = kvbiss2.getKeyValueBaseImplServicePort();
			
			assertEquals("aaaaaaa", kvbis2.read(key).getValueList().get(0).getValue());
			assertEquals("aaaaaaaAAAAAAAAA32234AASd", kvbis2.read(insertKey).getValueList().get(0).getValue());
			assertEquals("78s", kvbis2.read(key16).getValueList().get(0).getValue());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				Runtime.getRuntime().exec("/home/marco/apache-tomcat-7.0.33/bin/shutdown.sh");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

}
