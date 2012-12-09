package dk.diku.pcsd.assignment2.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import dk.diku.pcsd.keyvaluebase.interfaces.FutureLog;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Logger;

public class LoggerImpl implements Logger {
	private static LoggerImpl instance;

	public static LoggerImpl getInstance() {
		if (instance == null) {
			instance = new LoggerImpl();
			Thread loggerThread = new Thread(instance);
			loggerThread.start();
		}
		return instance;
	}

	private BlockingQueue<LogRequest> requests = new LinkedBlockingQueue<LogRequest>();
	private ObjectOutputStream oos;
	private String logPath;

	@Override
	public void run() {
		// log all requests
		while (true) {
			try {
				LogRequest current = requests.poll(Long.MAX_VALUE,
						TimeUnit.DAYS);
				log(current.getRecord());
				current.getFuture().notifyAll();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Future<?> logRequest(LogRecord record) {
		FutureLog<Boolean> f = new FutureLog<Boolean>();
		requests.add(new LogRequest(record, f));
		return f;
	}

	private void log(LogRecord record) throws IOException {
		oos.writeObject(record);
	}

	private LoggerImpl() {
		logPath = System.getProperty("user.home");
		if (!logPath.endsWith(File.separator))
			logPath += File.separator;
		logPath += "kvstore.log";

		try {
			oos = new ObjectOutputStream(new FileOutputStream(logPath, true));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public List<LogRecord> getLogEntries() {
		ObjectInputStream ois;
		List<LogRecord> result = new ArrayList<LogRecord>();

		try {
			ois = new ObjectInputStream(new FileInputStream(logPath));

			while (true)
				try {
					Object current = ois.readObject();
					if (current instanceof LogRecord) {
						result.add((LogRecord) current);
					}
				} catch (Exception e) {
					break;
				} 
			ois.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}
	
	@Override
	protected void finalize() throws Throwable {
		oos.close();
		super.finalize();
	}

}