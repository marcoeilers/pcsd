package dk.diku.pcsd.assignment3.proxy.impl;

import javax.xml.ws.WebServiceException;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.master.impl.KeyValueBaseMasterImplService;
import dk.diku.pcsd.assignment3.slave.impl.BeginGreaterThanEndException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.IOException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.KeyNotFoundException_Exception;
import dk.diku.pcsd.assignment3.slave.impl.KeyValueBaseSlaveImplService;
import dk.diku.pcsd.assignment3.slave.impl.ServiceNotInitializedException_Exception;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

/**
 * Wrapper for a master or slave service, because the generated classes do not
 * implement a common interface. Contains either a master or a slave, offers the
 * same methods as a Replica, throws the Slave's generated exceptions
 * (translates master's exceptions into those).
 */
public class Replica {
	private KeyValueBaseSlaveImplService slave;
	private KeyValueBaseMasterImplService master;
	private boolean hasMaster;

	public Replica(KeyValueBaseMasterImplService m) {
		this.master = m;
		hasMaster = true;
	}

	public Replica(KeyValueBaseSlaveImplService s) {
		this.slave = s;
		hasMaster = false;
	}

	public Pair read(KeyImpl key) throws IOException_Exception,
			KeyNotFoundException_Exception,
			ServiceNotInitializedException_Exception, WebServiceException {
		if (hasMaster) {
			try {
				return master.read(key);
			} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
				throw new IOException_Exception(e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.KeyNotFoundException_Exception e) {
				throw new KeyNotFoundException_Exception(e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException_Exception(
						e.getMessage(), null);
			}
		} else {
			return slave.read(key);
		}
	}

	public Pair scan(KeyImpl from, KeyImpl to, Predicate pred)
			throws BeginGreaterThanEndException_Exception,
			IOException_Exception, ServiceNotInitializedException_Exception,
			WebServiceException {
		if (hasMaster) {
			try {
				return master.scan(from, to, pred);
			} catch (dk.diku.pcsd.assignment3.master.impl.BeginGreaterThanEndException_Exception e) {
				throw new BeginGreaterThanEndException_Exception(
						e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
				throw new IOException_Exception(e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException_Exception(
						e.getMessage(), null);
			}
		} else {
			return slave.scan(from, to, pred);
		}
	}

	public Object getReplica() {
		if (hasMaster)
			return master;
		else
			return slave;
	}

	public Pair atomicScan(KeyImpl from, KeyImpl to, Predicate pred)
			throws BeginGreaterThanEndException_Exception,
			IOException_Exception, ServiceNotInitializedException_Exception,
			WebServiceException {
		if (hasMaster) {
			try {
				return master.atomicScan(from, to, pred);
			} catch (dk.diku.pcsd.assignment3.master.impl.BeginGreaterThanEndException_Exception e) {
				throw new BeginGreaterThanEndException_Exception(
						e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.IOException_Exception e) {
				throw new IOException_Exception(e.getMessage(), null);
			} catch (dk.diku.pcsd.assignment3.master.impl.ServiceNotInitializedException_Exception e) {
				throw new ServiceNotInitializedException_Exception(
						e.getMessage(), null);
			}
		} else {
			return slave.atomicScan(from, to, pred);
		}
	}
}
