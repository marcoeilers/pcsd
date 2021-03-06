
package dk.diku.pcsd.exam.proxy;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "NegativeAmountException", targetNamespace = "http://proxy.exam.pcsd.diku.dk/")
public class NegativeAmountException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private NegativeAmountException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public NegativeAmountException_Exception(String message, NegativeAmountException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public NegativeAmountException_Exception(String message, NegativeAmountException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dk.diku.pcsd.exam.proxy.NegativeAmountException
     */
    public NegativeAmountException getFaultInfo() {
        return faultInfo;
    }

}
