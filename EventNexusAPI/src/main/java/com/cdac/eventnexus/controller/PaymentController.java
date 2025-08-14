package com.cdac.eventnexus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.eventnexus.dto.PaymentRequestDto;
import com.cdac.eventnexus.dto.PaymentResponseDto;
import com.cdac.eventnexus.entities.PaymentMode;
import com.cdac.eventnexus.service.CustomerService;
import com.cdac.eventnexus.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;

	@GetMapping("/payment-modes")
    public List<String> getPaymentModes() {
		List<String> modes = new ArrayList<>();
	    for (PaymentMode pm : PaymentMode.values()) {
	        modes.add(pm.name());
	    }
	    return modes;
    }
	
	@GetMapping("/my/{customerId}")
    @Operation(summary = "List my registrations (payments as registrations)")
    public ResponseEntity<List<PaymentResponseDto>> getMyRegistrations(@PathVariable Long customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(customerId));
    }
	

}
