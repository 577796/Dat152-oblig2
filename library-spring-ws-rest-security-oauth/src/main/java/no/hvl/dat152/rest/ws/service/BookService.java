/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public Book saveBook(Book book) {

        return bookRepository.save(book);

    }

    public List<Book> findAll() {

        return (List<Book>) bookRepository.findAll();

    }


    public Book findByISBN(String isbn) throws BookNotFoundException {

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book with isbn = " + isbn + " not found!"));

        return book;
    }


    public Book updateBook(Book book) throws BookNotFoundException {

        Book existingBook = bookRepository.findBookByISBN(book.getIsbn());

        if (existingBook == null) {
            throw new BookNotFoundException("Book with isbn " + book.getIsbn() + "not found");
        }

        //update title of book
        existingBook.setTitle(book.getTitle());

        return bookRepository.save(existingBook);

    }

    public void deleteByISBN(String isbn) throws BookNotFoundException {

        Book bookToDelte = bookRepository.findBookByISBN(isbn);

        if (bookToDelte == null) {
            throw new BookNotFoundException(" Book with isbn" + isbn + "not found");
        }

        bookRepository.delete(bookToDelte);

    }

    public List<Book> findAllPaginate(Pageable page) {

        return bookRepository.findAllPaginate(page.getPageSize(), (int) page.getOffset());

    }

    public Set<Author> findAuthorsOfBookByISBN(String isbn) {

        Book book = bookRepository.findBookByISBN(isbn);

        return book.getAuthors();
    }
}
