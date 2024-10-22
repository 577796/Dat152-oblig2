/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
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
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

    // TODO authority annotation
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            // Call the service to create the new user
            User createdUser = userService.saveUser(user);

            // Return the newly created user with HTTP status 201 Created
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected exceptions
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){

        List<User> users = userService.findAllUsers();

        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id) throws UserNotFoundException, OrderNotFoundException{

        User user = userService.findUser(id);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody User updateUser) throws UserNotFoundException, AccessDeniedException {

        // Call the service to update the user by passing both the ID and the User object
        User savedUser = userService.updateUser(updateUser, id);

        // Return the updated user and status 200 OK
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/users/{uid}/orders/{oid}")
    public ResponseEntity<Object> getUserOrder(@PathVariable("uid") Long uid, @PathVariable("oid") Long oid)
            throws UserNotFoundException, OrderNotFoundException, AccessDeniedException {

        Order order = userService.getUserOrder(uid, oid);

        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/users/{id}/orders")
    public ResponseEntity<Set<Order>> getUserOrders(@PathVariable("id") Long userid) {
        try {
            // Call the service to get the orders associated with the user
            Set<Order> userOrders = userService.getUserOrders(userid);

            // Return the set of orders with HTTP status 200 OK
            return new ResponseEntity<>(userOrders, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // Return 404 Not Found if the user does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Return 500 Internal Server Error for any other exceptions
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        try {
            // Call the service to delete the user by ID
            userService.deleteUser(id);
            // return 200 if deleted.
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // Return 404 Not Found if the user does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Return 500 Internal Server Error for any other exceptions
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/users/{userid}/orders/{orderid}")
    public ResponseEntity<Void> deleteUserOrder(@PathVariable("userid") Long userId, @PathVariable("orderid") Long orderId) {
        try {
            userService.deleteUserOrder(userId, orderId);
            // Call the service to delete the order for the user

            // Return 200 No Content if deletion is successful
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (OrderNotFoundException e) {
            // Return 404 Not Found if the order does not exist for the user
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Return 500 Internal Server Error for any other exceptions
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/users/{uid}/orders")
    public ResponseEntity<Object> createUserOrder(@PathVariable("uid") Long uid, @RequestBody Order order)
            throws UserNotFoundException, BookNotFoundException, OrderNotFoundException, AccessDeniedException {

        Link viewUser = linkTo(methodOn(UserController.class).getUser(uid)).withRel("View User").withType("GET");

        User user = userService.createOrdersForUser(uid, order);

        for (Order norder : user.getOrders()) {
            Link viewOrders = linkTo(methodOn(UserController.class).getUserOrder(user.getUserid(), norder.getId()))
                    .withRel("View Order").withType("GET");
            norder.add(viewOrders);
            norder.add(viewUser);
        }

        return new ResponseEntity<>(user.getOrders(), HttpStatus.CREATED);

    }
	
}
