package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;

/**
 * This is a class provided to share the code for the slightly modified
 * read calls that the Master and the Slaves have. They both implement
 * this methods together with others specific to them, found respectively
 * in KeyValueBaseMaster and KeyValueBaseSlave.
 */
public interface KeyValueBaseReplica<K extends Key<K>, V extends Value> {
	public void init(String serverFilename) throws ServiceAlreadyInitializedException, ServiceInitializingException, FileNotFoundException;
	public Pair<TimestampLog,V> read(K k) throws KeyNotFoundException, IOException, ServiceNotInitializedException;
	public Pair<TimestampLog,List<V>> scan(K begin, K end, Predicate<V> p) throws IOException, BeginGreaterThanEndException, ServiceNotInitializedException;
	public Pair<TimestampLog,List<V>> atomicScan(K begin, K end, Predicate<V> p) throws IOException, BeginGreaterThanEndException, ServiceNotInitializedException;
}
