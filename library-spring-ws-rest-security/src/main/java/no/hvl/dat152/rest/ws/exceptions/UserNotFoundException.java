/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 */
public class UserNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
