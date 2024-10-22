/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

	// TODO authority annotation
    @Autowired
    AuthorService authorService;


    // TODO - getAllAuthor (@Mappings, URI, and method)
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/authors")
    public ResponseEntity<Object> findAll() {

        List<Author> authors = authorService.findAll();

        if (authors.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/authors")
    public ResponseEntity<Object> createAuthor(@RequestBody Author author) throws BookNotFoundException {

        Author nauthor = authorService.createAuthor(author);

        return new ResponseEntity<>(nauthor, HttpStatus.CREATED);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authors/{authorID}")
    public ResponseEntity<Author> getAuthorById(@PathVariable("authorID") long id) {

        try {
            Author author = authorService.findById(id);

            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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


    @PreAuthorize("hasAuthority('USER')")
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

