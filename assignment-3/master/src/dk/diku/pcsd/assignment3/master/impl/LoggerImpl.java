package dk.diku.pcsd.assignment3.master.impl;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;

import dk.diku.pcsd.assignment3.impl.IndexImpl;
import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.SpaceIdent;
import dk.diku.pcsd.keyvaluebase.interfaces.FutureLog;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;
import dk.diku.pcsd.keyvaluebase.interfaces.Logger;

public abstract class LoggerImpl implements Logger {
	
	protected static final int K = 1, TIMEOUT = 0;
		
	protected boolean execute, truncate;
	
	protected LinkedBlockingQueue<LogQueueEntry<Date>> logQueue;
	
	protected File logFile;
	protected ObjectOutputStream out;
		
	protected LoggerImpl() {
		logQueue = new LinkedBlockingQueue<LogQueueEntry<Date>>();
		execute = false; truncate = false;
		
		String tmpDir = System.getProperty("java.io.tmpdir");
		if(!tmpDir.endsWith(File.separator))
			tmpDir += File.separator;
		logFile = new File(tmpDir + getClass().getPackage().getName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + "records.log");
		logFile.getParentFile().mkdirs();
	}
	
	protected void initOutputStream() {
		try {
			if(logFile.exists())
				out = new AppendableObjectOutputStream(new FileOutputStream(logFile, true));
			else {
				out = new ObjectOutputStream(new FileOutputStream(logFile));
			
				// always write the index at the beginning
				out.writeLong(IndexImpl.getInstance().getFileLength());
				out.writeObject(IndexImpl.getInstance().getEmptyList());
				out.writeObject(IndexImpl.getInstance().getMappings());
				out.flush();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		out.close();
		super.finalize();
	}
	
	public void truncate() {
		truncate = true;
	}

	@Override
	public Future<?> logRequest(LogRecord record) {
		FutureLog<Date> future = new FutureLog<Date>();
		logQueue.add(new LogQueueEntry<Date>(record, future));
		return future;
	}
	
	public boolean canRecover() {
		return logFile.exists();
	}
	
	public IndexImpl recoverIndex() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(logFile));
			IndexImpl index = IndexImpl.getInstance();
			index.init(in.readLong(), (SortedSet<SpaceIdent>) in.readObject(), (Map<KeyImpl, SpaceIdent>) in.readObject());
			in.close();
			return index;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public List<LogRecord> recoverLogRecords() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(logFile));
			in.readLong(); in.readObject(); in.readObject(); // read index impl
			List<LogRecord> records = new ArrayList<LogRecord>();
			boolean read = true;
			while(read) {
				try {
					records.add((LogRecord) in.readObject());
				} catch(EOFException e) {
					// phew, this is bad
					read = false;
				}
			}
			in.close();
			return records;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	protected static class LogQueueEntry<T> {
		public LogRecord record;
		public FutureLog<T> future;
		public LogQueueEntry(LogRecord record, FutureLog<T> future) {
			this.record = record; this.future = future;
		}
	}
	
	protected static class AppendableObjectOutputStream extends ObjectOutputStream {
		public AppendableObjectOutputStream(OutputStream os) throws IOException {
			super(os);
		}
		
		@Override
		protected void writeStreamHeader() throws IOException {
			// if we append we don't want the header
			reset();
		}
	}

}
