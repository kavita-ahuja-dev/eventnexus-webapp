package com.cdac.eventnexus.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class EventRegistrationResponseDto {
    private Long registrationId;
    private String customerName;
    private String eventTitle;
    private String paymentMode;
    private LocalDate registeredOn;
    
   // private Long id;
    private Long eventId;
    private Long customerId;
   
}

