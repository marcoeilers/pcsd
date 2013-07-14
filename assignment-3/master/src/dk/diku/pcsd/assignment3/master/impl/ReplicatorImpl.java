package dk.diku.pcsd.assignment3.master.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.keyvaluebase.interfaces.Configuration;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseMaster;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Replicator;

/**
 * Extends the Logger. After logging a request, also forwards it to the slaves
 * before releasing the Future.
 */

public class ReplicatorImpl extends LoggerImpl implements Replicator {

	private List<KeyValueBaseSlaveImplService> slaves = new ArrayList<KeyValueBaseSlaveImplService>();
	private Configuration conf;

	private static ReplicatorImpl instance;

	public static ReplicatorImpl getInstance() {
		if (instance == null)
			instance = new ReplicatorImpl();
		return instance;
	}

	private ReplicatorImpl() {
		super();
	}

	@Override
	public Future<?> makeStable(LogRecord record) {
		return logRequest(record);
	}

	@Override
	public void run() {
		execute = true;
		initOutputStream();
		long lastRun = System.currentTimeMillis();

		// remember processed log requests so we can signal them later
		LinkedList<LogQueueEntry<Date>> signalQueue = new LinkedList<LogQueueEntry<Date>>();
		while (execute) {
			LogQueueEntry<Date> next;
			try {
				// wait for log request to appear
				next = logQueue.poll(10L, TimeUnit.SECONDS);

				if (next != null)
					signalQueue.add(next);

				// if we have reached a certain amount of requests
				// or some time has passed
				// then write the log records
				if (signalQueue.size() >= K
						|| (signalQueue.size() > 0 && System
								.currentTimeMillis() - lastRun >= TIMEOUT)) {

					for (LogQueueEntry<Date> entry : signalQueue) {
						try {
							out.writeObject(entry.record);
						} catch (IOException e) {
							throw new RuntimeException(e.getMessage(), e);
						}
					}

					try {
						out.flush();
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage(), e);
					}

					while (signalQueue.size() > 0) {
						LogRecord r = signalQueue.peek().record;
						// do not replicate configure log requests
						if (!r.getMethodName().equals("config")) {

							for (Iterator<KeyValueBaseSlaveImplService> it = slaves
									.iterator(); it.hasNext();) {
								KeyValueBaseSlaveImplService s = it.next();
								try {
									s.logApply(r);
								} catch (javax.xml.ws.WebServiceException e) {
									it.remove();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						signalQueue.poll().future.signalAll(new Date());
					}

					lastRun = System.currentTimeMillis();
				}

				// done by the checkpointer
				if (truncate) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage(), e);
					}

					logFile.delete();
					initOutputStream();
					logSlaves();
					truncate = false;

					// signal the slaves
					LogRecord checkpointRecord = new LogRecord("",
							"checkpoint", new Object[] {});
					for (Iterator<KeyValueBaseSlaveImplService> it = slaves
							.iterator(); it.hasNext();) {
						KeyValueBaseSlaveImplService s = it.next();
						try {
							s.logApply(checkpointRecord);
						} catch (javax.xml.ws.WebServiceException e) {
							it.remove();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (InterruptedException e1) {
				// replicator got interrupted
				execute = false;
			}
		}

		try {
			out.flush();
			out.close();
		} catch (Exception e) {
			// maybe it was flushed and closed before
		}
	}

	public void setSlaves(List<KeyValueBaseSlaveImplService> s,
			Configuration conf) {
		this.slaves = s;
		this.conf = conf;
	}

	private void logSlaves() {
		LogRecord slavesRecord = new LogRecord(KeyValueBaseMaster.class,
				"config", new Object[] { conf });
		makeStable(slavesRecord);
	}

}
