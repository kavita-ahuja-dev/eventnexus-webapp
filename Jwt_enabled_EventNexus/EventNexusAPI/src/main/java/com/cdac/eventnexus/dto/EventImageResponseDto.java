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
public class EventImageResponseDto extends BaseDTO {
	
	private Long id;
    private LocalDate createdOn;
    private LocalDateTime lastUpdatedOn;
	
    private String url;
    private Long eventId;
}
