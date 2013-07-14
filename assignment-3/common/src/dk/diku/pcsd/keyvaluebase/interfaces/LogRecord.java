package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * @author PCSD - DIKU
 * 
 *         This class contains all the information necessary for a log request.
 *         It provides methods to work with that information as well as to
 *         serialize its objects to write them to and from disk.
 * 
 *         Note: This class is capable of serializing the parameters even if
 *         they do not implement the <i>Serializable</i> interface. To
 *         accomplish that the class of the parameter has to implement a
 *         constructor that accepts a single <i>String</i>, which is produced by
 *         the same class's <i>toString()</i> method.
 */
public class LogRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final TimestampLog lastTimestamp = new TimestampLog(0L);

	@XmlElement
	private String className;
	@XmlElement
	private String methodName;
	@XmlElement
	private int numberParam;
	@XmlElement
	private TimestampLog LSN;
	@XmlElement
	private Object[] params;

	public LogRecord(Class<?> srcClass, String methodName, Object[] params) {
		this(srcClass.getCanonicalName(), methodName, params);
	}

	public LogRecord(String srcClass, String methodName, Object[] params) {
		this();
		this.className = srcClass;
		this.methodName = methodName;
		this.params = params;
		this.numberParam = params.length;
		synchronized (LogRecord.lastTimestamp) {
			this.LSN = LogRecord.lastTimestamp.inc();
		}
	}

	private LogRecord() {
	}

	public String getSrcClass() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public int getNumParams() {
		return this.numberParam;
	}

	public TimestampLog getLSN() {
		return this.LSN;
	}

	public Object[] getParams() {
		return this.params;
	}

	public Object invoke(Object src) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {

		Class<?>[] paramsClass = new Class<?>[this.numberParam];
		for (int i = 0; i < this.numberParam; i++) {
			Class<?> paramClass = this.params[i].getClass();
			if (Arrays.asList(paramClass.getInterfaces()).contains(List.class))
				paramsClass[i] = List.class;
			else
				paramsClass[i] = paramClass;
		}

		Method m = Class.forName(this.className).getMethod(methodName,
				paramsClass);

		lastTimestamp.inc();

		if (lastTimestamp.compareTo(LSN) != 0) {
			lastTimestamp.setInd(LSN.getInd());
//			throw new RuntimeException(
//					"Something has gone horribly wrong! Last timestamp after update from logrecord: "
//							+ lastTimestamp
//							+ ", invoked record with timestamp " + LSN);
		}

		return m.invoke(src, this.params);
	}

}
