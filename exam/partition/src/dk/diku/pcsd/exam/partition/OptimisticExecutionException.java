package dk.diku.pcsd.exam.partition;

/**
 * Signals that a transaction has conflicted with another one and has to be
 * rattempted.
 * 
 * @author marco
 * 
 */
public class OptimisticExecutionException extends Exception {

	private static final long serialVersionUID = 8724368813643422769L;

}
