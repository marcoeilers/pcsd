package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import dk.diku.pcsd.assignment1.impl.*;

public class MyClient {
	
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

	public static void main(String[] args) {
		File storeFile = new File("/tmp/store.mmf");
		File logFile = new File("/home/marco/kvstore.log");
		File indexFile = new File("/home/marco/kvindex.ind");
		if (storeFile.exists())
			storeFile.delete();
		
		if (logFile.exists())
			logFile.delete();
		
		if (indexFile.exists())
			indexFile.delete();
		
		
		try {
			//Process proc = Runtime.getRuntime().exec("/home/marco/apache-tomcat-7.0.33/bin/shutdown.sh");
			//waitForProc(proc);
			Process proc = Runtime.getRuntime().exec("/home/marco/apache-tomcat-7.0.33/bin/startup.sh");
			waitForProc(proc);
			
			Thread.sleep(6000);
			KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
			KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
			
			kvbis.init("testData.txt");
		
			KeyImpl key = new KeyImpl();
			key.setKey("15");
			
			System.out.println("Output: " + kvbis.read(key).getValueList().get(0).getValue());
			
			Thread.sleep(7000);
			proc = Runtime.getRuntime().exec("/home/marco/apache-tomcat-7.0.33/bin/shutdown.sh");
			waitForProc(proc);
			Thread.sleep(2000);
			proc = Runtime.getRuntime().exec("/home/marco/apache-tomcat-7.0.33/bin/startup.sh");
			waitForProc(proc);
			Thread.sleep(7000);
			
			KeyValueBaseImplServiceService kvbiss2 = new KeyValueBaseImplServiceService();
			KeyValueBaseImplService kvbis2 = kvbiss2.getKeyValueBaseImplServicePort();
			
			System.out.println("Output: " + kvbis2.read(key).getValueList().get(0).getValue());
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
