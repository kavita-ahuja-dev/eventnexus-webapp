package com.cdac.eventnexus.dto;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferRequestDto {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long eventId;
    //added 02-08-2025
    private Long exhibitorId; 
    
}

