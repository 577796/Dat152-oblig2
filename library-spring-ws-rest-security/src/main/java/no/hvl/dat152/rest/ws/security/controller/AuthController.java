/**
 * 
 */
package no.hvl.dat152.rest.ws.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.security.payload.AuthRequest;
import no.hvl.dat152.rest.ws.security.payload.AuthResponse;
import no.hvl.dat152.rest.ws.security.payload.SignupRequest;
import no.hvl.dat152.rest.ws.security.service.AuthService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authReq){
		
		try {
			
			AuthResponse authResp = authService.createJwtAccessToken(authReq);
			
			return ResponseEntity.ok().body(authResp);
			
		}catch(BadCredentialsException ex) {
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupReq){
		
		// raise exception if email already exists
		if(authService.exist(signupReq.getEmail())) {
			return ResponseEntity.badRequest().body("Error: Email already exist!");
		}
		
		// create user
		User user = authService.save(signupReq);
		
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

}
