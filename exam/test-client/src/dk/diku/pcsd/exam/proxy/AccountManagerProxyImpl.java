
package dk.diku.pcsd.exam.proxy;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "AccountManagerProxyImpl", targetNamespace = "http://proxy.exam.pcsd.diku.dk/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AccountManagerProxyImpl {


    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws InexistentAccountException_Exception
     * @throws NegativeAmountException_Exception
     * @throws InexistentBranchException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "credit", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.Credit")
    @ResponseWrapper(localName = "creditResponse", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.CreditResponse")
    @Action(input = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/creditRequest", output = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/creditResponse", fault = {
        @FaultAction(className = InexistentBranchException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/credit/Fault/InexistentBranchException"),
        @FaultAction(className = InexistentAccountException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/credit/Fault/InexistentAccountException"),
        @FaultAction(className = NegativeAmountException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/credit/Fault/NegativeAmountException")
    })
    public void credit(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        double arg2)
        throws InexistentAccountException_Exception, InexistentBranchException_Exception, NegativeAmountException_Exception
    ;

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws InexistentAccountException_Exception
     * @throws NegativeAmountException_Exception
     * @throws InexistentBranchException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "debit", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.Debit")
    @ResponseWrapper(localName = "debitResponse", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.DebitResponse")
    @Action(input = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/debitRequest", output = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/debitResponse", fault = {
        @FaultAction(className = InexistentBranchException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/debit/Fault/InexistentBranchException"),
        @FaultAction(className = InexistentAccountException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/debit/Fault/InexistentAccountException"),
        @FaultAction(className = NegativeAmountException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/debit/Fault/NegativeAmountException")
    })
    public void debit(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        double arg2)
        throws InexistentAccountException_Exception, InexistentBranchException_Exception, NegativeAmountException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns double
     * @throws InexistentBranchException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "calculateExposure", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.CalculateExposure")
    @ResponseWrapper(localName = "calculateExposureResponse", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.CalculateExposureResponse")
    @Action(input = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/calculateExposureRequest", output = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/calculateExposureResponse", fault = {
        @FaultAction(className = InexistentBranchException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/calculateExposure/Fault/InexistentBranchException")
    })
    public double calculateExposure(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0)
        throws InexistentBranchException_Exception
    ;

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @throws InexistentAccountException_Exception
     * @throws NegativeAmountException_Exception
     * @throws InexistentBranchException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "transfer", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.Transfer")
    @ResponseWrapper(localName = "transferResponse", targetNamespace = "http://proxy.exam.pcsd.diku.dk/", className = "dk.diku.pcsd.exam.proxy.TransferResponse")
    @Action(input = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/transferRequest", output = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/transferResponse", fault = {
        @FaultAction(className = InexistentBranchException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/transfer/Fault/InexistentBranchException"),
        @FaultAction(className = InexistentAccountException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/transfer/Fault/InexistentAccountException"),
        @FaultAction(className = NegativeAmountException_Exception.class, value = "http://proxy.exam.pcsd.diku.dk/AccountManagerProxyImpl/transfer/Fault/NegativeAmountException")
    })
    public void transfer(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        int arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        double arg3)
        throws InexistentAccountException_Exception, InexistentBranchException_Exception, NegativeAmountException_Exception
    ;

}