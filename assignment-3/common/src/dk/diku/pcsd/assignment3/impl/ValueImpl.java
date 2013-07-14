package dk.diku.pcsd.assignment3.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Value;

@SuppressWarnings("serial")
public class ValueImpl implements Value
{
	private Object v;
	
	public ValueImpl() {
		
	}
	
	public ValueImpl(Object o){
		this.v=o;
	}

	public String toString() {
		return v.toString();
	}
	
	public Object getValue(){
		return v;
	}
	
	public void setValue(Object v) {
		this.v = v;
	}
}

