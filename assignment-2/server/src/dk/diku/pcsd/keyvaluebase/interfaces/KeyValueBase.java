package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;

public interface KeyValueBase<K extends Key<K>, V extends Value>
{
	public void init(String serverFilename) throws ServiceAlreadyInitializedException, ServiceInitializingException, FileNotFoundException;
	public V read(K k) throws KeyNotFoundException, IOException, ServiceNotInitializedException;
	public void insert(K k, V v) throws KeyAlreadyPresentException, IOException, ServiceNotInitializedException;
	public void update(K k, V newV) throws KeyNotFoundException, IOException, ServiceNotInitializedException;
	public void delete(K k) throws KeyNotFoundException, ServiceNotInitializedException;
	public List<V> scan(K begin, K end, Predicate<V> p) throws IOException, BeginGreaterThanEndException, ServiceNotInitializedException;
	public List<V> atomicScan(K begin, K end, Predicate<V> p) throws IOException, BeginGreaterThanEndException, ServiceNotInitializedException;
	public void bulkPut(List<Pair<K,V>> mappings) throws IOException, ServiceNotInitializedException;
}