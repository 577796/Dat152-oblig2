/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.BookRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

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

	public Order saveOrder(Order order) {

		return orderRepository.save(order);
	}

	public Order findOrder(Long id) throws OrderNotFoundException {

		return orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));
	}

	public List<Order> findAllOrders() {

		return orderRepository.findAll();
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

}
