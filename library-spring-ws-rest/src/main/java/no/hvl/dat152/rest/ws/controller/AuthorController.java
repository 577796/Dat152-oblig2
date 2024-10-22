/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 * 
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

    @Autowired
    AuthorService authorService;


    // TODO - getAllAuthor (@Mappings, URI, and method)
    @GetMapping("/authors")
    public ResponseEntity<Object> findAll() {

        List<Author> authors = authorService.findAll();

        if (authors.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    // TODO - getAuthor (@Mappings, URI, and method)
    @GetMapping("/authors/{authorID}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("authorID") long id) {

        try {
            Author author = authorService.findById(id);

            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // TODO - updateAuthor (@Mappings, URI, and method)
    @PutMapping("/authors/{authorID}")
    public ResponseEntity<Author> updateAuthor(@PathVariable("authorID") long id, @RequestBody Author updateAuthor) {

        try {
            updateAuthor.setAuthorId((int) id);

            Author savedAuthor = authorService.updateAuthor(updateAuthor);

            return new ResponseEntity<>(savedAuthor, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    // TODO - getBooksByAuthorId (@Mappings, URI, and method)
    @GetMapping("/authors/{id}/books")
    public ResponseEntity<Set<Book>> findBooksByAuthorId(@PathVariable("id") Long id) {
        try {
            Set<Book> books = authorService.findBooksByAuthorId(id);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

	


