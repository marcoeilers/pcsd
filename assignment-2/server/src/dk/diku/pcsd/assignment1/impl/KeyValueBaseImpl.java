package dk.diku.pcsd.assignment1.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import dk.diku.pcsd.assignment2.impl.CheckpointerImpl;
import dk.diku.pcsd.assignment2.impl.KeyValueBaseLogImpl;
import dk.diku.pcsd.assignment2.impl.LoggerImpl;
import dk.diku.pcsd.assignment2.impl.MasterLock;
import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBase;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

/**
 * Implementation of our web service. Subclassed by KeyValueBaseImplService,
 * which is a wrapper that is used as the service's actual interface to make
 * JAX-WS happy. Mostly wraps methods from IndexImpl, but contains logic for
 * initialization and evaluates predicates for scans.
 * 
 */
public class KeyValueBaseImpl implements KeyValueBase<KeyImpl, ValueListImpl> {
	protected IndexImpl index;
	protected LoggerImpl logger;
	public static boolean initialized = false, initializing = false;

	

	public KeyValueBaseImpl() {
		logger = LoggerImpl.getInstance();
		KeyValueBaseLogImpl log = KeyValueBaseLogImpl.getInstance(logger);
		CheckpointerImpl.createInstance(log, logger);
		
		if (!initialized && !initializing){
			log.recover(this);
		}
		index = IndexImpl.getInstance();
	}

	/**
	 * Initializes the store using the specified file, which must have the
	 * format specified in the assignment text. If the provided fileName is
	 * null, just initializes an empty store. Must be used before other
	 * operations on the store are used, can only be used once.
	 * 
	 * The filename has to be specified relative to the computer's temp
	 * directory as specified by java.io.tmpdir.
	 */
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "init", new Object[]{serverFilename})));
			if (initialized)
				throw new ServiceAlreadyInitializedException();
			if (initializing)
				throw new ServiceInitializingException();
			initializing = true;

			if (serverFilename == null) {
				initialized = true;
				initializing = false;
				return;
			}

			String tmpDir = System.getProperty("user.home");
			String filePath;
			if (tmpDir.endsWith(File.separator))
				filePath = tmpDir + serverFilename;
			else
				filePath = tmpDir + File.separator + serverFilename;

			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);

			String current;
			try {
				current = br.readLine();
				String currentKey = null;
				ValueListImpl currentValues = null;
				while (current != null) {

					String[] values = current.split("\\s", 2);

					if (values.length > 1) {
						String key = values[0];
						if (!key.equals(currentKey)) {

							if (currentKey != null) {
								index.insert(new KeyImpl(currentKey),
										currentValues);
							}

							currentKey = key;
							currentValues = new ValueListImpl();
						}

						for (int i = 1; i < values.length; i++) {
							try {
								// try if we have a number
								currentValues.add(new ValueImpl(Integer
										.parseInt(values[i])));
							} catch (NumberFormatException e) {
								// otherwise use the string
								currentValues.add(new ValueImpl(values[i]));
							}
						}
					}

					current = br.readLine();
				}

				if (currentKey != null) {
					index.insert(new KeyImpl(currentKey), currentValues);
				}

				br.close();

				initialized = true;
				initializing = false;
			} catch (IOException e) {
				// Throw a FileNotFoundException instead.
				throw new FileNotFoundException(e.getMessage());
			} catch (KeyAlreadyPresentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "read", new Object[]{k})));
			if (!initialized)
				throw new ServiceNotInitializedException();
			return index.get(k);
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "insert", new Object[]{k,v})));
			if (!initialized)
				throw new ServiceNotInitializedException();
			index.insert(k, v);
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "update", new Object[]{k, newV})));
			if (!initialized)
				throw new ServiceNotInitializedException();
			index.update(k, newV);
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "delete", new Object[]{k})));
			if (!initialized)
				throw new ServiceNotInitializedException();
			index.remove(k);
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "scan", new Object[]{begin, end})));
			if (!initialized)
				throw new ServiceNotInitializedException();
			List<ValueListImpl> allValues = index.scan(begin, end);
			for (Iterator<ValueListImpl> i = allValues.iterator(); i.hasNext();) {
				ValueListImpl current = i.next();
				if (!p.evaluate(current))
					i.remove();
			}
			return allValues;
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "atomicScan", new Object[]{begin, end})));
			if (!initialized)
				throw new ServiceNotInitializedException();
			List<ValueListImpl> allValues = index.atomicScan(begin, end);
			for (Iterator<ValueListImpl> i = allValues.iterator(); i.hasNext();) {
				ValueListImpl current = i.next();
				if (!p.evaluate(current))
					i.remove();
			}
			return allValues;
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		MasterLock.getSharedLock().lock();
		try {
			waitFor(logger.logRequest(new LogRecord(this.getClass(), "bulkPut", new Object[]{mappings})));
			if (!initialized)
				throw new ServiceNotInitializedException();
			index.bulkPut(mappings);
		} finally {
			MasterLock.getSharedLock().unlock();
		}
	}
	
	private void waitFor(Future<?> f){
		
			while (!f.isDone()){
				try {
					f.wait();
				} catch (InterruptedException e) {
				} catch (IllegalMonitorStateException e){
					
				}
			}
		
	}
	
	public void setIndex(IndexImpl i){
		this.index = i;
	}

}
