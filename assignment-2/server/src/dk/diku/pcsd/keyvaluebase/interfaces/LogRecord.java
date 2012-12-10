package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
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
			// FIXME incredibly ugly
			params[i] = this.params[i]!=null?  this.params[i].getClass() : String.class;
		
		Method m =  this.className.getMethod(methodName, params);
		return m.invoke(src, this.params);
	}
	
	public void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		
		for (int i=0; i<this.numberParam; i++) {
			Object obj = this.params[i];
			if (obj instanceof Serializable)
				out.writeObject(obj);
			else
				out.writeObject(new SerializablePair(obj));
		}
	}
	
	public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		
		this.params = new Object[this.numberParam];
		Object obj = null;
		for (int i=0; i<this.numberParam; i++) {
			obj = in.readObject();
			if (obj instanceof SerializablePair)
				obj = ((SerializablePair)obj).getObject();
			this.params[i] = obj;
		}
	}
	
	private class SerializablePair implements Serializable {
		
		private static final long serialVersionUID = 1L;
		private Class<?> className;
		private String object;
		
		public SerializablePair(Object object) throws NotSerializableException {
			this.className = object.getClass();
			this.object = object.toString();
			if (this.getStringConstructor() == null)
				throw new NotSerializableException();
		}
		
		public Object getObject() {
			Constructor<?> constr = this.getStringConstructor();
			Object ret = null;
			try {
				ret = constr.newInstance(this.object);
			} catch (Exception e) {}
			
			return ret;
		}
		
		private Constructor<?> getStringConstructor() {
			try {
				return this.className.getConstructor(String.class);
			} catch (Exception e) {
				return null;
			}
		}
		
	}
	
}
