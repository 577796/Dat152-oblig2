/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 */
public class UnauthorizedOrderActionException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedOrderActionException(String customMessage) {
		super(customMessage);
	}
	
}
