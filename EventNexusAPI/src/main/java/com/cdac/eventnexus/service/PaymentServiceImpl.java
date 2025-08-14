package com.cdac.eventnexus.service;

import com.cdac.eventnexus.custom_exceptions.DuplicateProfileException;
import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.EventRepository;
import com.cdac.eventnexus.dao.PaymentRepository;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.PaymentRequestDto;
import com.cdac.eventnexus.dto.PaymentResponseDto;
import com.cdac.eventnexus.entities.Event;
import com.cdac.eventnexus.entities.Payment;
import com.cdac.eventnexus.entities.PaymentMode;
import com.cdac.eventnexus.entities.User;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class PaymentServiceImpl implements PaymentService {

    
	 	private final PaymentRepository paymentRepository;
	    private final UserRepository userRepository;
	    private final EventRepository eventRepository;
	    private final ModelMapper modelMapper;
	    
	    @Override
	    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
	        // Fetch associated customer and event
	        User customer = userRepository.findById(dto.getCustomerId())
	                .orElseThrow(() -> new ResourceNotFoundException("Invalid Customer ID"));

	        Event event = eventRepository.findById(dto.getEventId())
	                .orElseThrow(() -> new ResourceNotFoundException("Invalid Event ID"));

	        // Map DTO to entity and set associations
	        Payment payment = modelMapper.map(dto, Payment.class);
	        payment.setCustomer(customer);
	        payment.setEvent(event);

	        // Save and return response DTO
	        Payment saved = paymentRepository.save(payment);
	        PaymentResponseDto response = modelMapper.map(saved, PaymentResponseDto.class);
	        response.setCustomerName(customer.getUsername());
	        response.setEventTitle(event.getTitle());
	        return response;
	    }

	    @Override
	    public List<PaymentResponseDto> getAllPayments() {
	        return paymentRepository.findAll().stream()
	                .map(payment -> {
	                    PaymentResponseDto dto = modelMapper.map(payment, PaymentResponseDto.class);
	                    dto.setCustomerName(payment.getCustomer().getUsername());
	                    dto.setEventTitle(payment.getEvent().getTitle());
	                    return dto;
	                })
	                .collect(Collectors.toList());
	    }

	    @Override
	    public PaymentResponseDto getPaymentById(Long id) {
	        Payment payment = paymentRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
	        PaymentResponseDto dto = modelMapper.map(payment, PaymentResponseDto.class);
	        dto.setCustomerName(payment.getCustomer().getUsername());
	        dto.setEventTitle(payment.getEvent().getTitle());
	        return dto;
	    }

	    @Override
	    public void deletePayment(Long id) {
	        Payment payment = paymentRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
	        paymentRepository.delete(payment); // You can add soft delete if needed
	    }

	    //Register if not using event_registration table
	    public PaymentResponseDto register(PaymentRequestDto dto) {
	        Long customerId = dto.getCustomerId();
	        Long eventId = dto.getEventId();

	        // Prevent duplicate registration
	        if (paymentRepository.existsByCustomer_IdAndEvent_Id(customerId, eventId)) {
	            throw new DuplicateProfileException(
	                "Already registered for this event");
	        }

	        User customer = userRepository.findById(customerId)
	            .orElseThrow(() -> new com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException("Customer not found"));

	        Event event = eventRepository.findById(eventId)
	            .orElseThrow(() -> new com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException("Event not found"));

	        // save Payment
	        Payment p = new Payment();
	        p.setCustomer(customer);
	        p.setEvent(event);
	        p.setAmount(dto.getAmount() != null ? dto.getAmount() : BigDecimal.ZERO);

	        // Payment mode mapping with default FREE
	        PaymentMode mode;
	        try {
	            mode = dto.getPaymentMode() != null
	                    ? PaymentMode.valueOf(dto.getPaymentMode().toUpperCase())
	                    : PaymentMode.FREE;
	        } catch (IllegalArgumentException ex) {
	            mode = PaymentMode.FREE;
	        }
	        
	        p.setMode(mode);

	        // If payment is not FREE, transactionId is required Added 10-08-2025
	        if (mode != PaymentMode.FREE && mode != PaymentMode.CASH) {
	            if (dto.getTransactionId() == null || dto.getTransactionId().isBlank()) {
	                throw new IllegalArgumentException("Transaction ID is required for paid events.");
	            }
	            p.setTransactionId(dto.getTransactionId().trim());
	        } else {
	            p.setTransactionId(null); // free events not storing transactionId
	        }
	        
	        p.setMode(mode);
	        p.setPaymentDate(java.time.LocalDateTime.now());

	        p = paymentRepository.save(p);

	        // Manual mapping to PaymentResponseDto
	        PaymentResponseDto res = new PaymentResponseDto();
	        res.setId(p.getId());
	        res.setCustomerId(p.getCustomer().getId());
	        res.setEventId(p.getEvent().getId());
	        res.setAmount(p.getAmount());
	        res.setPaymentDate(p.getPaymentDate());
	        res.setPaymentMode(p.getMode().name());
	        //Added 10-08-2025
	        res.setTransactionId(p.getTransactionId());

	        res.setCustomerName(p.getCustomer().getUsername());
	        res.setEventTitle(p.getEvent().getTitle());

	        return res;
	    }
	    
	    //
	    @Override
	    @Transactional(readOnly = true)
	    public List<PaymentResponseDto> getPaymentsByCustomer(Long customerId) {
	        List<Payment> payments = paymentRepository.findByCustomer_Id(customerId);

	        return payments.stream().map(p -> {
	            PaymentResponseDto dto = new PaymentResponseDto();
	            dto.setEventTitle(p.getEvent().getTitle());
	            dto.setPaymentMode(p.getMode().name());
	          //  dto.setRegistrationDate(p.getPaymentDate());
	            dto.setPaymentDate(p.getPaymentDate());   	            
	            return dto;
	        }).toList();
	    }
	    
	    
	    
}
