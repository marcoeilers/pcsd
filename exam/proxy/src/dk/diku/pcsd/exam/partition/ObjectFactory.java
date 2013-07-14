
package dk.diku.pcsd.exam.partition;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.diku.pcsd.exam.partition package. 
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

    private final static QName _CalculateExposure_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "calculateExposure");
    private final static QName _DebitResponse_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "debitResponse");
    private final static QName _SetInitialized_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "setInitialized");
    private final static QName _TransferResponse_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "transferResponse");
    private final static QName _CreditResponse_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "creditResponse");
    private final static QName _SetInitializedResponse_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "setInitializedResponse");
    private final static QName _Credit_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "credit");
    private final static QName _InexistentAccountException_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "InexistentAccountException");
    private final static QName _Debit_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "debit");
    private final static QName _CalculateExposureResponse_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "calculateExposureResponse");
    private final static QName _StartInitializingResponse_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "startInitializingResponse");
    private final static QName _SetBalancesResponse_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "setBalancesResponse");
    private final static QName _InexistentBranchException_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "InexistentBranchException");
    private final static QName _Transfer_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "transfer");
    private final static QName _StartInitializing_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "startInitializing");
    private final static QName _SetBalances_QNAME = new QName("http://partition.exam.pcsd.diku.dk/", "setBalances");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.diku.pcsd.exam.partition
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Transfer }
     * 
     */
    public Transfer createTransfer() {
        return new Transfer();
    }

    /**
     * Create an instance of {@link StartInitializing }
     * 
     */
    public StartInitializing createStartInitializing() {
        return new StartInitializing();
    }

    /**
     * Create an instance of {@link StartInitializingResponse }
     * 
     */
    public StartInitializingResponse createStartInitializingResponse() {
        return new StartInitializingResponse();
    }

    /**
     * Create an instance of {@link SetBalancesResponse }
     * 
     */
    public SetBalancesResponse createSetBalancesResponse() {
        return new SetBalancesResponse();
    }

    /**
     * Create an instance of {@link InexistentBranchException }
     * 
     */
    public InexistentBranchException createInexistentBranchException() {
        return new InexistentBranchException();
    }

    /**
     * Create an instance of {@link SetBalances }
     * 
     */
    public SetBalances createSetBalances() {
        return new SetBalances();
    }

    /**
     * Create an instance of {@link TransferResponse }
     * 
     */
    public TransferResponse createTransferResponse() {
        return new TransferResponse();
    }

    /**
     * Create an instance of {@link CreditResponse }
     * 
     */
    public CreditResponse createCreditResponse() {
        return new CreditResponse();
    }

    /**
     * Create an instance of {@link SetInitializedResponse }
     * 
     */
    public SetInitializedResponse createSetInitializedResponse() {
        return new SetInitializedResponse();
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
     * Create an instance of {@link SetInitialized }
     * 
     */
    public SetInitialized createSetInitialized() {
        return new SetInitialized();
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
     * Create an instance of {@link Account }
     * 
     */
    public Account createAccount() {
        return new Account();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateExposure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "calculateExposure")
    public JAXBElement<CalculateExposure> createCalculateExposure(CalculateExposure value) {
        return new JAXBElement<CalculateExposure>(_CalculateExposure_QNAME, CalculateExposure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DebitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "debitResponse")
    public JAXBElement<DebitResponse> createDebitResponse(DebitResponse value) {
        return new JAXBElement<DebitResponse>(_DebitResponse_QNAME, DebitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetInitialized }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "setInitialized")
    public JAXBElement<SetInitialized> createSetInitialized(SetInitialized value) {
        return new JAXBElement<SetInitialized>(_SetInitialized_QNAME, SetInitialized.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "transferResponse")
    public JAXBElement<TransferResponse> createTransferResponse(TransferResponse value) {
        return new JAXBElement<TransferResponse>(_TransferResponse_QNAME, TransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreditResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "creditResponse")
    public JAXBElement<CreditResponse> createCreditResponse(CreditResponse value) {
        return new JAXBElement<CreditResponse>(_CreditResponse_QNAME, CreditResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetInitializedResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "setInitializedResponse")
    public JAXBElement<SetInitializedResponse> createSetInitializedResponse(SetInitializedResponse value) {
        return new JAXBElement<SetInitializedResponse>(_SetInitializedResponse_QNAME, SetInitializedResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Credit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "credit")
    public JAXBElement<Credit> createCredit(Credit value) {
        return new JAXBElement<Credit>(_Credit_QNAME, Credit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InexistentAccountException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "InexistentAccountException")
    public JAXBElement<InexistentAccountException> createInexistentAccountException(InexistentAccountException value) {
        return new JAXBElement<InexistentAccountException>(_InexistentAccountException_QNAME, InexistentAccountException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Debit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "debit")
    public JAXBElement<Debit> createDebit(Debit value) {
        return new JAXBElement<Debit>(_Debit_QNAME, Debit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateExposureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "calculateExposureResponse")
    public JAXBElement<CalculateExposureResponse> createCalculateExposureResponse(CalculateExposureResponse value) {
        return new JAXBElement<CalculateExposureResponse>(_CalculateExposureResponse_QNAME, CalculateExposureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartInitializingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "startInitializingResponse")
    public JAXBElement<StartInitializingResponse> createStartInitializingResponse(StartInitializingResponse value) {
        return new JAXBElement<StartInitializingResponse>(_StartInitializingResponse_QNAME, StartInitializingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetBalancesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "setBalancesResponse")
    public JAXBElement<SetBalancesResponse> createSetBalancesResponse(SetBalancesResponse value) {
        return new JAXBElement<SetBalancesResponse>(_SetBalancesResponse_QNAME, SetBalancesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InexistentBranchException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "InexistentBranchException")
    public JAXBElement<InexistentBranchException> createInexistentBranchException(InexistentBranchException value) {
        return new JAXBElement<InexistentBranchException>(_InexistentBranchException_QNAME, InexistentBranchException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Transfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "transfer")
    public JAXBElement<Transfer> createTransfer(Transfer value) {
        return new JAXBElement<Transfer>(_Transfer_QNAME, Transfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartInitializing }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "startInitializing")
    public JAXBElement<StartInitializing> createStartInitializing(StartInitializing value) {
        return new JAXBElement<StartInitializing>(_StartInitializing_QNAME, StartInitializing.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetBalances }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://partition.exam.pcsd.diku.dk/", name = "setBalances")
    public JAXBElement<SetBalances> createSetBalances(SetBalances value) {
        return new JAXBElement<SetBalances>(_SetBalances_QNAME, SetBalances.class, null, value);
    }

}
