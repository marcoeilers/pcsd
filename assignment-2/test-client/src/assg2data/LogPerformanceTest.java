package assg2data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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

public class LogPerformanceTest implements Callable<BenchmarkData> {
	private int insertCount = 2000;
	private int updateCount = 4000;
	private int deleteCount = 1000;

	public static void main(String[] args) {
		int h = Integer.parseInt(args[0]);
		String userdir = System.getProperty("user.home");
		if (!userdir.endsWith(File.separator))
			userdir += File.separator;
		//int h = 1;
		
		PrintWriter bw=null;
		try {
			bw = new PrintWriter(new FileWriter(userdir + "testResults.txt", true));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
		
		try {
			kvbis.init("");
		} catch (FileNotFoundException_Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceInitializingException_Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ExecutorService executor = Executors.newFixedThreadPool(h);

		List<Future<BenchmarkData>> results=null;
		List<Callable<BenchmarkData>> threadList = new ArrayList<Callable<BenchmarkData>>();
		for (int i = 0; i < h; i++)
			threadList.add(new LogPerformanceTest());
		
		List<BenchmarkData> res = new ArrayList<BenchmarkData>();
		
		long before = System.currentTimeMillis();
		try {
			results = executor.invokeAll(threadList);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < h; ++i) {
			BenchmarkData result=null;
			try {
				result = results.get(i).get(); 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res.add(result);
        }
		long after = System.currentTimeMillis();
		
		bw.println("OverallLength: "+(after-before));
		
		for (BenchmarkData result : res){
			bw.println(""+ result.getInsert() + "\t" + result.getUpdate() + "\t"+result.getDelete());
		}
		
	    executor.shutdown();
	    
	    bw.flush();
		

	}

	@Override
	public BenchmarkData call() throws Exception {
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();

		List<KeyImpl> keys = new ArrayList<KeyImpl>();
		HashMap<KeyImpl, ValueListImpl> mappings = new HashMap<KeyImpl, ValueListImpl>();

		Random rnd = new Random();

		long before = System.currentTimeMillis();
		for (int i = 0; i < insertCount; i++) {
			String newKey = "" + rnd.nextInt(10000000);
			KeyImpl key = new KeyImpl();
			key.setKey(newKey);

			ValueListImpl newValList = new ValueListImpl();
			ValueImpl newVal = new ValueImpl();
			String newValStr = "" + rnd.nextInt(1000000);
			newVal.setValue(newValStr);
			newValList.getValueList().add(newVal);

			try {
				kvbis.insert(key, newValList);
				mappings.put(key, newValList);
				keys.add(key);
			} catch (IOException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyAlreadyPresentException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceNotInitializedException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long afterInsert = System.currentTimeMillis();

		for (int i = 0; i < updateCount; i++) {
			KeyImpl key = keys.get(rnd.nextInt(keys.size()));

			ValueListImpl newValList = new ValueListImpl();
			ValueImpl newVal = new ValueImpl();
			String newValStr = "" + rnd.nextInt(1000000);
			newVal.setValue(newValStr);
			newValList.getValueList().add(newVal);

			try {
				kvbis.update(key, newValList);
				mappings.put(key, newValList);
			} catch (IOException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyNotFoundException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceNotInitializedException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long afterUpdate = System.currentTimeMillis();

		for (int i = 0; i < deleteCount; i++) {
			KeyImpl key = keys.get(rnd.nextInt(keys.size()));

			try {
				kvbis.delete(key);
				keys.remove(key);
			} catch (KeyNotFoundException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceNotInitializedException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		long afterDelete = System.currentTimeMillis();

		return new BenchmarkData((afterInsert - before),
				(afterUpdate - afterInsert),
				(afterDelete - afterUpdate));
	}

}
