package dk.diku.pcsd.exam.experiment;

import java.util.List;

/**
 * Container for all the information that might be interesting about a single
 * run of the experiment.
 * 
 * @author marco
 * 
 */
public class Result {
	private int noOfThreads;
	private int requestsPerThread;
	private List<Long> threadRuntimes;
	private long totalRuntime;

	public int getNoOfThreads() {
		return noOfThreads;
	}

	public void setNoOfThreads(int noOfThreads) {
		this.noOfThreads = noOfThreads;
	}

	public int getRequestsPerThread() {
		return requestsPerThread;
	}

	public void setRequestsPerThread(int requestsPerThread) {
		this.requestsPerThread = requestsPerThread;
	}

	public List<Long> getThreadRuntimes() {
		return threadRuntimes;
	}

	public void setThreadRuntimes(List<Long> threadRuntimes) {
		this.threadRuntimes = threadRuntimes;
	}

	public long getTotalRuntime() {
		return totalRuntime;
	}

	public void setTotalRuntime(long totalRuntime) {
		this.totalRuntime = totalRuntime;
	}

	public Result(int noOfThreads, int requestsPerThread,
			List<Long> threadRuntimes, long totalRuntime) {
		this.noOfThreads = noOfThreads;
		this.requestsPerThread = requestsPerThread;
		this.threadRuntimes = threadRuntimes;
		this.totalRuntime = totalRuntime;
	}

}
