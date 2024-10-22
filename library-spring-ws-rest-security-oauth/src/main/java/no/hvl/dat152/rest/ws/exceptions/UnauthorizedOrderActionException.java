/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

/**
 * 
 */
public class UnauthorizedOrderActionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedOrderActionException(String customMessage) {
		super(customMessage);
	}
	
}
