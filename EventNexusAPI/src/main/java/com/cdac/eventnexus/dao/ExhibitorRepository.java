package com.cdac.eventnexus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.eventnexus.entities.Exhibitor;

@Repository
public interface ExhibitorRepository extends JpaRepository<Exhibitor, Long> {
	List<Exhibitor> findByIsActiveTrue();
}
