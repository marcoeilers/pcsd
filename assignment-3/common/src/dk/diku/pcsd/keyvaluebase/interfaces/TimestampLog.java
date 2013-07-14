package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.Serializable;

/**
 * This class is used in LogRecord, and it represents
 * the LSN of a record.
 */
public class TimestampLog implements Serializable, Comparable<TimestampLog> {
	private static final long serialVersionUID = 1L;
	private Long ind;

	public TimestampLog(Long ind) {
		this();
		this.ind = ind;
	}

	public TimestampLog() {
	}
	
	public boolean before(TimestampLog t) {
		return this.compareTo(t) < 0;
	}
	
	public boolean after(TimestampLog t) {
		return this.compareTo(t) > 0;
	}
	
	@Override
	public int compareTo(TimestampLog t) {
		return this.ind.compareTo(t.ind);
	}
	
	public TimestampLog inc() {
		this.ind++;
		return new TimestampLog(this.ind);
	}
	
	public String toString() {
		return this.ind.toString();
	}
	
	public TimestampLog duplicate(){
		return new TimestampLog(ind);
	}

	public Long getInd() {
		return ind;
	}

	public void setInd(Long ind) {
		this.ind = ind;
	}
	
	
	
}
