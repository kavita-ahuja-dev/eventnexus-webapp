package com.cdac.eventnexus.dao;

import com.cdac.eventnexus.entities.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByIsActiveTrue();
    Optional<Customer> findByUserId(Long userId);


}
