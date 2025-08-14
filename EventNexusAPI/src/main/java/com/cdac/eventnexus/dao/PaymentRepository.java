package com.cdac.eventnexus.dao;

import com.cdac.eventnexus.entities.Payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
	
	 	boolean existsByCustomer_IdAndEvent_Id(Long customerId, Long eventId);

	    long countByEvent_Id(Long eventId);

	    List<Payment> findAllByCustomer_Id(Long customerId);
	    
	    List<Payment> findByCustomer_Id(Long customerId);
	    
	    @Query("""
	    	    select p 
	    	    from Payment p
	    	    join fetch p.event e
	    	    where p.customer.id = :customerId
	    	""")
	    	List<Payment> findByCustomerIdWithEvent(@Param("customerId") Long customerId);
	    
	 // total registrations  across  events
	    long countByEvent_Exhibitor_Id(Long exhibitorId);

	    // distinct customers across  events 
	    @Query("""
	           select count(distinct p.customer.id)
	           from Payment p
	           where p.event.exhibitor.id = :exhibitorId
	           """)
	    long countDistinctCustomersByExhibitor(@Param("exhibitorId") Long exhibitorId);
	    
}
