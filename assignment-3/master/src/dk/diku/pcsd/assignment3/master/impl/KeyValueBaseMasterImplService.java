package dk.diku.pcsd.assignment3.master.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyAlreadyPresentException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyConfiguredException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.Configuration;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

@WebService
public class KeyValueBaseMasterImplService extends KeyValueBaseMasterImpl{
	
	public KeyValueBaseMasterImplService() {
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
	public Pair<TimestampLog, ValueListImpl> read(KeyImpl k) throws KeyNotFoundException,
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
	public Pair<TimestampLog, List<ValueListImpl>> scan(KeyImpl begin, KeyImpl end, Predicate<ValueListImpl> p)
			throws IOException, BeginGreaterThanEndException,
			ServiceNotInitializedException {
		return super.scan(begin, end, p);
	}

	@Override
	@WebMethod
	public Pair<TimestampLog, List<ValueListImpl>> atomicScan(KeyImpl begin, KeyImpl end,
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
	
	@Override
	@WebMethod
	public void config(Configuration conf)
			throws ServiceAlreadyConfiguredException {
		super.config(conf);
	}

}
