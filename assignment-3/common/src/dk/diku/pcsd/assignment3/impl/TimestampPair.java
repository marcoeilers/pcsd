package dk.diku.pcsd.assignment3.impl;

import javax.xml.bind.annotation.XmlElement;

import dk.diku.pcsd.keyvaluebase.interfaces.Pair;
import dk.diku.pcsd.keyvaluebase.interfaces.TimestampLog;

public class TimestampPair extends Pair<TimestampLog, ValueListImpl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2297814542470880237L;

	@XmlElement
	private TimestampLog k;
	@XmlElement
	private ValueListImpl v;
	
	public TimestampPair(TimestampLog t, ValueListImpl v) {
		this.k = t;
		this.v = v;
	}
	
	public TimestampPair(){
		super();
	}
	
	@Override
	public TimestampLog getKey() {
		return k;
	}
	
	public void setKey(TimestampLog k){
		this.k=k;
	}
	
	public void setValue(ValueListImpl v){
		this.v=v;
	}
	
	@Override
	public ValueListImpl getValue() {
		return v;
	}

	@Override
	public int compareTo(Pair<TimestampLog, ValueListImpl> o) {
		return k.compareTo(o.getKey());
	}

}
