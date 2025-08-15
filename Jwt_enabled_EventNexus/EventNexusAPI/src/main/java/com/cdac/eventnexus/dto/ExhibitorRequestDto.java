package com.cdac.eventnexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExhibitorRequestDto  {
	
	private Long userId; 
    private String companyName;
    private String contactInfo;
}
