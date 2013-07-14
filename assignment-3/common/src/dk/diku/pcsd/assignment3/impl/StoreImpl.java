package dk.diku.pcsd.assignment3.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;

import org.apache.catalina.loader.WebappClassLoader;

import dk.diku.pcsd.keyvaluebase.interfaces.MemoryMappedPinnable;
import dk.diku.pcsd.keyvaluebase.interfaces.Store;

public class StoreImpl implements Store {

	// size of the file
	private static final long MMP_SIZE = 1024L*1024L*2; // 1GB
	
	// the actual file
	private final RandomAccessFile mmpRandomAccessFile;
	
	// abstraction over the file
	private final MemoryMappedPinnable mmp;
	
	// singleton
	private static StoreImpl instance = null;
	
	public static StoreImpl getInstance() {
		if(instance == null)
				instance = new StoreImpl();
		return instance;
	}
		
	private StoreImpl() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		if(!tmpDir.endsWith(File.separator))
			tmpDir += File.separator;
		
		// most bestest way to create a unique ID from the web app path to mark the store file
		int uid = (System.getProperty("catalina.home") + ((WebappClassLoader) getClass().getClassLoader()).getContextName()).hashCode();
		
		// versioning of the store
		String mmpPath = tmpDir + getClass().getPackage().getName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + "store-" + uid + ".mmp";
		File mmpFile = new File(mmpPath);

		try {
			mmpFile.getParentFile().mkdirs();
			mmpRandomAccessFile = new RandomAccessFile(mmpPath, "rw");
			mmpRandomAccessFile.setLength(MMP_SIZE);
			
			// initialize the memory mapped file with either the old file or the newly created one
			mmp = new MemoryMappedPinnable(mmpRandomAccessFile.getChannel(), FileChannel.MapMode.READ_WRITE, 0, MMP_SIZE);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		// close the raf on garbage collection
		mmpRandomAccessFile.close();
		super.finalize();		
	}

	@Override
	public byte[] read(Long position, int length) {
		try {
			byte[] dst = new byte[length];
			mmp.get(dst, position);
			return dst;
		} catch(Exception e) {
			throw new RuntimeException("Position: " + position + ", Length: " + length + ", " + e.getMessage(), e);
		}
	}

	@Override
	public void write(Long position, byte[] value) {
		try {
			mmp.writePinned(value, position);
		} catch(Exception e) {
			throw new RuntimeException("Position: " + position + ", Length: " + value.length + ", " + e.getMessage(), e);
		}
	}
	
	public void flush() {
		mmp.flush();
	}

	@Override
	public Long getSize() {
		return MMP_SIZE;
	}
	
}
