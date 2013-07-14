package dk.diku.pcsd.keyvaluebase.exceptions;

import dk.diku.pcsd.keyvaluebase.interfaces.Key;


public class BeginGreaterThanEndException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private Key<?> begin = null;
	private Key<?> end = null;
	
	public BeginGreaterThanEndException (String message, Key<?> begin, Key<?> end) {
		super(message);
		this.begin = begin;
		this.end = end;
	}

	public BeginGreaterThanEndException (Key<?> begin, Key<?> end) {
		super("The key "+begin+" is greater than the key "+end);
		this.begin = begin;
		this.end = end;
	}

	public BeginGreaterThanEndException () {
		super("The key begin is greater than the key end");
	}

	public Object getBeginKey () {
		return this.begin;
	}

	public Object getEndKey () {
		return this.end;
	}
}
