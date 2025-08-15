package com.cdac.eventnexus.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {
    private Long customerId;
    private Long eventId;
    private BigDecimal amount;
    private String paymentMode;
    
    //Added 03-08-25
    private String customerName;
    private String eventTitle;
    
    //Added 10-08-25  
    private String transactionId; 

}
