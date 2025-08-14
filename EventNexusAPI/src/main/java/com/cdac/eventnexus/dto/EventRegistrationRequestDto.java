package com.cdac.eventnexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@NoArgsConstructor 
@AllArgsConstructor

public class EventRegistrationRequestDto {
    private Long eventId;
    private Long customerId;
    private String paymentMode;
    
}
