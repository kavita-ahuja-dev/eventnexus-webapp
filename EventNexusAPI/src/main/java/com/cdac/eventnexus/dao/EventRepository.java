package com.cdac.eventnexus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.eventnexus.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
	List<Event> findByIsActiveTrue();

    // Unidirectional search based on EventType
    List<Event> findByTypeId(Long typeId);

    // Unidirectional search based on Exhibitor
    List<Event> findByExhibitorId(Long exhibitorId);
	
    //Added 04-08-2025
    List<Event> findByTypeIdAndExhibitorIdAndIsActiveTrue(Long typeId, Long exhibitorId);
    List<Event> findByTypeIdAndIsActiveTrue(Long typeId);
    List<Event> findByExhibitorIdAndIsActiveTrue(Long exhibitorId);
    List<Event> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title);

   
 // Count active events
    long countByIsActiveTrue();
    
    //for exhibitor dashboard
    long countByExhibitor_Id(Long exhibitorId);
    

    
    
}
