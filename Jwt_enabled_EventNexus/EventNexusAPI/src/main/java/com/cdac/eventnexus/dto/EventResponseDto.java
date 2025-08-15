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
public class EventResponseDto extends BaseDTO {
	private String title;
    private String description;
    private LocalDate date;
    private String venue;
    private BigDecimal price;
    private Integer year;
    private EventMode mode;
    private String zoomUrl;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String mapUrl;
    
    private Long typeId;
    private Long exhibitorId;
    
    private String eventTypeName;
    private String exhibitorName;
    
    //Added 07-08-2025
    private Long id;
    //
    private String imageUrl;
    
}

