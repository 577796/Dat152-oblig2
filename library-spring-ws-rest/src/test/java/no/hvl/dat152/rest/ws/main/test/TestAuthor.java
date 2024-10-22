package no.hvl.dat152.rest.ws.main.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.service.AuthorService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TestAuthor {

	@Autowired
	private AuthorService authorService;

	private String API_ROOT = "http://localhost:8090/elibrary/api/v1";

	
	@DisplayName("JUnit test for @GetMapping(/authors) endpoint")
	@Test
	public void getAllAuthors_thenOK() {
		Response response = RestAssured.get(API_ROOT+"/authors");
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertTrue(response.jsonPath().getList("authorId").size() > 0);
	}
	
	@DisplayName("JUnit test for @GetMapping(/authors/{id}) endpoint")
	@Test
	public void getAuthorById_thenOK() throws AuthorNotFoundException {

	    Response response = RestAssured.get(API_ROOT+"/authors/1");
	    
	    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	    assertEquals("1", response.jsonPath().get("authorId").toString());

	}
	
	@DisplayName("JUnit test for @PostMapping(/authors) endpoint")
	@Test
	public void createAuthor_thenOK() throws AuthorNotFoundException {
		Author author = new Author("Test", "Author");
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(author)
				.post(API_ROOT+"/authors");
	    
	    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
	    assertEquals("Test", response.jsonPath().get("firstname"));
	}
	
	@DisplayName("JUnit test for @GetMapping(/authors/{id}/books) endpoint")
	@Test
	public void getBooksOfAuthor_thenOK() throws AuthorNotFoundException, BookNotFoundException {
		
		Response response = RestAssured.get(API_ROOT+"/authors/1/books");
	    
	    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	    assertTrue(response.jsonPath().getList("id").size() > 0);
	}
	
	@DisplayName("JUnit test for @PutMapping(/authors/{id}) endpoint")
	@Test
	public void updateAuthor_thenOK() throws AuthorNotFoundException, BookNotFoundException {
		Author author = new Author("Test2", "Author");
		author = authorService.saveAuthor(author);
		
		Author upd_author = new Author("Test22", "Author_2");
		upd_author.setAuthorId(author.getAuthorId());
		
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(upd_author)
				.put(API_ROOT+"/authors/{id}", upd_author.getAuthorId());
	    
	    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	    assertEquals("Test22", response.jsonPath().get("firstname"));
	}


}
