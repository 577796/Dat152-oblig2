/**
 * 
 */
package no.hvl.dat152.rest.ws.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;

/**
 * @author tdoy
 */
@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false, unique = true)
	private String isbn;
	
	@Column(nullable = false)
	private String title;
	
	@ManyToMany
	@JoinTable(name = "book_author",
			joinColumns = { @JoinColumn(name = "fk_book", referencedColumnName = "id")},
			inverseJoinColumns = @JoinColumn(name = "fk_author", referencedColumnName = "authorId"))
	@JsonIgnoreProperties("books")
	private Set<Author> authors = new HashSet<Author>();
	

	public Book() {
		// default constructor
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

    /**
	 * @return the authors
	 */
	public Set<Author> getAuthors() {
		return authors;
	}
	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
	
	public void addAuthor(Author author) {
		this.authors.add(author);
	}
	
	public void removeAuthor(Author author) {
		this.authors.remove(author);
	}
	
	@Override
    public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Long.valueOf(id) == null) ? 0 : Long.hashCode(id));
		result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }
	
	@Override
	public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Book book = (Book) obj;
        
        return this.id == book.id;
	 }
	
	@Override
    public String toString() {
        return "Book [isbn=" + isbn + ", title=" + title  + "]";
    }
}
