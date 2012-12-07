package dk.diku.pcsd.assignment1.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

/**
 * This class is used by JAX-WS to create the web service.
 *
 */
@WebService
public class KeyValueBaseImplService extends KeyValueBaseImpl {
	
	public KeyValueBaseImplService() {
		super();
	}
	
	@Override
	@WebMethod
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		super.init(serverFilename);
	}

	@Override
	@WebMethod
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		return super.read(k);
	}

	@Override
	@WebMethod
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		super.insert(k, v);
	}

	@Override
	@WebMethod
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		super.update(k, newV);
	}

	@Override
	@WebMethod
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		super.delete(k);
	}

	@Override
	@WebMethod
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end, Predicate<ValueListImpl> p)
			throws IOException, BeginGreaterThanEndException,
			ServiceNotInitializedException {
		return super.scan(begin, end, p);
	}

	@Override
	@WebMethod
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		return super.atomicScan(begin, end, p);
	}

	@Override
	@WebMethod
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		super.bulkPut(mappings);
	}

}
