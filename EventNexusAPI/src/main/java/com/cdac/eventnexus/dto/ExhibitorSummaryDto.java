package com.cdac.eventnexus.dto;

import lombok.Data;
import lombok.Builder;


@Data
@Builder
public class ExhibitorSummaryDto {
 private long myEvents;          
 private long myCustomers;       
 private long myRegistrations;   
}
