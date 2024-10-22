/**
 * 
 */
package no.hvl.dat152.rest.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import no.hvl.dat152.rest.ws.security.service.UserDetailsImpl;

/**
 * @author tdoy
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtUtils;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String jwt = parseJwtFromRequestHeader(request);
			if(jwt != null && jwtUtils.validateAccessTokenWithPublicKey(jwt)) {		// can also validate with SECRET_KEY (it means, token must be signed with SECRET_KEY)

				UserDetails userDetails = getUserDetails(jwt);
				
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, 
																		null, userDetails.getAuthorities());
				
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(auth);
				
			}
		}catch(Exception e) {
			LOGGER.error("Failed to set user authentication: {}", e);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String parseJwtFromRequestHeader(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer"))
			return headerAuth.substring(7);
		
		return null;
	}
	
	private UserDetails getUserDetails(String jwt) throws UserNotFoundException {
		
		User user = new User();
		
		Claims claims = jwtUtils.parseClaimsWithPublicKey(jwt);
		String email = claims.getSubject();
		String firstname = claims.get("firstname").toString();		
		String lastname = claims.get("lastname").toString();		
		
		user.setEmail(email);
		user.setFirstname(firstname);
		user.setLastname(lastname);
		
		Set<Role> allrole = new HashSet<>();
		@SuppressWarnings("unchecked")
		List<String> roles = (ArrayList<String>) claims.get("roles");

		for(String role : roles) {
			Role _role = roleRepository.findByName(role);
			allrole.add(_role);
		}
		user.setRoles(allrole);
		
		return UserDetailsImpl.build(user);
		
	}

}
