package com.cdac.eventnexus.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.eventnexus.dao.PaymentRepository;
import com.cdac.eventnexus.dto.EventRegistrationRequestDto;
import com.cdac.eventnexus.dto.EventRegistrationResponseDto;
import com.cdac.eventnexus.dto.PaymentRequestDto;
import com.cdac.eventnexus.dto.PaymentResponseDto;
import com.cdac.eventnexus.service.EventRegistrationService;
import com.cdac.eventnexus.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/event-registrations")
@RequiredArgsConstructor
public class EventRegistrationController {
    
	private final EventRegistrationService eventRegistrationService;
	
	//related to non-event_registration table
	
	  private final PaymentService paymentService;
	  private final PaymentRepository paymentRepository;


    @PostMapping
    @Operation(summary = "Register customer for event")
    public ResponseEntity<EventRegistrationResponseDto> registerForEvent(
            @RequestBody EventRegistrationRequestDto request) {
        return ResponseEntity.ok(eventRegistrationService.register(request));
    }

    @GetMapping
    @Operation(summary = "Get all event registrations")
    public ResponseEntity<List<EventRegistrationResponseDto>> getAll() {
        return ResponseEntity.ok(eventRegistrationService.getAllRegistrations());
    }
    
    //Added on 08-08-2025
    @GetMapping("/customer/{customerId}")
    public List<EventRegistrationResponseDto> getRegistrationsByCustomer(@PathVariable Long customerId) {
        return eventRegistrationService.getRegistrationsByCustomer(customerId);
    }
    
    //Added on 08-08-2025, when not using event_registration table
    
  

    @PostMapping("/test")
    @Operation(summary = "Register customer for an event (FREE or paid)")
    public ResponseEntity<PaymentResponseDto> register(@RequestBody @Valid PaymentRequestDto dto) {
        PaymentResponseDto res = paymentService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/my")
    @Operation(summary = "List my registrations (payments as registrations)")
    public ResponseEntity<List<PaymentResponseDto>> myRegistrations(@RequestParam Long customerId) {
        List<PaymentResponseDto> list = paymentRepository.findAllByCustomer_Id(customerId)
            .stream()
            .map(p -> {
                PaymentResponseDto dto = new PaymentResponseDto();
                dto.setId(p.getId());
                dto.setCustomerId(p.getCustomer().getId());
                dto.setEventId(p.getEvent().getId());
                dto.setAmount(p.getAmount());
                dto.setPaymentDate(p.getPaymentDate());
                dto.setPaymentMode(p.getMode().name());
                dto.setCustomerName(p.getCustomer().getUsername());
                dto.setEventTitle(p.getEvent().getTitle());
                return dto;
            })
            .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/eligible/{eventId}")
    @Operation(summary = "Check if a customer is registered for an event")
    public ResponseEntity<Boolean> isRegistered(@RequestParam Long customerId, @PathVariable Long eventId) {
        return ResponseEntity.ok(paymentRepository.existsByCustomer_IdAndEvent_Id(customerId, eventId));
    }
    
 
    
}

