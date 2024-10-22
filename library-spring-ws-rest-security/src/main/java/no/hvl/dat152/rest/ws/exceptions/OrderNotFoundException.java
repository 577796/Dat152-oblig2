/**
 * 
 */
package no.hvl.dat152.rest.ws.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 */
public class OrderNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public OrderNotFoundException(String customMessage) {
		super(customMessage);
	}
	
}
