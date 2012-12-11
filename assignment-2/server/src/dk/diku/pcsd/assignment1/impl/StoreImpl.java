package dk.diku.pcsd.assignment1.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import dk.diku.pcsd.keyvaluebase.interfaces.MemoryMappedPinnable;
import dk.diku.pcsd.keyvaluebase.interfaces.Store;

/**
 * Handles accesses to the MemoryMappedFile. Creates a file "store.mmf" of size
 * MMF_SIZE in the temp folder (specified by the java environment variable
 * java.io.tmpdir). Deletes any existing file with that name.
 * 
 */
public class StoreImpl implements Store {

	// size of the file
	private static final long MMF_SIZE = 1024L* 1024L*100L; // 32GB

	// the actual file
	private final RandomAccessFile mmfRandomAccessFile;

	// abstraction over the file
	private final MemoryMappedPinnable mmf;

	// singleton
	private static StoreImpl instance = null;

	public static StoreImpl getInstance() {
		if (instance == null)
			instance = new StoreImpl(true);
		return instance;
	}
	
	public static StoreImpl recreate(){
		instance = new StoreImpl(false);
		return instance;
	}

	private StoreImpl(boolean overwrite) {
		String tmpDir = System.getProperty("java.io.tmpdir");
		if (!tmpDir.endsWith(File.separator))
			tmpDir += File.separator;

		// versioning of the store
		String mmfPath = tmpDir + "store.mmf";
		File mmfFile = new File(mmfPath);

		try {
			if (overwrite) {
				if (mmfFile.exists())
					mmfFile.delete();

				mmfFile.getParentFile().mkdirs();
				mmfRandomAccessFile = new RandomAccessFile(mmfPath, "rw");
				mmfRandomAccessFile.setLength(MMF_SIZE);

			} else {
				mmfRandomAccessFile = new RandomAccessFile(mmfPath, "rw");
			}
			mmf = new MemoryMappedPinnable(mmfRandomAccessFile.getChannel(),
					FileChannel.MapMode.READ_WRITE, 0, MMF_SIZE);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// close the raf on garbage collection
		mmfRandomAccessFile.close();
		super.finalize();
	}

	@Override
	public byte[] read(Long position, int length) {
		try {
			byte[] dst = new byte[length];
			mmf.get(dst, position);
			return dst;
		} catch (Exception e) {
			throw new RuntimeException("Position: " + position + ", Length: "
					+ length + ", " + e.getMessage(), e);
		}
	}

	@Override
	public void write(Long position, byte[] value) {
		try {
			mmf.writePinned(value, position);
			mmf.put(value, position);
		} catch (Exception e) {
			throw new RuntimeException("Position: " + position + ", Length: "
					+ value.length + ", " + e.getMessage(), e);
		}
	}

	public void flush() {
		mmf.flush();
	}

}
