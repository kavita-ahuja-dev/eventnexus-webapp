package com.cdac.eventnexus.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"eventTitle", "exhibitorName"})
public class OfferResponseDto extends BaseDTO {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long eventId;
    
  //added 2-8-25
    private Long exhibitorId;
    
 // Needed for display 
    private String exhibitorName;
    private String eventTitle;


}
