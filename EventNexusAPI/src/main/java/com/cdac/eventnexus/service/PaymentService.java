package com.cdac.eventnexus.service;

import com.cdac.eventnexus.dto.PaymentRequestDto;
import com.cdac.eventnexus.dto.PaymentResponseDto;
import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto dto);
    PaymentResponseDto register(PaymentRequestDto dto);

    List<PaymentResponseDto> getAllPayments();
    PaymentResponseDto getPaymentById(Long id);
    void deletePayment(Long id);
    //
    List<PaymentResponseDto> getPaymentsByCustomer(Long customerId); 
}
