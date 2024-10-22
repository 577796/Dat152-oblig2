/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.repository.BookRepository;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BookRepository bookRepository;


    public List<User> findAllUsers(){

        return (List<User>) userRepository.findAll();
    }

    public User findUser(Long userid) throws UserNotFoundException {

        User user = userRepository.findById(userid)
                .orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));

        return user;
    }

    public User saveUser(User user){

        return userRepository.save(user);
    }


    public User updateUser(User user, long id) throws UserNotFoundException, AccessDeniedException {

        verifyUser(id);
        // Find the user by ID
        Optional<User> userToUpdate = userRepository.findById(id);

        // Throw an exception if the user is not found
        if (!userToUpdate.isPresent()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        // Get the existing user from the database
        User existingUser = userToUpdate.get();

        // Update the fields of the existing user with the new data
        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        // Add any other fields you want to update here

        // Save the updated user
        return userRepository.save(existingUser);
    }

    public Set<Order> getUserOrders(Long userid) throws UserNotFoundException, AccessDeniedException {

        verifyUser(userid);
        // Find the user by ID
        Optional<User> user = userRepository.findById(userid);

        // If user is not found, throw an exception
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userid + " not found");
        }

        // Return the orders associated with the user
        return user.get().getOrders();
    }

    // TODO public void deleteUser(Long id) throws UserNotFoundException
    public void deleteUser(long id) throws UserNotFoundException {


        Optional<User> userToDelete = userRepository.findById(id);

        if(!userToDelete.isPresent()){
            throw new UserNotFoundException("can't find user");
        }
        userRepository.delete(userToDelete.get());
    }


    public void deleteOrderForUser(Long userid, Long orderId) throws UserNotFoundException, OrderNotFoundException {
        // Check if the user exists
        if (!userRepository.existsById(userid)) {
            throw new UserNotFoundException("User with ID " + userid + " not found");
        }

        // Check if the order exists and is associated with the user
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        // If the order is not found, throw an exception
        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found for user with ID " + userid);
        }
        // Delete the order from the OrderRepository
        orderRepository.deleteById(orderId);
    }

    // TODO public User createOrdersForUser(Long userid, Order order)
    public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException, BookNotFoundException, AccessDeniedException {

        verifyUser(userid);

        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userid + " not found"));

        if (bookRepository.findBookByISBN(order.getIsbn()) == null) {
            throw new BookNotFoundException("Book with ISBN " + order.getIsbn() + " not found.");
        }

        user.addOrder(order);

        orderRepository.save(order);

        return userRepository.save(user);

    }

    public void verifyUser(Long userId) throws AccessDeniedException {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        authentication.getAuthorities().forEach(authority -> System.out.println("auth" + authority));
        // Fetch the user by their ID (the one being accessed)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Check if the authenticated user matches the user being accessed
        if (!user.getEmail().equals(currentUsername)) {
            throw new AccessDeniedException("You do not have permission to access this user's data");
        }
    }
}
