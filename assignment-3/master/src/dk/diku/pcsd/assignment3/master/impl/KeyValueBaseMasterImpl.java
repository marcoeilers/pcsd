package dk.diku.pcsd.assignment3.master.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import dk.diku.pcsd.assignment3.impl.IndexImpl;
import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.KeyValueBaseReplicaImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplServiceService;
import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyConfiguredException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.Configuration;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseMaster;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseReplica;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

/**
 * Implements the functionality of a master. Pretty much identical to the
 * earlier service, but uses a Replicator instead of a logger to forward writes
 * to the slaves.
 * 
 * Only directly offers write methods, as read functionality is inherited from
 * KeyValueBaseReplica.
 * 
 */
public class KeyValueBaseMasterImpl extends KeyValueBaseReplicaImpl implements
		KeyValueBaseMaster<KeyImpl, ValueListImpl>,
		KeyValueBaseLog<KeyImpl, ValueListImpl> {

	private ReadWriteLock quiesceLock;
	
	private CheckpointerImpl checkpointer;
	private Thread checkpointerThread;

	private ReplicatorImpl replicator;
	private Thread replicatorThread;

	private boolean logging = true, recovering = false;

	private List<KeyValueBaseSlaveImplService> slaves;

	public KeyValueBaseMasterImpl() {
		this(IndexImpl.getInstance(), ReplicatorImpl.getInstance(),
				CheckpointerImpl.getInstance());
	}

	private KeyValueBaseMasterImpl(IndexImpl index, ReplicatorImpl replicator,
			CheckpointerImpl checkpointer) {
		quiesceLock = new ReentrantReadWriteLock(true);
		this.replicator = replicator;
		this.checkpointer = checkpointer;
		this.checkpointer.init(this);

		if (this.replicator.canRecover()) {
			this.index = this.replicator.recoverIndex();
			List<LogRecord> records = this.replicator.recoverLogRecords();

			// if we have not the situation that the first log entry is config
			// then we must declare the service as configured and start the
			// logger and checkpointer
			// because the log is only created after config
			boolean wasConfigured = !(records.size() > 0 && records.get(0)
					.getMethodName().equals("config"));

			// same for initialized
			initialized = !(records.size() > 1 && records.get(1)
					.getMethodName().equals("init"));

			logging = false;
			recovering = true;
			for (LogRecord record : records) {
				try {
					if (record.getMethodName().equals("init")
							|| record.getMethodName().equals("config"))
						record.invoke(this);
					else
						record.invoke(IndexImpl.getInstance());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			logging = true;
			recovering = false;

			if (wasConfigured) {
				replicatorThread = new Thread(this.replicator);
				replicatorThread.start();
				checkpointerThread = new Thread(this.checkpointer);
				checkpointerThread.start();
			}

			// make all slaves recover
			LogRecord recoverRecord = new LogRecord("", "recover",
					new Object[] {});
			for (Iterator<KeyValueBaseSlaveImplService> it = slaves.iterator(); it
					.hasNext();) {
				KeyValueBaseSlaveImplService s = it.next();
				try {
					s.logApply(recoverRecord);
				} catch (javax.xml.ws.WebServiceException e) {
					it.remove();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else
			this.index = index;
	}

	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		quiesceLock.readLock().lock();
		try {
			if (initialized) {
				throw new ServiceAlreadyInitializedException();
			}
			if (initializing) {
				throw new ServiceInitializingException();
			}

			if (logging)
				log("init", serverFilename);

			super.init(serverFilename);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public Pair<TimestampLog, ValueListImpl> read(KeyImpl k)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			return super.read(k);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			if (!initialized) {
				quiesceLock.readLock().unlock();
				throw new ServiceNotInitializedException();
			}
			try {
				if (logging)
					log("insert", k, v);
			} catch (Exception e) {
				e.printStackTrace();
			}
			index.insert(k, v);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			if (!initialized) {
				quiesceLock.readLock().unlock();
				throw new ServiceNotInitializedException();
			}
			if (logging)
				log("update", k, newV);
			index.update(k, newV);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			if (!initialized) {

				quiesceLock.readLock().unlock();
				throw new ServiceNotInitializedException();
			}
			if (logging)
				log("remove", k);
			index.remove(k);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public Pair<TimestampLog, List<ValueListImpl>> scan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			return super.scan(begin, end, p);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public Pair<TimestampLog, List<ValueListImpl>> atomicScan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		try {
			return super.atomicScan(begin, end, p);
		} finally {
			quiesceLock.readLock().unlock();
		}
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		quiesceLock.readLock().lock();
		if (!initialized) {
			quiesceLock.readLock().unlock();
			throw new ServiceNotInitializedException();
		}
		if (logging)
			log("bulkPut", mappings);
		index.bulkPut(mappings);
		quiesceLock.readLock().unlock();
	}

	@Override
	public void quiesce() {
		quiesceLock.writeLock().lock();
	}

	@Override
	public void resume() {
		quiesceLock.writeLock().unlock();
	}

	/**
	 * Logging helper that waits for the log to be written.
	 * 
	 * @param methodName
	 * @param params
	 */
	private void log(String methodName, Object... params) {
		try {
			if (methodName.equals("init")) {
				replicator.makeStable(
						new LogRecord(KeyValueBaseReplica.class, methodName,
								params)).get();
			} else if (methodName.equals("config")) {
				replicator.makeStable(
						new LogRecord(KeyValueBaseMaster.class, methodName,
								params)).get();
			} else {
				replicator.makeStable(
						new LogRecord(IndexImpl.class, methodName, params))
						.get();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void config(Configuration conf)
			throws ServiceAlreadyConfiguredException {
		quiesceLock.readLock().lock();
		if (slaves != null) {
			quiesceLock.readLock().unlock();
			throw new ServiceAlreadyConfiguredException();
		}

		if (logging || recovering) {
			replicatorThread = new Thread(replicator);
			replicatorThread.start();
		}
		checkpointerThread = new Thread(checkpointer);
		checkpointerThread.start();

		if (logging)
			log("config", conf);

		slaves = new ArrayList<KeyValueBaseSlaveImplService>();

		for (String current : conf.getSlaves()) {
			try {
				URL baseUrl;
				baseUrl = dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplServiceService.class
						.getResource(".");
				URL url = new URL(baseUrl, current);
				QName qn = new QName(
						"http://impl.slave.assignment3.pcsd.diku.dk/",
						"KeyValueBaseSlaveImplServiceService");
				KeyValueBaseSlaveImplServiceService service = new KeyValueBaseSlaveImplServiceService(
						url, qn);
				KeyValueBaseSlaveImplService newSlave = service
						.getKeyValueBaseSlaveImplServicePort();

				Map<String, Object> requestContext = ((BindingProvider) newSlave)
						.getRequestContext();
				requestContext.put("com.sun.xml.ws.connect.timeout", 15000);
				requestContext.put("com.sun.xml.ws.request.timeout", 15000);

				slaves.add(newSlave);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		replicator.setSlaves(slaves, conf);
		quiesceLock.readLock().unlock();
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (checkpointerThread!=null && checkpointerThread.isAlive())
			checkpointerThread.interrupt();
		if (replicatorThread!=null && replicatorThread.isAlive())
			replicatorThread.interrupt();
		super.finalize();
	}

}