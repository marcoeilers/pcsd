package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.diku.pcsd.assignment1.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.IOException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplServiceService;
import dk.diku.pcsd.assignment1.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment1.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.assignment1.impl.ValueImpl;

public class LoadReadExperiment {
	
	// don't you ever dare asking
	private static final int[] ZIPFS = new int[] {
		2349, 12, 18, 65, 45, 12, 12, 4718, 733, 616, 1502, 1148, 83, 10425, 
		3332, 29, 121, 31, 28, 288, 293, 12, 106, 5319, 4438, 1597, 1072, 5420, 
		32, 19, 856, 547, 54, 241, 845, 16, 137, 12, 37, 152, 74, 7449, 153, 
		68, 167, 3717, 9315, 101, 27, 305, 4701, 506, 300, 20, 2324, 220, 4921, 
		3943, 8651, 13, 2923, 13, 2607, 25, 18, 128, 17, 7094, 4721, 2205, 63, 
		514, 2771, 3960, 125, 192, 16, 165, 2897, 113, 6736, 13, 284, 185, 3576, 
		22, 63, 385, 66, 438, 23, 20, 365, 816, 199, 933, 168, 64, 26, 1696, 
		8628, 37, 90, 4709, 2145, 516, 204, 278, 55, 165, 16, 8836, 114, 339, 
		7023, 242, 4953, 1303, 42, 738, 625, 4891, 10222, 118, 158, 5550, 2254, 
		4218, 960, 1000, 13, 866, 13, 619, 4416, 2904, 88, 59, 38, 15, 574, 
		154, 15, 254, 1480, 19, 4545, 1842, 1207, 8154, 891, 44, 13, 200, 7289, 
		5833, 2643, 58, 241, 17, 31, 28, 116, 5011, 12, 178, 1582, 4086, 1011, 
		1260, 5523, 2621, 75, 507, 6635, 143, 799, 51, 3314, 87, 2128, 9880, 208, 
		122, 1036, 912, 5278, 23, 157, 12, 66, 1568, 4726, 27, 7296, 1224, 4681, 
		240, 3967, 1758, 139, 7314, 843, 7937, 683, 679, 84, 3156, 21, 1967, 
		1067, 3242, 1749, 3981, 805, 83, 27, 102, 1756, 2789, 7256, 290, 96, 
		5607, 30, 8454, 24, 3331, 358, 1193, 9711, 8184, 9778, 48, 360, 863, 
		9361, 12, 12, 2265, 79, 6818, 1168, 5053, 280, 2523, 2027, 3627, 24, 63 };
	
	static KeyValueBaseImplServiceService kvbiss;
	static KeyValueBaseImplService kvbis;

	public static void main(String[] args) throws FileNotFoundException{
		kvbiss = new KeyValueBaseImplServiceService();
		kvbis = kvbiss.getKeyValueBaseImplServicePort();
		
		try {
			//put in Twitter data set
			kvbis.init("twitter.small");
		} catch (FileNotFoundException_Exception e) {
			e.printStackTrace();
		} catch (ServiceAlreadyInitializedException_Exception e) {
			e.printStackTrace();
		} catch (ServiceInitializingException_Exception e) {
			e.printStackTrace();
		}
		
		//how many threads
		int[] numOfThreads = new int[] { 3, 5, 6 };

		//how many reads per thread
		int n = 250;
		
		//get Zipf keys
		int COUNT = 10856;
		//int COUNT = 61578403; //highest key -11
		float theta = (float) 0.8;
		ZipfGenerator zg = new ZipfGenerator(COUNT, theta);
		Random rnd = new Random();
		
		List<Long> zipfs = new ArrayList<Long>();
		for(int i = 0; i < n; ++i)
//			zipfs.add(zg.calculateZipf(rnd.nextDouble()));
			zipfs.add((long) ZIPFS[i] - 11);
		
		String targetFile = System.getProperty("java.io.tmpdir");
		if(!targetFile.endsWith(File.separator))
			targetFile += File.separator;
		targetFile += "experiment-results.csv";
		File resultFile = new File(targetFile);
		if(resultFile.exists())
			resultFile.delete();
		PrintWriter writer = new PrintWriter(targetFile);
		writer.println("ThreadId\tThreadCount\tKey\tResultCount\tTime");
		
		//iterate over different thread numbers
		for (int j = 0; j < numOfThreads.length; j++){
			//get the current number of threads
			int h = numOfThreads[j];
			
			//make threads and let them read
			ExecutorService executor = Executors.newFixedThreadPool(h);
			
			List<Future<List<ReadResult>>> results;
			try {
				List<Callable<List<ReadResult>>> threadList = new ArrayList<Callable<List<ReadResult>>>();
				for(int i = 0; i < h; i++)
					threadList.add(new ReadThread(zipfs));
				results = executor.invokeAll(threadList);
				
				for (int i = 0; i < h; ++i) {
					Future<List<ReadResult>> result = results.get(i);
					for(ReadResult readResult : result.get()) {
						// writer.println("ThreadId\tThreadCount\tKey\tResultCount\tTime");
						writer.println(i + "\t" + h + "\t" + readResult.key + "\t" + readResult.numResults + "\t" + readResult.time);
					}
					writer.flush();
		        }
			    executor.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		
		}
		
		writer.flush();
		writer.close();
		System.out.println("Done!");
	}
	
	public static class ReadThread implements Callable<List<ReadResult>> {
		List<Long> zipfs;
		ReadThread(List<Long> zipfs) {
			  this.zipfs = zipfs;
		}
		
		public List<ReadResult> call() {
			
			List<ReadResult> readResults = new ArrayList<ReadResult>();
			
			for (int i=0;i<zipfs.size();i++){
				//get key
		        long rank = zipfs.get(i);
		        
		        KeyImpl key = new KeyImpl();
				key.setKey(Long.toString(rank + 11)); //+11 to avoid having a large number of miss hits
				
				//start measurement
				long numResults = 0;
				long startTime = System.nanoTime();
				
				try {
					List<ValueImpl> tempValueList = kvbis.read(key).getValueList();
					numResults = tempValueList.size();
				} //in case of errors do nothing
				catch (IOException_Exception e) {} 
				catch (KeyNotFoundException_Exception e) {} 
				catch (ServiceNotInitializedException_Exception e) {}

		        //stop measurement
				long diff = System.nanoTime() - startTime;
				
				readResults.add(new ReadResult(key.getKey(), diff, numResults));
				
			}

			//write measurement
			return readResults;
			
		}
	} 
	
	private static class ReadResult {
		public String key;
		public long time, numResults;
		public ReadResult(String key, long time, long numResults) {
			this.key = key; this.time = time; this.numResults = numResults;
		}
	}
}
