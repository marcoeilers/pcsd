package dk.diku.pcsd.keyvaluebase.exceptions;

import dk.diku.pcsd.keyvaluebase.interfaces.Key;


public class KeyNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private Key<?> key = null;
	
	public KeyNotFoundException (String message, Key<?> k) {
		super(message);
		key = k;
	}

	public KeyNotFoundException (Key<?> k) {
		super("The key "+k+" is not present");
		key = k;
	}

	public KeyNotFoundException () {
		super("The key is not present");
	}

	public Object getKey () {
		return key;
	}
}