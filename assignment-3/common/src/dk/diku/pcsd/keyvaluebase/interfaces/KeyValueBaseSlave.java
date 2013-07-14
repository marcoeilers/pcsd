package dk.diku.pcsd.keyvaluebase.interfaces;

/**
 * This class extends the Replica interface which declares the read
 * methods, and defines the logApply method. This class is supposed
 * to hold a private object of type KeyValueBase (your assignment 1
 * implementation) and apply all write calls received with the records
 * to it.
 */
public interface KeyValueBaseSlave<K extends Key<K>, V extends Value> extends KeyValueBaseReplica<K,V> {
	public void logApply(LogRecord record);
}
