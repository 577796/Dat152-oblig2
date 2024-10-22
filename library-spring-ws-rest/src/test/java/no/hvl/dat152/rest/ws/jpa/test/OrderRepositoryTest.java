/**
 * 
 */
package no.hvl.dat152.rest.ws.jpa.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import no.hvl.dat152.rest.ws.main.LibraryApplication;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

/**
 * 
 */
@SpringBootTest
@ContextConfiguration(classes = LibraryApplication.class)
class OrderRepositoryTest {
	
	@Autowired
	private OrderRepository orderRepo;
	/**
	 * 
	 */
	@Test
	final void testFindByUser_Id() {
		
		List<Order> orders = orderRepo.findByUserId(1L);
		System.out.println(orders.get(0));
		assertTrue(orders.size() == 1);
		assertTrue(orders.get(0).getIsbn().equals("ghijk1234"));

	}
	
	/**
	 * expirydate pattern = yyyy-MM-dd
	 */
	@Test
	final void testFindByExpiryBefore() {
		
		LocalDate  expiry = LocalDate.now().plusWeeks(4);
		Pageable paging = PageRequest.of(0, 2);
		
		Page<Order> orders = orderRepo.findByExpiryBefore(expiry, paging);
		
		System.out.println(orders.getContent());
		System.out.println(orders.getSize());
		System.out.println(orders.getTotalElements());
		
		assertTrue(orders.getNumberOfElements() == 2);
	}
	
	/**
	 * expirydate pattern = yyyy-MM-dd
	 */
	@Test
	final void testFindByExpiryLimitOffset() {
		
		LocalDate  expiry = LocalDate.now().plusWeeks(4);
		
		List<Order> orders = orderRepo.findOrderByExpiry(expiry, 2, 0);
		
		System.out.println(orders);
		
		assertTrue(orders.size() == 2);
	}

}
