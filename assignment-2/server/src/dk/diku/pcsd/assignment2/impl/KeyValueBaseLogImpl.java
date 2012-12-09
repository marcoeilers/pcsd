package dk.diku.pcsd.assignment2.impl;

import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;
import dk.diku.pcsd.keyvaluebase.interfaces.Logger;

public class KeyValueBaseLogImpl implements KeyValueBaseLog<KeyImpl,ValueListImpl> {
	private LoggerImpl logger;
	private static KeyValueBaseLogImpl instance;
	
	public static KeyValueBaseLogImpl getInstance(LoggerImpl logger){
		if (instance == null){
			instance = new KeyValueBaseLogImpl(logger);
		}
		return instance;
	}
	
	@Override
	public void quiesce() {
		// TODO		
	}

	@Override
	public void resume() {
		// TODO		
	}
	
	private KeyValueBaseLogImpl(LoggerImpl logger){
		this.logger = logger;
	}
	
	public Logger getLogger(){
		return logger;
	}
	
}
