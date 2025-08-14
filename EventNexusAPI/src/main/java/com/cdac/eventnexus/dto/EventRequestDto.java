package com.cdac.eventnexus.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cdac.eventnexus.entities.EventMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestDto {
	//added 07-08-2025 testing
	private Long id;
    private String title;
    private String description;
    private LocalDate date;
    //modified  venue to location 02-08-25
   // private String venue;
    
    private BigDecimal price;
    private Integer year;
    
    // ONLINE or OFFLINE
    private EventMode mode;
    
    // For ONLINE only
    private String zoomUrl;
    
    // For OFFLINE only
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String mapUrl;
    
    private Long typeId;
 
    // Added 02-08-2025
    private Long exhibitorId;
    
    //
    // EventRequestDto.java
    private String imageUrl;

  
}