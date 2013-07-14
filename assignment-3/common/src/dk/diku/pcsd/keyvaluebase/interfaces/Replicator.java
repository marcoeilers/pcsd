package dk.diku.pcsd.keyvaluebase.interfaces;

import java.util.concurrent.Future;

/**
 * This class superseeds the Logger from your
 * second assignment in the KeyValueBaseMaster.
 * It is suposed to take the responsibility of
 * the previous logger with the added function
 * of forwarding all write calls to all the Slaves.
 */
public interface Replicator extends Runnable {
	public Future<?> makeStable(LogRecord record);
}
