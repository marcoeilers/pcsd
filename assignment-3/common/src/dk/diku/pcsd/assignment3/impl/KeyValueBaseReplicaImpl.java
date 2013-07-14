package dk.diku.pcsd.assignment3.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseReplica;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

/**
 * Offers methods for reading from the database, i.e. the functionality offered
 * by both master and slaves. Is therefore extended by both master and slaves,
 * and never used directly.
 */
public abstract class KeyValueBaseReplicaImpl implements
		KeyValueBaseReplica<KeyImpl, ValueListImpl> {

	protected IndexImpl index;

	protected boolean initialized = false, initializing = false;

	protected KeyValueBaseReplicaImpl() {
		index = IndexImpl.getInstance();
	}

	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		if (initialized) {
			throw new ServiceAlreadyInitializedException();
		}
		if (initializing) {
			throw new ServiceInitializingException();
		}

		initializing = true;

		if ("".equals(serverFilename)) {
			initialized = true;
			initializing = false;
			return;
		}

		String tmpDir = System.getProperty("java.io.tmpdir");
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
							index.insert(new KeyImpl(currentKey), currentValues);
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

	}

	@Override
	public Pair<TimestampLog, ValueListImpl> read(KeyImpl k)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		if (!initialized) {
			throw new ServiceNotInitializedException();
		}
		TimestampLog lsn = LogRecord.lastTimestamp.duplicate();
		ValueListImpl v = index.get(k);
		return new TimestampPair(lsn, v);
	}

	@Override
	public Pair<TimestampLog, List<ValueListImpl>> scan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		if (!initialized) {
			throw new ServiceNotInitializedException();
		}
		TimestampLog lsn = LogRecord.lastTimestamp.duplicate();
		List<ValueListImpl> allValues = index.scan(begin, end);
		for (Iterator<ValueListImpl> i = allValues.iterator(); i.hasNext();) {
			ValueListImpl current = i.next();
			if (!p.evaluate(current))
				i.remove();
		}
		return new TimestampListPair(lsn, allValues);
	}

	@Override
	public Pair<TimestampLog, List<ValueListImpl>> atomicScan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		if (!initialized) {
			throw new ServiceNotInitializedException();
		}
		TimestampLog lsn = LogRecord.lastTimestamp.duplicate();
		List<ValueListImpl> allValues = index.atomicScan(begin, end);
		for (Iterator<ValueListImpl> i = allValues.iterator(); i.hasNext();) {
			ValueListImpl current = i.next();
			if (!p.evaluate(current))
				i.remove();
		}
		return new TimestampListPair(lsn, allValues);
	}

}
