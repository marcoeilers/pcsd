package dk.diku.pcsd.assignment2.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.FutureLog;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;

public class LogRequest {
	private LogRecord record;
	private FutureLog<Boolean> future;
	
	
	
	public LogRecord getRecord() {
		return record;
	}



	public void setRecord(LogRecord record) {
		this.record = record;
	}



	public FutureLog<Boolean> getFuture() {
		return future;
	}



	public void setFuture(FutureLog<Boolean> future) {
		this.future = future;
	}



	public LogRequest(LogRecord record, FutureLog<Boolean> future) {
		super();
		this.record = record;
		this.future = future;
	}
	
	
}
