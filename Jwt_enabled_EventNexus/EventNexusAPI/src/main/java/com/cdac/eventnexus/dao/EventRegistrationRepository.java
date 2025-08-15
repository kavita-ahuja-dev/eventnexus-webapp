package com.cdac.eventnexus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.eventnexus.entities.EventRegistration;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByCustomerId(Long customerId);

}