package com.cdac.eventnexus.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto extends BaseDTO {
    private Long customerId;
    private Long eventId;
    private LocalDateTime registrationDate;
    //08-08-2025 formatting date

    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String paymentMode;
    
    //Added 03-08-25
    private String customerName;
    private String eventTitle;
    
    //Added 10-08-25
    private String transactionId; 

}

