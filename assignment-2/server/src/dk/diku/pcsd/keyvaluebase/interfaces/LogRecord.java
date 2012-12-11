package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author PCSD - DIKU
 *
 * This class contains all the information necessary
 * for a log request. It provides methods to work
 * with that information as well as to serialize
 * its objects to write them to and from disk.
 * 
 * Note: This class is capable of serializing the parameters
 * even if they do not implement the <i>Serializable</i>
 * interface. To accomplish that the class of the parameter
 * has to implement a constructor that accepts a single
 * <i>String</i>, which is produced by the same class's
 * <i>toString()</i> method.
 */
public class LogRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private Class<?> className;
	private String methodName;
	private int numberParam;
	private Object[] params;

	public LogRecord(Class<?> srcClass, String methodName, Object[] params) {
		this.className = srcClass;
		this.methodName = methodName;
		this.params = params;
		this.numberParam = params.length;
	}
	
	public Class<?> getSrcClass() {
		return this.className;
	}
	
	public String getMethodName() {
		return this.methodName;
	}
	
	public int getNumParams() {
		return this.numberParam;
	}
	
	public Object[] getParams() {
		return this.params;
	}
	
	public Object invoke(Object src) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {

		Class<?>[] params = new Class<?>[this.numberParam];
		for (int i=0; i<this.numberParam; i++)
			params[i] = this.params[i].getClass();
		
		Method m =  this.className.getMethod(methodName, params);
		return m.invoke(src, this.params);
	}	
	
}
