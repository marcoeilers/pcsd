package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is intended to keep the WSDL
 * file locations of all the internal RPC
 * services. Those locations are used by the
 * proxies and the master to find all the
 * replicas.
 */
public class Configuration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8025195159733077837L;
	
	private String master = "";
	private List<String> slaves = new ArrayList<String>();
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public List<String> getSlaves() {
		return slaves;
	}
	public void setSlaves(List<String> slaves) {
		this.slaves = slaves;
	}
	
	
}
