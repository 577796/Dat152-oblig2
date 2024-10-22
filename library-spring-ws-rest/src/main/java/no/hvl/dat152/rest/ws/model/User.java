/**
 * 
 */
package no.hvl.dat152.rest.ws.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * @author tdoy
 */
@Entity
@Table(name = "users")
public class User extends RepresentationModel<User>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userid;
	
	@Column(nullable = false)
	private String firstname;
	
	@Column(nullable = false)
	private String lastname;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "userid")
	private Set<Order> orders = new HashSet<>();

	public User() {
		//default
	}
	
	public User(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
	/**
	 * @return the userid
	 */
	public Long getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the orders
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	public void addOrder(Order order) {
		orders.add(order);
	}
	
	public void removeOrder(Order order) {
		orders.remove(order);
	}
	
//	@Override
//    public final int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + 
//				((userid == 0) ? 0 :Long.valueOf(userid).hashCode());
//		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
//		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
//		
//        return result;
//    }
//	
//	@Override
//	public final boolean equals(final Object obj) {
//		if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        User other = (User)obj;
//        
//        return this.userid == other.userid;
//	}
	
}
