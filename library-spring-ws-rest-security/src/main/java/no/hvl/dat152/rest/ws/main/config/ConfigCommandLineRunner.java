package no.hvl.dat152.rest.ws.main.config;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
import no.hvl.dat152.rest.ws.repository.BookRepository;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import no.hvl.dat152.rest.ws.service.AuthorService;

@Component
class ConfigCommandLineRunner implements CommandLineRunner  {

  private static final Logger log = LoggerFactory.getLogger(ConfigCommandLineRunner.class);

  @Autowired
  AuthorService authorService;
  
  @Autowired
  BookRepository bookRepository;
  
  @Autowired
  AuthorRepository authorRepository;
  
  @Autowired
  UserRepository userRepository;
  
  @Autowired
  RoleRepository roleRepository;
  
  @Override
  public void run(String... args) throws Exception {
	  
	  authorRepository.saveAll(createDefaultAuthors());
	  bookRepository.saveAll(creatDefaultBooks());
	  roleRepository.saveAll(createDefaultRoles());
	  userRepository.saveAll(createDefaultUsersPlusOrders());
   
  }
  
	private Iterable<Author> createDefaultAuthors() {
		
		List<Author> authors = new ArrayList<Author>();
		
		authors.add(new Author("Shari", "Pfleeger"));
		authors.add(new Author("Jim", "Cooling"));
		authors.add(new Author("James", "Kurose"));
		authors.add(new Author("Keith", "Ross"));
		authors.add(new Author("Martin", "Kleppmann"));
		authors.add(new Author("Cormen", "Leiserson"));
		
		return authors;
	}
	
	private Iterable<Book> creatDefaultBooks() throws AuthorNotFoundException {
		
		Author author1 = authorService.findById(1L);
		Author author2 = authorService.findById(2L);
		Author author3 = authorService.findById(3L);
		Author author4 = authorService.findById(4L);
		Author author5 = authorService.findById(5L);
		Author author6 = authorService.findById(6L);
		
		Set<Author> authors = new HashSet<Author>();
		Book book1 = new Book();
		book1.setIsbn("abcde1234");
		book1.setTitle("Software Engineering and System");
		authors.add(author1);
		book1.setAuthors(authors);
		
		Set<Author> authors2 = new HashSet<Author>();
		Book book2 = new Book();
		book2.setIsbn("ghijk1234");
		book2.setTitle("Computer Network");
		authors2.add(author3);
		authors2.add(author4);
		book2.setAuthors(authors2);
		
		Set<Author> authors3 = new HashSet<Author>();
		Book book3 = new Book();
		book3.setIsbn("qabfde1230");
		book3.setTitle("Real-Time Operating System");
		authors3.add(author2);
		book3.setAuthors(authors3);
		
		Set<Author> authors4 = new HashSet<Author>();
		Book book4 = new Book();
		book4.setIsbn("rstuv1540");
		book4.setTitle("Introduction To Algorithms");
		authors4.add(author6);
		book4.setAuthors(authors4);
		
		Set<Author> authors5 = new HashSet<Author>();
		Book book5 = new Book();
		book5.setIsbn("cfewxy2651");
		book5.setTitle("Designing Data-Intensive Applications");
		authors5.add(author5);
		book5.setAuthors(authors5);
		
		
		List<Book> books =  new ArrayList<Book>();		
		books.add(book1);
		books.add(book2);
		books.add(book3);
		books.add(book4);
		books.add(book5);
		
		return books;
	}
	
	private Iterable<User> createDefaultUsersPlusOrders(){
		
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		
		List<User> users = new ArrayList<>();
		
		// user1 (roles = USER)
		String robertPwd = pwdEncoder.encode("robert_pwd");
		User user1 = new User("robert@email.com",robertPwd,"Robert", "Isaac");
		// orders
		Order order1 = new Order("ghijk1234", LocalDate.now().plusWeeks(2));		
		user1.addOrder(order1);
		Order order2 = new Order("qabfde1230", LocalDate.now().plusWeeks(3));		
		user1.addOrder(order1);
		user1.addOrder(order2);
		// add roles
		Role role1 = roleRepository.findById(3).get();
		user1.addRole(role1);
		System.out.println("User = "+user1.getFirstname()+" Role = "+role1.getName());
		
		
		//user2 (roles = ADMIN, USER)
		String kristinPwd = pwdEncoder.encode("kristin_pwd");
		User user2 = new User("kristin@email.com",kristinPwd,"Kristin", "Solberg");
		// add order
		Order order2_1 = new Order("abcde1234", LocalDate.now().plusWeeks(3));
		user2.addOrder(order2_1);
		// add roles
		Role role2 = roleRepository.findById(2).get();
		Role role3 = roleRepository.findById(3).get();
		user2.addRole(role2);
		user2.addRole(role3);
		System.out.println("User = "+user2.getFirstname()+" Role = "+role2.getName()+", "+role3.getName());
		
		//user 3  (roles = SUPER_ADMIN, ADMIN, USER)
		String beritPwd = pwdEncoder.encode("berit_pwd");
		User user3 = new User("berit@email.com", beritPwd, "Berit", "Jørgensen");
		// add roles
		Role role31 = roleRepository.findById(1).get();
		Role role32 = roleRepository.findById(2).get();
		Role role33 = roleRepository.findById(3).get();
		user3.addRole(role31);
		user3.addRole(role32);
		user3.addRole(role33);
		System.out.println("User = "+user3.getFirstname()+" Role = "+role31.getName()+", "+role32.getName()+", "+role33.getName());
		
		users.add(user1);
		users.add(user2);
		users.add(user3);
		
		return users;
		
	}
	
	// we take the roles we have configured in our property file (application.properties)
	@Value("#{'${user.resource.roles}'.split(',')}")
	private List<String> userRoles;
	
	private Iterable<Role> createDefaultRoles(){
		List<Role> roles = new ArrayList<>();
		for(String role : userRoles) {
			roles.add(new Role(role));
		}
		
		return roles;
	}

}
