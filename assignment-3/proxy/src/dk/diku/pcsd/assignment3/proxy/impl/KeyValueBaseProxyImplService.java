package dk.diku.pcsd.assignment3.proxy.impl;

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

@WebService
public class KeyValueBaseProxyImplService extends KeyValueBaseProxyImpl {

	@Override
	@WebMethod
	public void init(String serverFilename)
			throws ServiceAlreadyInitializedException,
			ServiceInitializingException, FileNotFoundException {
		// TODO Auto-generated method stub
		super.init(serverFilename);
	}
	
	@Override
	@WebMethod
	public ValueListImpl read(KeyImpl k) throws KeyNotFoundException,
			IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return super.read(k);
	}
	
	@Override
	@WebMethod
	public void insert(KeyImpl k, ValueListImpl v)
			throws KeyAlreadyPresentException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		super.insert(k, v);
	}
	
	@Override
	@WebMethod
	public void update(KeyImpl k, ValueListImpl newV)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		super.update(k, newV);
	}
	
	@Override
	@WebMethod
	public void delete(KeyImpl k) throws KeyNotFoundException,
			ServiceNotInitializedException {
		// TODO Auto-generated method stub
		super.delete(k);
	}
	
	@Override
	@WebMethod
	public List<ValueListImpl> scan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return super.scan(begin, end, p);
	}
	
	@Override
	@WebMethod
	public List<ValueListImpl> atomicScan(KeyImpl begin, KeyImpl end,
			Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		return super.atomicScan(begin, end, p);
	}
	
	@Override
	@WebMethod
	public void bulkPut(List<Pair<KeyImpl, ValueListImpl>> mappings)
			throws IOException, ServiceNotInitializedException {
		// TODO Auto-generated method stub
		super.bulkPut(mappings);
	}
	
	@Override
	@WebMethod
	public void config(Configuration conf)
			throws ServiceAlreadyConfiguredException {
		// TODO Auto-generated method stub
		super.config(conf);
	}
}
