package no.hvl.dat152.rest.ws.main.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.security.payload.AuthRequest;
import no.hvl.dat152.rest.ws.security.payload.SignupRequest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestSignupLogin {
	
	private String API_ROOT = "http://localhost:8090/elibrary/api/v1/auth";
	
	@DisplayName("JUnit test for @PostMapping(/signup) endpoint")
	@Test
	public void registerUser_thenOK() {
		
		SignupRequest user1 = new SignupRequest();
		user1.setEmail("test1@email.com");
		user1.setFirstname("Test1");
		user1.setLastname("User1");
		user1.setPassword("test1_pwd");		
		
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(user1)
				.post(API_ROOT+"/signup");
	    
	    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
	    assertEquals("Test1", response.jsonPath().get("firstname"));

	}

	@DisplayName("JUnit test for @PostMapping(/login) endpoint")
	@Test
	public void loginUser_thenOK() throws UserNotFoundException {
		
		AuthRequest authReq = new AuthRequest();
		authReq.setEmail("robert@email.com");
		authReq.setPassword("robert_pwd");	
		
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(authReq)
				.post(API_ROOT+"/login");

		System.out.println("robert_access_token: "+response.jsonPath().get("accessToken").toString());
	    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	    assertTrue(response.jsonPath().get("accessToken").toString().contains("eyJhbGciOiJSUzM4NCJ9"));

	}
	
	@DisplayName("JUnit test for @PostMapping(/login) endpoint")
	@Test
	public void loginUserAdmin_thenOK() throws UserNotFoundException {
		
		AuthRequest authReq = new AuthRequest();
		authReq.setEmail("berit@email.com");
		authReq.setPassword("berit_pwd");	
		
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(authReq)
				.post(API_ROOT+"/login");

		System.out.println("berit_access_token: "+response.jsonPath().get("accessToken").toString());
	    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	    assertTrue(response.jsonPath().get("accessToken").toString().contains("eyJhbGciOiJSUzM4NCJ9"));

	}

}
