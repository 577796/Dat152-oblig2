package no.hvl.dat152.rest.ws.main.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestsAdminUser {
	
	private String API_ROOT = "http://localhost:8090/elibrary/api/v1/admin";
	
	@Value("${super.admin.token.test}") 
	private String SUPER_ADMIN_TOKEN;
	
	@Value("${user.token.test}")
	private String USER_TOKEN;
	
	@DisplayName("JUnit test for @PutMapping(/users/{id}) endpoint")
	@Test
	public void updateUserRole_thenOK() {
		
		Response response = RestAssured.given()
				.header("Authorization", "Bearer "+ SUPER_ADMIN_TOKEN)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("role", "admin")
				.put(API_ROOT+"/users/{id}", 1);
	    
	    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	    assertEquals("robert@email.com", response.jsonPath().get("email"));
	}
	
	@DisplayName("JUnit test for @PutMapping(/users/{id}) endpoint")
	@Test
	public void updateUserRole_USER_ROLE_thenUnauthorized() {
		
		Response response = RestAssured.given()
				.header("Authorization", "Bearer "+ USER_TOKEN)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("role", "super_admin")
				.put(API_ROOT+"/users/{id}", 1);
	    
	    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
	}
	
	@DisplayName("JUnit test for @DeleteMapping(/users/{id}) endpoint")
	@Test
	public void deleteUserRole_thenOK() {
		
		Response response = RestAssured.given()
				.header("Authorization", "Bearer "+ SUPER_ADMIN_TOKEN)
				.queryParam("role", "user")
				.delete(API_ROOT+"/users/{id}", 3);
	    
	    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	    assertFalse(response.jsonPath().getList("roles").get(0).toString().contains("USER"));
	}

}
