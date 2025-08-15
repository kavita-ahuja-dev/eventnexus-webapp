package com.cdac.eventnexus.service;

import java.util.List;

import com.cdac.eventnexus.dto.ExhibitorRequestDto;
import com.cdac.eventnexus.dto.ExhibitorResponseDto;
import com.cdac.eventnexus.dto.ExhibitorSummaryDto;

public interface ExhibitorService {
	ExhibitorResponseDto createExhibitor(ExhibitorRequestDto dto);
    ExhibitorResponseDto getExhibitorById(Long id);
    List<ExhibitorResponseDto> getAllExhibitors();
    void deleteExhibitor(Long id);
    
    //Added 08-08-2025
    ExhibitorSummaryDto getSummary(Long exhibitorId);


}
