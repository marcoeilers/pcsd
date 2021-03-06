
package dk.diku.pcsd.exam.proxy;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InexistentAccountException", targetNamespace = "http://proxy.exam.pcsd.diku.dk/")
public class InexistentAccountException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InexistentAccountException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public InexistentAccountException_Exception(String message, InexistentAccountException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public InexistentAccountException_Exception(String message, InexistentAccountException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dk.diku.pcsd.exam.proxy.InexistentAccountException
     */
    public InexistentAccountException getFaultInfo() {
        return faultInfo;
    }

}
