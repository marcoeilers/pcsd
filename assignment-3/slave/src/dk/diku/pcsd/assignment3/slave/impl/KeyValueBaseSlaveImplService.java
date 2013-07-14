package dk.diku.pcsd.assignment3.slave.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.exceptions.BeginGreaterThanEndException;
import dk.diku.pcsd.keyvaluebase.exceptions.KeyNotFoundException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyInitializedException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceInitializingException;
import dk.diku.pcsd.keyvaluebase.exceptions.ServiceNotInitializedException;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

@WebService
public class KeyValueBaseSlaveImplService extends KeyValueBaseSlaveImpl {

	@Override
	@WebMethod
	public Pair<TimestampLog, ValueListImpl> read(KeyImpl k)
			throws KeyNotFoundException, IOException,
			ServiceNotInitializedException {
		return super.read(k);
	}
	
	@Override
	@WebMethod
	public Pair<TimestampLog, List<ValueListImpl>> scan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		return super.scan(begin, end, p);
	}
	
	@Override
	@WebMethod
	public Pair<TimestampLog, List<ValueListImpl>> atomicScan(KeyImpl begin,
			KeyImpl end, Predicate<ValueListImpl> p) throws IOException,
			BeginGreaterThanEndException, ServiceNotInitializedException {
		return super.atomicScan(begin, end, p);
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
	public void logApply(LogRecord record) {
		super.logApply(record);
	}
	
}
