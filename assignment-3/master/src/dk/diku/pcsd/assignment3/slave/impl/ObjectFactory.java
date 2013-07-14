
package dk.diku.pcsd.assignment3.slave.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dk.diku.pcsd.assignment3.slave.impl package. 
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

    private final static QName _LogApplyResponse_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "logApplyResponse");
    private final static QName _FileNotFoundException_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "FileNotFoundException");
    private final static QName _Read_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "read");
    private final static QName _LogApply_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "logApply");
    private final static QName _ReadResponse_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "readResponse");
    private final static QName _Scan_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "scan");
    private final static QName _ScanResponse_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "scanResponse");
    private final static QName _IOException_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "IOException");
    private final static QName _ServiceNotInitializedException_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "ServiceNotInitializedException");
    private final static QName _KeyNotFoundException_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "KeyNotFoundException");
    private final static QName _AtomicScan_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "atomicScan");
    private final static QName _AtomicScanResponse_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "atomicScanResponse");
    private final static QName _Init_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "init");
    private final static QName _ServiceInitializingException_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "ServiceInitializingException");
    private final static QName _ServiceAlreadyInitializedException_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "ServiceAlreadyInitializedException");
    private final static QName _BeginGreaterThanEndException_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "BeginGreaterThanEndException");
    private final static QName _InitResponse_QNAME = new QName("http://impl.slave.assignment3.pcsd.diku.dk/", "initResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dk.diku.pcsd.assignment3.slave.impl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BeginGreaterThanEndException }
     * 
     */
    public BeginGreaterThanEndException createBeginGreaterThanEndException() {
        return new BeginGreaterThanEndException();
    }

    /**
     * Create an instance of {@link Scan }
     * 
     */
    public Scan createScan() {
        return new Scan();
    }

    /**
     * Create an instance of {@link AtomicScan }
     * 
     */
    public AtomicScan createAtomicScan() {
        return new AtomicScan();
    }

    /**
     * Create an instance of {@link AtomicScanResponse }
     * 
     */
    public AtomicScanResponse createAtomicScanResponse() {
        return new AtomicScanResponse();
    }

    /**
     * Create an instance of {@link ServiceAlreadyInitializedException }
     * 
     */
    public ServiceAlreadyInitializedException createServiceAlreadyInitializedException() {
        return new ServiceAlreadyInitializedException();
    }

    /**
     * Create an instance of {@link ScanResponse }
     * 
     */
    public ScanResponse createScanResponse() {
        return new ScanResponse();
    }

    /**
     * Create an instance of {@link ServiceInitializingException }
     * 
     */
    public ServiceInitializingException createServiceInitializingException() {
        return new ServiceInitializingException();
    }

    /**
     * Create an instance of {@link Init }
     * 
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link InitResponse }
     * 
     */
    public InitResponse createInitResponse() {
        return new InitResponse();
    }

    /**
     * Create an instance of {@link Read }
     * 
     */
    public Read createRead() {
        return new Read();
    }

    /**
     * Create an instance of {@link ServiceNotInitializedException }
     * 
     */
    public ServiceNotInitializedException createServiceNotInitializedException() {
        return new ServiceNotInitializedException();
    }

    /**
     * Create an instance of {@link KeyNotFoundException }
     * 
     */
    public KeyNotFoundException createKeyNotFoundException() {
        return new KeyNotFoundException();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link LogApplyResponse }
     * 
     */
    public LogApplyResponse createLogApplyResponse() {
        return new LogApplyResponse();
    }

    /**
     * Create an instance of {@link FileNotFoundException }
     * 
     */
    public FileNotFoundException createFileNotFoundException() {
        return new FileNotFoundException();
    }

    /**
     * Create an instance of {@link ReadResponse }
     * 
     */
    public ReadResponse createReadResponse() {
        return new ReadResponse();
    }

    /**
     * Create an instance of {@link LogApply }
     * 
     */
    public LogApply createLogApply() {
        return new LogApply();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogApplyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "logApplyResponse")
    public JAXBElement<LogApplyResponse> createLogApplyResponse(LogApplyResponse value) {
        return new JAXBElement<LogApplyResponse>(_LogApplyResponse_QNAME, LogApplyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "FileNotFoundException")
    public JAXBElement<FileNotFoundException> createFileNotFoundException(FileNotFoundException value) {
        return new JAXBElement<FileNotFoundException>(_FileNotFoundException_QNAME, FileNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Read }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "read")
    public JAXBElement<Read> createRead(Read value) {
        return new JAXBElement<Read>(_Read_QNAME, Read.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogApply }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "logApply")
    public JAXBElement<LogApply> createLogApply(LogApply value) {
        return new JAXBElement<LogApply>(_LogApply_QNAME, LogApply.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "readResponse")
    public JAXBElement<ReadResponse> createReadResponse(ReadResponse value) {
        return new JAXBElement<ReadResponse>(_ReadResponse_QNAME, ReadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Scan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "scan")
    public JAXBElement<Scan> createScan(Scan value) {
        return new JAXBElement<Scan>(_Scan_QNAME, Scan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "scanResponse")
    public JAXBElement<ScanResponse> createScanResponse(ScanResponse value) {
        return new JAXBElement<ScanResponse>(_ScanResponse_QNAME, ScanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceNotInitializedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "ServiceNotInitializedException")
    public JAXBElement<ServiceNotInitializedException> createServiceNotInitializedException(ServiceNotInitializedException value) {
        return new JAXBElement<ServiceNotInitializedException>(_ServiceNotInitializedException_QNAME, ServiceNotInitializedException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "KeyNotFoundException")
    public JAXBElement<KeyNotFoundException> createKeyNotFoundException(KeyNotFoundException value) {
        return new JAXBElement<KeyNotFoundException>(_KeyNotFoundException_QNAME, KeyNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AtomicScan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "atomicScan")
    public JAXBElement<AtomicScan> createAtomicScan(AtomicScan value) {
        return new JAXBElement<AtomicScan>(_AtomicScan_QNAME, AtomicScan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AtomicScanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "atomicScanResponse")
    public JAXBElement<AtomicScanResponse> createAtomicScanResponse(AtomicScanResponse value) {
        return new JAXBElement<AtomicScanResponse>(_AtomicScanResponse_QNAME, AtomicScanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Init }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "init")
    public JAXBElement<Init> createInit(Init value) {
        return new JAXBElement<Init>(_Init_QNAME, Init.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceInitializingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "ServiceInitializingException")
    public JAXBElement<ServiceInitializingException> createServiceInitializingException(ServiceInitializingException value) {
        return new JAXBElement<ServiceInitializingException>(_ServiceInitializingException_QNAME, ServiceInitializingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceAlreadyInitializedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "ServiceAlreadyInitializedException")
    public JAXBElement<ServiceAlreadyInitializedException> createServiceAlreadyInitializedException(ServiceAlreadyInitializedException value) {
        return new JAXBElement<ServiceAlreadyInitializedException>(_ServiceAlreadyInitializedException_QNAME, ServiceAlreadyInitializedException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeginGreaterThanEndException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "BeginGreaterThanEndException")
    public JAXBElement<BeginGreaterThanEndException> createBeginGreaterThanEndException(BeginGreaterThanEndException value) {
        return new JAXBElement<BeginGreaterThanEndException>(_BeginGreaterThanEndException_QNAME, BeginGreaterThanEndException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.slave.assignment3.pcsd.diku.dk/", name = "initResponse")
    public JAXBElement<InitResponse> createInitResponse(InitResponse value) {
        return new JAXBElement<InitResponse>(_InitResponse_QNAME, InitResponse.class, null, value);
    }

}
