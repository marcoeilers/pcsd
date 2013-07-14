package dk.diku.pcsd.assignment3.slave.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.SortedSet;
import java.util.regex.Matcher;

import org.apache.catalina.loader.WebappClassLoader;

import dk.diku.pcsd.assignment3.impl.IndexImpl;
import dk.diku.pcsd.assignment3.impl.KeyImpl;
import dk.diku.pcsd.assignment3.impl.KeyValueBaseReplicaImpl;
import dk.diku.pcsd.assignment3.impl.SpaceIdent;
import dk.diku.pcsd.assignment3.impl.StoreImpl;
import dk.diku.pcsd.assignment3.impl.ValueListImpl;
import dk.diku.pcsd.keyvaluebase.interfaces.KeyValueBaseSlave;
import dk.diku.pcsd.keyvaluebase.interfaces.LogRecord;

/**
 * Offers the functionality of the KeyValueBaseReplica. Apart from that, applies
 * log requests sent by the master to this instance as well. Does NOT directly
 * offer the possibility to write to the store.
 * 
 */
public class KeyValueBaseSlaveImpl extends KeyValueBaseReplicaImpl implements
		KeyValueBaseSlave<KeyImpl, ValueListImpl> {

	@Override
	public void logApply(LogRecord record) {
		try {
			if (record.getMethodName().equals("init")) {
				record.invoke(this);
			} else if (record.getMethodName().equals("checkpoint")) {
				// flush index and store
				StoreImpl.getInstance().flush();

				File indexFile = getIndexFile();
				indexFile.getParentFile().mkdirs();

				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(indexFile));
				oos.writeLong(IndexImpl.getInstance().getFileLength());
				oos.writeObject(IndexImpl.getInstance().getEmptyList());
				oos.writeObject(IndexImpl.getInstance().getMappings());
				oos.close();
			} else if (record.getMethodName().equals("recover")) {
				// recover index and store
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(getIndexFile()));
				IndexImpl index = IndexImpl.getInstance();
				index.init(in.readLong(),
						(SortedSet<SpaceIdent>) in.readObject(),
						(Map<KeyImpl, SpaceIdent>) in.readObject());
				in.close();
			} else {
				record.invoke(IndexImpl.getInstance());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getIndexFile() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		if (!tmpDir.endsWith(File.separator))
			tmpDir += File.separator;
		int uid = (System.getProperty("catalina.home") + ((WebappClassLoader) getClass()
				.getClassLoader()).getContextName()).hashCode();
		return new File(tmpDir
				+ getClass()
						.getPackage()
						.getName()
						.replaceAll("\\.",
								Matcher.quoteReplacement(File.separator))
				+ File.separator + uid + ".ind");
	}

}
