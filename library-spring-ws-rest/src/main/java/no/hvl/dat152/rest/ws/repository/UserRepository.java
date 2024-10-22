/**
 * 
 */
package no.hvl.dat152.rest.ws.repository;

import no.hvl.dat152.rest.ws.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import no.hvl.dat152.rest.ws.model.User;
import org.springframework.data.repository.query.Param;

/**
 * 
 */
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT o FROM User u JOIN u.orders o WHERE u.userid = :userId AND o.id = :orderId")
    Order findOrderByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);
}

