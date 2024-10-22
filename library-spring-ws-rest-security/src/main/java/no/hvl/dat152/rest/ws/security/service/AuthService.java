/**
 * 
 */
package no.hvl.dat152.rest.ws.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import no.hvl.dat152.rest.ws.security.JwtTokenUtil;
import no.hvl.dat152.rest.ws.security.payload.AuthRequest;
import no.hvl.dat152.rest.ws.security.payload.AuthResponse;
import no.hvl.dat152.rest.ws.security.payload.SignupRequest;

/**
 * @author tdoy
 */
@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenUtil jwtUtil;
	
	public boolean exist(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public User save(SignupRequest signupReq) {
		
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		// create a new user
		User user = new User(signupReq.getEmail(), 
								pwdEncoder.encode(signupReq.getPassword()), 
								signupReq.getFirstname(), 
								signupReq.getLastname());
		
		Role _role = roleRepository.findByName("USER");	// default role for a new user unless it's changed by the admin
		user.addRole(_role);			
		user = userRepository.save(user);
		
		return user;
	}
	
	public User findUserByEmail(String email) throws UserNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User with email: "+email+" does not exist!"));
		
		return user;
	}
	
	public AuthResponse createJwtAccessToken(AuthRequest authReq) {
		
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authReq.getEmail(), authReq.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		String accessToken = jwtUtil.createAccessTokenWithPublicKey(auth);		// we can also use SECRET_KEY
		
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		// String firstname, String lastname, String email, List<String> roles, String accessToken
		AuthResponse authResp = new AuthResponse(
				userDetails.getFirstname(),
				userDetails.getLastname(),
				userDetails.getEmail(),
				roles,
				accessToken);
		
		return authResp;
	}
}
