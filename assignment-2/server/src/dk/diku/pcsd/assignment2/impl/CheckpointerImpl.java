package dk.diku.pcsd.assignment2.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import dk.diku.pcsd.assignment1.impl.IndexImpl;
import dk.diku.pcsd.assignment1.impl.KeyImpl;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImpl;
import dk.diku.pcsd.assignment1.impl.KeyValueBaseImplService;
import dk.diku.pcsd.assignment1.impl.StoreImpl;
import dk.diku.pcsd.assignment1.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.interfaces.Checkpointer;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseLog;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;

/**
 * Periodically stops all new incoming requests, waits until current requests
 * are processed, then creates a new checkpoint. This means it flushes the
 * store, saves the current state if the index, truncates the log.
 * 
 * This is a singleton and runs in its own thread, which is created once when
 * the service is first initialized.
 * 
 */
public class CheckpointerImpl implements Checkpointer {
	private final long CHECKPOINT_PERIOD = 10L;
	private KeyValueBaseLog<KeyImpl, ValueListImpl> log;
	private LoggerImpl logger;
	private String indexPath;

	private static CheckpointerImpl instance;

	public static CheckpointerImpl createInstance(
			KeyValueBaseLog<KeyImpl, ValueListImpl> log, LoggerImpl logger) {
		if (instance == null) {
			instance = new CheckpointerImpl(log, logger);
			Thread checkpointerThread = new Thread(instance);
			checkpointerThread.start();
		}
		return instance;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(CHECKPOINT_PERIOD);
				System.out.println("Creating a checkpoint");
				log.quiesce();
				StoreImpl.getInstance().flush();
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(indexPath));
				oos.writeObject(IndexImpl.getInstance());
				oos.close();
				logger.truncate();
				if (KeyValueBaseImpl.initialized) {
					logger.logRequest(new LogRecord(
							KeyValueBaseImplService.class, "init",
							new Object[] { "" }));
				}
				log.resume();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private CheckpointerImpl(KeyValueBaseLog<KeyImpl, ValueListImpl> log,
			LoggerImpl logger) {
		this.log = log;
		this.logger = logger;
		indexPath = System.getProperty("user.home");
		if (!indexPath.endsWith(File.separator))
			indexPath += File.separator;
		indexPath += "kvindex.ind";
	}

}
