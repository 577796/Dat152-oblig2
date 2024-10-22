/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import no.hvl.dat152.rest.ws.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
		
	
	public Author findById(long id) throws AuthorNotFoundException {

        return authorRepository.findById(id)
				.orElseThrow(()-> new AuthorNotFoundException("Author with the id: "+id+ "not found!"));
	}

	// TODO public saveAuthor(Author author)
	public Author saveAuthor(Author author){

		return authorRepository.save(author);
	}
	
	// TODO public Author updateAuthor(Author author, int id)
	public Author updateAuthor(Author author) throws AuthorNotFoundException {

		Optional<Author> existingAuthorOpt = authorRepository.findById((long) author.getAuthorId());

		if(!existingAuthorOpt.isPresent()){
			throw new AuthorNotFoundException("Author with id" +author.getAuthorId()+ " not found");
		}

		Author existingAuthor = existingAuthorOpt.get();

		existingAuthor.setFirstname(author.getFirstname());
		existingAuthor.setLastname(author.getLastname());

		return authorRepository.save(existingAuthor);
	}
	
	// TODO public List<Author> findAll()
	public List<Author> findAll(){

		List<Author> authors = (List<Author>) authorRepository.findAll();

		for(Author author: authors){
			author.setBooks(null);
		}
		return authors;
	}
	
	// TODO public void deleteById(Long id) throws AuthorNotFoundException
	/*public void deleteById(Long id) throws AuthorNotFoundException{

		Optional<Author> author = authorRepository.findById(id);

		if(!author.isPresent()){
			throw new AuthorNotFoundException("Auther with id "+id+" not found");
		}
		authorRepository.delete(author.get());
	}*/

	
	// TODO public Set<Book> findBooksByAuthorId(Long id)
	public Set<Book> findBooksByAuthorId(Long id) throws AuthorNotFoundException {
		// Find the author by ID
		Author author = authorRepository.findById(id)
				.orElseThrow(() -> new ArithmeticException("Author with ID " + id + " not found"));

		// Return the set of books associated with the author
		return author.getBooks();
	}


}
