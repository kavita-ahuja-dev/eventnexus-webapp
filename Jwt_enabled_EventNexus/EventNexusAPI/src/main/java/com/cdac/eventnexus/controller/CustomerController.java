package com.cdac.eventnexus.controller;

import com.cdac.eventnexus.dto.CustomerRequestDto;
import com.cdac.eventnexus.dto.CustomerResponseDto;
import com.cdac.eventnexus.dto.DashboardSummaryDto;
import com.cdac.eventnexus.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor

@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")

public class CustomerController {

    private final CustomerService customerService;

    // Create customer
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create new customer")
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto dto) {
        CustomerResponseDto saved = customerService.createCustomer(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get all active customers
    @PreAuthorize("hasRole('EXHIBITOR','ADMIN')")
    @GetMapping
    @Operation(summary = "Fetch all active customers")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Get by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        CustomerResponseDto dto = customerService.getCustomerById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by ID")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    
}
