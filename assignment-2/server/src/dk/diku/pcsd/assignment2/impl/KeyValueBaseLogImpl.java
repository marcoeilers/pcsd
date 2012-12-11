package dk.diku.pcsd.assignment2.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import dk.diku.pcsd.assignment1.impl.IndexImpl;
import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
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
		MasterLock.getExclusiveLock().lock();
	}

	@Override
	public void resume() {
		MasterLock.getExclusiveLock().unlock();
	}
	
	private KeyValueBaseLogImpl(LoggerImpl logger){
		this.logger = logger;
	}
	
	public Logger getLogger(){
		return logger;
	}
	
	public boolean recover(KeyValueBaseImpl kvb){
		if (logger.hasLog()){
			IndexImpl.recreate();
			kvb.setIndex(IndexImpl.getInstance());
			
			List<LogRecord> entries = logger.getLogEntries();
			kvb.setLogging(false);
			for (LogRecord rec : entries){
				try {
					rec.invoke(kvb);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			kvb.setLogging(true);
			return true;
		}else{
			return false;
		}
		
	}
	
}
