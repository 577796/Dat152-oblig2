/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.BookService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class BookController {

    // TODO authority annotation
    @Autowired
    private BookService bookService;


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/books")
    public ResponseEntity<Object> getAllBooks() {

        List<Book> books = bookService.findAll();

        if (books.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/books/{isbn}")
    public ResponseEntity<Object> getBook(@PathVariable("isbn") String isbn) throws BookNotFoundException {

        Book book = bookService.findByISBN(isbn);

        return new ResponseEntity<>(book, HttpStatus.OK);

    }
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {

        Book nbook = bookService.saveBook(book);


        return new ResponseEntity<>(nbook, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/books/{isbn}")
    public ResponseEntity<Book> updateBook(@PathVariable("isbn") String isbn, @RequestBody Book updateBook) {

        try {
            updateBook.setIsbn(isbn);

            Book savedBook = bookService.updateBook(updateBook);

            return new ResponseEntity<>(savedBook, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/books/{isbn}")
    public ResponseEntity<Book> deleteBook(@PathVariable("isbn") String isbn) {

        try {
            bookService.deleteByISBN(isbn);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/books/{isbn}/authors")
    public ResponseEntity<Object> getAuthorsOfBookByISBN(@PathVariable("isbn") String isbn) {

        Set<Author> authors = bookService.findAuthorsOfBookByISBN(isbn);

        return new ResponseEntity<>(authors, HttpStatus.OK);

    }

}
