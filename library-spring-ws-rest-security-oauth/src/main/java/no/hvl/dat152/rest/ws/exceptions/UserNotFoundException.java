/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

/**
 * 
 */
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
