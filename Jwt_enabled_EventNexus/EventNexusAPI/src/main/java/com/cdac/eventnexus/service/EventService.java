package com.cdac.eventnexus.service;

import com.cdac.eventnexus.dto.EventRequestDto;
import com.cdac.eventnexus.dto.EventResponseDto;
import com.cdac.eventnexus.entities.Event;
import java.util.List;

public interface EventService {
	
    List<Event> findByIsActiveTrue();

	EventResponseDto save(EventRequestDto dto);
    EventResponseDto getById(Long id);
    List<EventResponseDto> getAll();
    void deleteById(Long id);
    
   //04-08-2025
    List<EventResponseDto> getFilteredEvents(Long typeId, Long exhibitorId);
    List<EventResponseDto> searchByTitle(String title);
    
    //update events
    EventResponseDto update(EventRequestDto dto);


}
