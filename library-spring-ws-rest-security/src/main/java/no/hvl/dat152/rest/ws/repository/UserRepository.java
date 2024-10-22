/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import no.hvl.dat152.rest.ws.model.User;


/**
 * 
 */
public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	Boolean existsByEmail(String email);
}
