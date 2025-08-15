package com.cdac.eventnexus.dao;

import com.cdac.eventnexus.entities.Offer;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
	
	List<Offer> findByIsActiveTrue();

	List<Offer> findByEvent_IdAndIsActiveTrue(Long eventId);

	List<Offer> findByExhibitor_IdAndIsActiveTrue(Long exhibitorId);
	
	boolean existsByEvent_IdAndExhibitor_Id(Long eventId, Long exhibitorId);
	
	@Modifying
    @Transactional
	@Query("update Offer o set o.description = :desc, o.endDate = :endDate where o.id = :id")
	int updateOfferEndDateDesc(@Param("desc") String desc, @Param("endDate") LocalDate endDate, @Param("id") Long id);


}
