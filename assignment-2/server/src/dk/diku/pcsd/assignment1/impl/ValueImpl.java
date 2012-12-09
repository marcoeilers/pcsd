package dk.diku.pcsd.assignment1.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Value;

/**
 * Wrapper for a value that is to be stored in the store. Values can either be
 * strings or any primitive value that exists in Java.
 */

public class ValueImpl implements Value {
	private static final long serialVersionUID = -8073246226564164920L;
	private Object v;

	public ValueImpl() {

	}

	public ValueImpl(Object o) {
		this.v = o;
	}

	public String toString() {
		return v.toString();
	}

	public Object getValue() {
		return v;
	}

	public void setValue(Object v) {
		this.v = v;
	}
}
