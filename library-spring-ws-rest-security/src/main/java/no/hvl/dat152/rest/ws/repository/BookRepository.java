/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import no.hvl.dat152.rest.ws.model.Book;

/**
 * @author tdoy
 */
public interface BookRepository extends CrudRepository<Book, Long>, PagingAndSortingRepository<Book, Long> {
	
	Optional<Book> findByIsbn(String isbn);
	
	Page<Book> findAll(Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
	Book findBookByISBN(@Param("isbn") String isbn);
	
	@Query("SELECT b FROM Book b join b.authors a WHERE a.authorId = :authorId")
	List<Book> findBooksByAuthorId(@Param("authorId") int authorId);
	
	@Query(value = "SELECT * FROM Book limit :limit offset :offset", nativeQuery=true)
	List<Book> findAllPaginate(@Param("limit") int limit, @Param("offset") int offset);

}
