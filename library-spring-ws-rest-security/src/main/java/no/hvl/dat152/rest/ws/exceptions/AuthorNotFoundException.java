/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 */
public class AuthorNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public AuthorNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
