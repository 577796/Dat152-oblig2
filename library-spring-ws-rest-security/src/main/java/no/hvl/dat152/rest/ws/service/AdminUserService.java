/**
 * 
 */
package no.hvl.dat152.rest.ws.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class AdminUserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public User saveUser(User user) {
		
		user = userRepository.save(user);
		
		return user;
	}
	
	// TODO public User deleteUserRole(Long id, String role)
	public User deleteUserRole(Long id, String role) throws UserNotFoundException {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

		Role roleToDelete = roleRepository.findByName(role);

		// Remove the role from the user's roles set
		if (user.getRoles().contains(roleToDelete)) {
			user.getRoles().remove(roleToDelete);
		} else {
			throw new IllegalArgumentException("Role " + role + " not found for the user");
		}

		return userRepository.save(user);
	}
	// TODO public User updateUserRole(Long id, String role)
	public User updateUserRole(Long id, String roleName) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

		// Assuming you have a Role entity and a roleRepository to fetch Role by name
		Role role = roleRepository.findByName(roleName);

		// Add the Role to the user's roles set if it does not already exist
		if (!user.getRoles().contains(role)) {
			user.getRoles().add(role);
		} else {
			throw new IllegalArgumentException("User already has the role " + roleName);
		}

		return userRepository.save(user);
	}

	public User findUser(Long id) throws UserNotFoundException {

		User user = userRepository.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+id+" not found"));

		return user;
	}
}
