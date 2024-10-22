/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.hvl.dat152.rest.ws.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // TODO - getOrder (@Mappings, URI=/orders/{id}, and method)
    //Completed
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> findOrder(@PathVariable("id") Long id) {
        try {
            // Call the service to find the order by ID
            Order order = orderService.findOrder(id);

            // Return the order with 200 OK status
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            // Return 404 Not Found if the order is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // TODO - getOrders (@Mappings, URI=/orders, and method) + filter by expiry and paginate
    // partly complete - no filter.
    @GetMapping("/orders")
    public ResponseEntity<Object> getAllBorrowOrders
    (@RequestParam(required = false) LocalDate expiry,
     @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {

        List<Order> orders = null;
        if (expiry != null) {

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("expiry").ascending());
            orders = orderService.findOrdersByExpiryBefore(expiry, pageable).getContent();

        } else {
            orders = orderService.findAllSortedByExpiry(pageNumber, pageSize);
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);

    }

    //Complete
    @PutMapping("/orders/{id}")
    public ResponseEntity <Order> updateOrder(@PathVariable("id") long id, @RequestBody Order updateOrder){
        try{
            updateOrder.setExpiry(updateOrder.getExpiry());

            Order updatedOrder = orderService.updateOrder(updateOrder, id);

            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);

            } catch (OrderNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    //Complete
    @DeleteMapping("/orders/{id}")
    public ResponseEntity <Order> deleteOrder(@PathVariable("id") long id){
        try{
            orderService.deleteOrder(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/orders/expiry")
    public ResponseEntity<Page<Order>> findOrdersByExpiryBefore(
            @RequestParam("expiry") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiry,
            @PageableDefault(size = 10) Pageable pageable) {

        try {
            // Fetch orders that expire on the given date, with pagination
            Page<Order> orders = orderService.findOrdersByExpiryBefore(expiry, pageable);

            // If no orders are found, return 204 No Content
            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Return the orders with HTTP status 200 OK
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected exceptions
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}





	



	

