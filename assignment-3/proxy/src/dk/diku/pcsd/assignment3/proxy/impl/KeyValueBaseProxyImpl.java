package dk.diku.pcsd.assignment3.proxy.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.TimestampListPair;
import dk.diku.pcsd.assignment3.impl.TimestampPair;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.assignment3.master.impl.FileNotFoundException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyAlreadyPresentException_Exception;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyConfiguredException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceAlreadyInitializedException_Exception;
import dk.diku.pcsd.assignment3.master.impl.ServiceInitializingException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.BeginGreaterThanEndException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplServiceService;
import dk.diku.pcsd.assignment3.slave.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyConfiguredException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.Configuration;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseProxy;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

/**
 * Forwards incoming requests to either the master or a slave. Write requests
 * are always forwarded to the master, read requests are forwarded either to a
 * slave or the master.
 * 
 * Uses a round robin schedule to select the replica for a read request.
 * 
 */
public class KeyValueBaseProxyImpl implements
		KeyValueBaseProxy<KeyImpl, ValueListImpl> {
	private final String replicaError = "Proxy has no working replicas. Either it has not been initialized or all replicas are unreachable.";
	private final String masterError = "Proxy has no working master. Either it has not been initialized or the master is unreachable.";

	private KeyValueBaseMasterImplService master;
	private List<KeyValueBaseSlaveImplService> slaves;

	private int currentReplica = 0;

	// slaves.size() + 1 if the master is available, otherwise + 0
	private int replicas = 0;
	private TimestampLog lastLSN;

	private String replicaLock = "";

	public KeyValueBaseProxyImpl() {
		lastLSN = new TimestampLog(0L);
	}

	@Override
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		if (master != null) {
			try {
				master.init(serverFilename);
			} catch (WebServiceException e) {
				removeMaster();
			} catch (FileNotFoundException_Exception e) {
				throw new FileNotFoundException(e.getMessage());
			} catch (ServiceAlreadyInitializedException_Exception e) {
				throw new ServiceAlreadyInitializedException(e.getMessage());
			} catch (ServiceInitializingException_Exception e) {
				throw new ServiceInitializingException(e.getMessage());
			}
		} else {
			throw new RuntimeException(masterError);
		}
	}

	@Override
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		if (replicas > 0) {
			ValueListImpl result = null;
			Replica r = getReplica();
			try {
				Pair p = r.read(k);
				if (p instanceof TimestampPair) {
					TimestampPair tsp = (TimestampPair) p;
					if (tsp.getKey().before(lastLSN)) {
						return read(k);
					} else {
						lastLSN = tsp.getKey();
						result = tsp.getValue();
					}
				}
			} catch (WebServiceException e) {
				remove(r.getReplica());
				return read(k);
			} catch (IOException_Exception e) {
				throw new IOException(e.getMessage());
			} catch (KeyNotFoundException_Exception e) {
				throw new KeyNotFoundException(e.getMessage(), null);
			} catch (ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException(e.getMessage());
			}
			return result;
		} else {
			throw new RuntimeException(replicaError);
		}
	}

	@Override
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		if (master != null) {
			try {
				master.insert(k, v);
			} catch (WebServiceException e) {
				removeMaster();
			} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
				throw new IOException(e.getMessage());
			} catch (KeyAlreadyPresentException_Exception e) {
				throw new KeyAlreadyPresentException(e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException(e.getMessage());
			}
		} else {
			throw new RuntimeException(masterError);
		}
	}

	@Override
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		if (master != null) {
			try {
				master.update(k, newV);
			} catch (WebServiceException e) {
				removeMaster();
			} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
				throw new IOException(e.getMessage());
			} catch (dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception e) {
				throw new KeyNotFoundException(e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException(e.getMessage());
			}
		} else {
			throw new RuntimeException(masterError);
		}
	}

	@Override
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		if (master != null) {
			try {
				master.delete(k);
			} catch (WebServiceException e) {
				removeMaster();
			} catch (dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception e) {
				throw new KeyNotFoundException(e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException(e.getMessage());
			}
		} else {
			throw new RuntimeException(masterError);
		}
	}

	@Override
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> pred) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		if (replicas > 0) {
			List<ValueListImpl> result = null;
			Replica r = getReplica();
			try {
				Pair p = r.scan(begin, end, pred);
				if (p instanceof TimestampListPair) {
					TimestampListPair tsp = (TimestampListPair) p;
					if (tsp.getKey().before(lastLSN)) {
						return scan(begin, end, pred);
					} else {
						lastLSN = tsp.getKey();
						result = tsp.getValue();
					}
				}
			} catch (WebServiceException e) {
				remove(r.getReplica());
				return atomicScan(begin, end, pred);
			} catch (IOException_Exception e) {
				throw new IOException(e.getMessage());
			} catch (ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException(e.getMessage());
			} catch (BeginGreaterThanEndException_Exception e) {
				throw new BeginGreaterThanEndException(e.getMessage(), null,
						null);
			}
			return result;
		} else {
			throw new RuntimeException(replicaError);
		}
	}

	@Override
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> pred) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		if (replicas > 0) {
			List<ValueListImpl> result = null;
			Replica r = getReplica();
			try {
				Pair p = r.atomicScan(begin, end, pred);
				if (p instanceof TimestampListPair) {
					TimestampListPair tsp = (TimestampListPair) p;
					if (tsp.getKey().before(lastLSN)) {
						return atomicScan(begin, end, pred);
					} else {
						lastLSN = tsp.getKey();
						result = tsp.getValue();
					}
				}
			} catch (WebServiceException e) {
				remove(r.getReplica());
				return atomicScan(begin, end, pred);
			} catch (IOException_Exception e) {
				throw new IOException(e.getMessage());
			} catch (ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException(e.getMessage());
			} catch (BeginGreaterThanEndException_Exception e) {
				throw new BeginGreaterThanEndException(e.getMessage(), null,
						null);
			}
			return result;
		} else {
			throw new RuntimeException(replicaError);
		}
	}

	@Override
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		if (master != null) {
			List list = mappings;
			try {
				master.bulkPut(list);
			} catch (WebServiceException e) {
				removeMaster();
			} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
				throw new IOException(e.getMessage());
			} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException(e.getMessage());
			}
		} else {
			throw new RuntimeException(masterError);
		}
	}

	@Override
	public void config(Configuration conf)
			throws ServiceAlreadyConfiguredException {
		if (slaves != null) {
			throw new ServiceAlreadyConfiguredException();
		}

		try {
			URL baseUrl;
			baseUrl = dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplServiceService.class
					.getResource(".");
			URL url = new URL(baseUrl, conf.getMaster());
			QName qn = new QName(
					"http://impl.master.assignment3.pcsd.diku.dk/",
					"KeyValueBaseMasterImplServiceService");
			KeyValueBaseMasterImplServiceService mservice = new KeyValueBaseMasterImplServiceService(
					url, qn);

			master = mservice.getKeyValueBaseMasterImplServicePort();

			Map<String, Object> requestContext = ((BindingProvider) master)
					.getRequestContext();
			requestContext.put("com.sun.xml.ws.connect.timeout", 15000);
			requestContext.put("com.sun.xml.ws.request.timeout", 15000);

			slaves = new ArrayList<KeyValueBaseSlaveImplService>();

			for (String current : conf.getSlaves()) {
				try {
					baseUrl = dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplServiceService.class
							.getResource(".");
					url = new URL(baseUrl, current);
					qn = new QName(
							"http://impl.slave.assignment3.pcsd.diku.dk/",
							"KeyValueBaseSlaveImplServiceService");
					KeyValueBaseSlaveImplServiceService service = new KeyValueBaseSlaveImplServiceService(
							url, qn);

					KeyValueBaseSlaveImplService newSlave = service
							.getKeyValueBaseSlaveImplServicePort();

					requestContext = ((BindingProvider) newSlave)
							.getRequestContext();
					requestContext.put("com.sun.xml.ws.connect.timeout", 15000);
					requestContext.put("com.sun.xml.ws.request.timeout", 15000);

					slaves.add(newSlave);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}

			replicas = slaves.size() + 1;

			try {
				master.config(conf);
			} catch (ServiceAlreadyConfiguredException_Exception e) {
				// Fuck it, bin ich wohl nicht der erste.
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns either a slave or the master wrapped in a Replica object. A round
	 * robin schedule is used for selecting which replica is wrapped and used.
	 */
	private Replica getReplica() {
		synchronized (replicaLock) {
			if (currentReplica >= slaves.size()) {
				currentReplica = (currentReplica + 1) % replicas;
				return new Replica(master);
			} else {
				int index = currentReplica;
				currentReplica = (currentReplica + 1) % replicas;
				return new Replica(slaves.get(index));
			}
		}
	}

	private void remove(Object o) {
		if (o instanceof KeyValueBaseSlaveImplService)
			removeSlave((KeyValueBaseSlaveImplService) o);
		else
			removeMaster();
	}

	private void removeSlave(KeyValueBaseSlaveImplService slave) {
		int toDelete = -1;
		for (int i = 0; i < slaves.size(); i++) {
			if (slaves.get(i) == slave) {
				toDelete = i;
				break;
			}
		}
		if (toDelete != -1)
			slaves.remove(toDelete);
		replicas = slaves.size() + (master != null ? 1 : 0);
	}

	private void removeMaster() {
		master = null;
		replicas = slaves.size();
	}

}
