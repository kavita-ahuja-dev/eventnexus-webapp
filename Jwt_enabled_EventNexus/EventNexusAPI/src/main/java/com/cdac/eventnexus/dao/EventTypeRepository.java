package com.cdac.eventnexus.dao;

import com.cdac.eventnexus.entities.EventType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
	
	List<EventType> findByIsActiveTrue();
	
	boolean existsByName(String name);
    List<EventType> findByNameContainingIgnoreCase(String keyword);

}
