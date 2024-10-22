/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 */
public class BookNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public BookNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
