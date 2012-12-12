package assg2data;

import java.io.File;
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
	private static  int insertCount = 5000;
	private static int updateCount = 10000;
	private static int deleteCount = 2500;

	public static void main(String[] args) {
		int h;
		if (args.length > 0) {
			h = Integer.parseInt(args[0]);
			
		} else {
			h = 1;
		}
		// int h = 1;
		
		String userdir = System.getProperty("user.home");
		if (!userdir.endsWith(File.separator))
			userdir += File.separator;

		PrintWriter bw = null;
		try {
			bw = new PrintWriter(new FileWriter(userdir + "testResults.txt",
					true));
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

		List<Future<BenchmarkData>> results = null;
		List<Callable<BenchmarkData>> threadList = new ArrayList<Callable<BenchmarkData>>();
		for (int i = 0; i < h; i++)
			threadList.add(new LogPerformanceTest());

		List<BenchmarkData> res = new ArrayList<BenchmarkData>();

		long before = System.nanoTime();
		try {
			results = executor.invokeAll(threadList);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < h; ++i) {
			BenchmarkData result = null;
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
		
		long after = System.nanoTime();
		executor.shutdown();

		
		

		bw.println("Had " + h + " threads.");
		bw.println("Had "+insertCount + " inserts, "+updateCount +" updates, "+deleteCount +" deletes.");
		bw.println("AvgLength: " + (after - before));

		for (BenchmarkData result : res) {
			bw.println("" + result.getInsert() + "\t" + result.getUpdate()
					+ "\t" + result.getDelete());
		}
		bw.flush();

	}

	@Override
	public BenchmarkData call() throws Exception {
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();

		List<KeyImpl> keys = new ArrayList<KeyImpl>();
		HashMap<KeyImpl, ValueListImpl> mappings = new HashMap<KeyImpl, ValueListImpl>();

		Random rnd = new Random();

		long insertTime = 0L;
		long inserts = 0;
		
		for (int i = 0; i < insertCount; i++) {
			String newKey = "" + rnd.nextLong();
			KeyImpl key = new KeyImpl();
			key.setKey(newKey);

			ValueListImpl newValList = new ValueListImpl();
			ValueImpl newVal = new ValueImpl();
			String newValStr = "" + rnd.nextLong();
			newVal.setValue(newValStr);
			newValList.getValueList().add(newVal);

			try {
				long before = System.nanoTime();
				kvbis.insert(key, newValList);
				long after = System.nanoTime();
				inserts++;
				insertTime += (after - before);
				mappings.put(key, newValList);
				keys.add(key);
			} catch (IOException_Exception e) {
			} catch (KeyAlreadyPresentException_Exception e) {
			} catch (ServiceNotInitializedException_Exception e) {
			}
		}
		long updateTime = 0L;
		long updates = 0;

		for (int i = 0; i < updateCount; i++) {
			KeyImpl key = keys.get(rnd.nextInt(keys.size()));

			ValueListImpl newValList = new ValueListImpl();
			ValueImpl newVal = new ValueImpl();
			String newValStr = "" + rnd.nextInt(1000000);
			newVal.setValue(newValStr);
			newValList.getValueList().add(newVal);

			try {
				long before = System.nanoTime();
				kvbis.update(key, newValList);
				long after = System.nanoTime();
				updates++;
				updateTime += (after - before);
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
		long deleteTime = 0L;
		long deletes=0;

		for (int i = 0; i < deleteCount; i++) {
			KeyImpl key = keys.get(rnd.nextInt(keys.size()));

			try {
				long before = System.nanoTime();
				kvbis.delete(key);
				long after = System.nanoTime();
				deleteTime += (after-before);
				deletes++;
				keys.remove(key);
			} catch (KeyNotFoundException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceNotInitializedException_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return new BenchmarkData(insertTime / inserts,
				updateTime/updates, deleteTime / deletes);
	}

}
