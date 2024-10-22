/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

import no.hvl.dat152.rest.ws.repository.BookRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.security.UserDetailsImpl;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;


	/*public Order findOrder(Long id) throws OrderNotFoundException, UnauthorizedOrderActionException {

		verifyPrincipalOfOrder(id);	// verify who is making this request - Only ADMIN or SUPER_ADMIN can access any order
		Order order = orderRepository.findById(id)
				.orElseThrow(()-> new OrderNotFoundException("Order with id: "+id+" not found in the order list!"));

		return order;
	}

	private boolean verifyPrincipalOfOrder(Long id) throws UnauthorizedOrderActionException {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		// verify if the user sending request is an ADMIN or SUPER_ADMIN
		for(GrantedAuthority authority : userPrincipal.getAuthorities()){
			if(authority.getAuthority().equals("ADMIN") ||
					authority.getAuthority().equals("SUPER_ADMIN")) {
				return true;
			}
		}

		// otherwise, make sure that the user is the one who initially made the order
		String email = orderRepository.findEmailByOrderId(id);

		if(email.equals(userPrincipal.getEmail()))
			return true;

		throw new UnauthorizedOrderActionException("Unauthorized order action!");

	}*/

	public Order saveOrder(Order order) {

		return orderRepository.save(order);
	}


	public List<Order> findAllOrders() {

		return (List<Order>) orderRepository.findAll();
	}


	public void deleteOrder(Long Id) throws OrderNotFoundException {
		// Check if the order exists
		if (!orderRepository.existsById(Id)) {
			throw new OrderNotFoundException("Order with ID " + Id + " not found");
		}

		// Delete the order
		orderRepository.deleteById(Id);
	}

	public Page<Order> findOrdersByExpiryBefore(LocalDate expiry, Pageable pageable) {
		return orderRepository.findByExpiryBefore(expiry, pageable);
	}

	public Order updateOrder(Order order, Long id) throws OrderNotFoundException {
		Optional<Order> orderToUpdate = orderRepository.findById(id);

		if (!orderToUpdate.isPresent()) {
			throw new OrderNotFoundException("order not found");
		}

		Order existingOrder = orderToUpdate.get();

		existingOrder.setExpiry(order.getExpiry());

		return orderRepository.save(existingOrder);
	}

	public List<Order> findAllSortedByExpiry(int pageNumber, int pageSize) {

		Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("expiry").descending());

		Page<Order> orderSorted = orderRepository.findAll(page);

		return orderSorted.getContent();
	}
	
	
	public Order findOrder(Long id) throws OrderNotFoundException, UnauthorizedOrderActionException {
		
		verifyPrincipalOfOrder(id);
		Order order = orderRepository.findById(id)
				.orElseThrow(()-> new OrderNotFoundException("Order with id: "+id+" not found in the order list!"));
		
		return order;
	}
	
	private boolean verifyPrincipalOfOrder(Long id) throws UnauthorizedOrderActionException {
		
		JwtAuthenticationToken oauthJwtToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userPrincipal = (UserDetailsImpl) oauthJwtToken.getDetails();
		// verify if the user sending request is an ADMIN or SUPER_ADMIN
		for(GrantedAuthority authority : userPrincipal.getAuthorities()){
			if(authority.getAuthority().equals("ADMIN")) {
				return true;
			}
		}
		
		// otherwise, make sure that the user is the one who initially made the order
		String email = orderRepository.findEmailByOrderId(id);
		
		if(email.equals(userPrincipal.getEmail()))
			return true;
		
		throw new UnauthorizedOrderActionException("Unauthorized order action!");

	}

}
