package dk.diku.pcsd.assignment3.master.impl;

import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.StoreImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.interfaces.Checkpointer;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;

public class CheckpointerImpl implements Checkpointer {
	
	private static final long INTERVAL = 600000;
	
	private boolean execute;
	
	private KeyValueBaseLog<KeyImpl, ValueListImpl> keyValueBase;
	
	private static CheckpointerImpl instance;
	
	public static CheckpointerImpl getInstance() {
		if(instance == null)
			instance = new CheckpointerImpl();
		return instance;
	}
	
	private CheckpointerImpl() {
		execute = false;
	}
	
	public void init(KeyValueBaseLog<KeyImpl, ValueListImpl> keyValueBase) {
		this.keyValueBase = keyValueBase;
	}

	@Override
	public void run() {
		execute = true;
		while(execute) {
			try {
				Thread.sleep(INTERVAL);
				keyValueBase.quiesce();
				StoreImpl.getInstance().flush();
				ReplicatorImpl.getInstance().truncate();
				keyValueBase.resume();
			} catch (InterruptedException e) {
				// checkpointer got interrupted
				execute = false;
			}
		}
	}

}
