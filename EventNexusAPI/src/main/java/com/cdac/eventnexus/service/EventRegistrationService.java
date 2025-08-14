package com.cdac.eventnexus.service;

import java.util.List;

import com.cdac.eventnexus.dto.EventRegistrationRequestDto;
import com.cdac.eventnexus.dto.EventRegistrationResponseDto;

public interface EventRegistrationService {
	EventRegistrationResponseDto register(EventRegistrationRequestDto request);
	List<EventRegistrationResponseDto> getAllRegistrations();
	List<EventRegistrationResponseDto> getRegistrationsByCustomer(Long customerId);
	

}
