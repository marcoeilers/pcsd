package dk.diku.pcsd.keyvaluebase.exceptions;

public class ServiceAlreadyConfiguredException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceAlreadyConfiguredException (String message) {
		super(message);
	}

	public ServiceAlreadyConfiguredException () {
		super("Service is already configured");
	}
}
