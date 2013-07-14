package dk.diku.pcsd.assignment3.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import dk.diku.pcsd.keyvaluebase.interfaces.Pair;

public class PairImpl extends Pair<KeyImpl,ValueListImpl> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1049640512497692751L;
	@XmlElement
	private KeyImpl k;
	@XmlElement
	private ValueListImpl v;

	public PairImpl(){
	}
	
	public PairImpl(KeyImpl k, ValueListImpl v) {
		this.k = k;
		this.v = v;
	}
	
	@Override
	public KeyImpl getKey() {
		return k;
	}
	
	public void setKey(KeyImpl k){
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
	public int compareTo(Pair<KeyImpl, ValueListImpl> arg0) {
		return k.compareTo(arg0.getKey());
	}

	

}
