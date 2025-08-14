package com.cdac.eventnexus.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventTypeResponseDto extends BaseDTO {
	
	//Added id on 04-08-2025
	private Long id;  
	 
    private String name;
    private String description;
    
    private LocalDate createdOn;        // BaseEntity
    private LocalDateTime lastUpdatedOn; // BaseEntity
}