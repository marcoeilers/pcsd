package dk.diku.pcsd.keyvaluebase.interfaces;

import dk.diku.pcsd.keyvaluebase.exceptions.ServiceAlreadyConfiguredException;


/**
 * This class extends all the methods from KeyValueBase
 * that have to be exposed to clients. It sends all read
 * calls using round robin to the replicas (including master)
 * and all write calls to the Master.
 */
public interface KeyValueBaseProxy<K extends Key<K>, V extends Value> extends KeyValueBase<K,V> {
	public void config(Configuration conf) throws ServiceAlreadyConfiguredException;
}
