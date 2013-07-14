
package dk.diku.pcsd.assignment3.slave.impl;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "ServiceInitializingException", targetNamespace = "http://impl.slave.assignment3.pcsd.diku.dk/")
public class ServiceInitializingException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ServiceInitializingException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public ServiceInitializingException_Exception(String message, ServiceInitializingException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public ServiceInitializingException_Exception(String message, ServiceInitializingException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dk.diku.pcsd.assignment3.slave.impl.ServiceInitializingException
     */
    public ServiceInitializingException getFaultInfo() {
        return faultInfo;
    }

}
