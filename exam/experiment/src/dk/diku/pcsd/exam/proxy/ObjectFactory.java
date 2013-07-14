
package dk.diku.pcsd.exam.proxy;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.diku.pcsd.exam.proxy package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Debit_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "debit");
    private final static QName _CalculateExposureResponse_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "calculateExposureResponse");
    private final static QName _InexistentAccountException_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "InexistentAccountException");
    private final static QName _Credit_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "credit");
    private final static QName _Transfer_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "transfer");
    private final static QName _TransferResponse_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "transferResponse");
    private final static QName _CreditResponse_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "creditResponse");
    private final static QName _NegativeAmountException_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "NegativeAmountException");
    private final static QName _InexistentBranchException_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "InexistentBranchException");
    private final static QName _CalculateExposure_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "calculateExposure");
    private final static QName _DebitResponse_QNAME = new QName("http://proxy.exam.pcsd.diku.dk/", "debitResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.diku.pcsd.exam.proxy
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TransferResponse }
     * 
     */
    public TransferResponse createTransferResponse() {
        return new TransferResponse();
    }

    /**
     * Create an instance of {@link Transfer }
     * 
     */
    public Transfer createTransfer() {
        return new Transfer();
    }

    /**
     * Create an instance of {@link CreditResponse }
     * 
     */
    public CreditResponse createCreditResponse() {
        return new CreditResponse();
    }

    /**
     * Create an instance of {@link CalculateExposure }
     * 
     */
    public CalculateExposure createCalculateExposure() {
        return new CalculateExposure();
    }

    /**
     * Create an instance of {@link DebitResponse }
     * 
     */
    public DebitResponse createDebitResponse() {
        return new DebitResponse();
    }

    /**
     * Create an instance of {@link NegativeAmountException }
     * 
     */
    public NegativeAmountException createNegativeAmountException() {
        return new NegativeAmountException();
    }

    /**
     * Create an instance of {@link InexistentBranchException }
     * 
     */
    public InexistentBranchException createInexistentBranchException() {
        return new InexistentBranchException();
    }

    /**
     * Create an instance of {@link Debit }
     * 
     */
    public Debit createDebit() {
        return new Debit();
    }

    /**
     * Create an instance of {@link CalculateExposureResponse }
     * 
     */
    public CalculateExposureResponse createCalculateExposureResponse() {
        return new CalculateExposureResponse();
    }

    /**
     * Create an instance of {@link Credit }
     * 
     */
    public Credit createCredit() {
        return new Credit();
    }

    /**
     * Create an instance of {@link InexistentAccountException }
     * 
     */
    public InexistentAccountException createInexistentAccountException() {
        return new InexistentAccountException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Debit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "debit")
    public JAXBElement<Debit> createDebit(Debit value) {
        return new JAXBElement<Debit>(_Debit_QNAME, Debit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateExposureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "calculateExposureResponse")
    public JAXBElement<CalculateExposureResponse> createCalculateExposureResponse(CalculateExposureResponse value) {
        return new JAXBElement<CalculateExposureResponse>(_CalculateExposureResponse_QNAME, CalculateExposureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InexistentAccountException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "InexistentAccountException")
    public JAXBElement<InexistentAccountException> createInexistentAccountException(InexistentAccountException value) {
        return new JAXBElement<InexistentAccountException>(_InexistentAccountException_QNAME, InexistentAccountException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Credit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "credit")
    public JAXBElement<Credit> createCredit(Credit value) {
        return new JAXBElement<Credit>(_Credit_QNAME, Credit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Transfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "transfer")
    public JAXBElement<Transfer> createTransfer(Transfer value) {
        return new JAXBElement<Transfer>(_Transfer_QNAME, Transfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "transferResponse")
    public JAXBElement<TransferResponse> createTransferResponse(TransferResponse value) {
        return new JAXBElement<TransferResponse>(_TransferResponse_QNAME, TransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreditResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "creditResponse")
    public JAXBElement<CreditResponse> createCreditResponse(CreditResponse value) {
        return new JAXBElement<CreditResponse>(_CreditResponse_QNAME, CreditResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NegativeAmountException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "NegativeAmountException")
    public JAXBElement<NegativeAmountException> createNegativeAmountException(NegativeAmountException value) {
        return new JAXBElement<NegativeAmountException>(_NegativeAmountException_QNAME, NegativeAmountException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InexistentBranchException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "InexistentBranchException")
    public JAXBElement<InexistentBranchException> createInexistentBranchException(InexistentBranchException value) {
        return new JAXBElement<InexistentBranchException>(_InexistentBranchException_QNAME, InexistentBranchException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateExposure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "calculateExposure")
    public JAXBElement<CalculateExposure> createCalculateExposure(CalculateExposure value) {
        return new JAXBElement<CalculateExposure>(_CalculateExposure_QNAME, CalculateExposure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DebitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://proxy.exam.pcsd.diku.dk/", name = "debitResponse")
    public JAXBElement<DebitResponse> createDebitResponse(DebitResponse value) {
        return new JAXBElement<DebitResponse>(_DebitResponse_QNAME, DebitResponse.class, null, value);
    }

}
