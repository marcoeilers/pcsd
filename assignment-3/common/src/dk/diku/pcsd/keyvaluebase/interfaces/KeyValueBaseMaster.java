package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.IOException;
import java.util.List;

import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyConfiguredException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;

/**
 * This class extends the Replica interface which declares the read
 * methods, and defines the same write methods found in KeyValueBase
 * to be accessed from the proxies. This class is supposed to hold a
 * private object of type KeyValueBase (your assignment 1 implementation)
 * and apply all write calls received from the proxies to it and
 * forward the write calls to the Slaves through the Replicator.
 */
public interface KeyValueBaseMaster<K extends Key<K>, V extends Value> extends KeyValueBaseReplica<K,V> {
	public void insert(K k, V v) throws KeyAlreadyPresentException, IOException, ServiceNotInitializedException;
	public void update(K k, V newV) throws KeyNotFoundException, IOException, ServiceNotInitializedException;
	public void delete(K k) throws KeyNotFoundException, ServiceNotInitializedException;
	public void bulkPut(List<Pair<K,V>> mappings) throws IOException, ServiceNotInitializedException;
	public void config(Configuration conf) throws ServiceAlreadyConfiguredException;
}
