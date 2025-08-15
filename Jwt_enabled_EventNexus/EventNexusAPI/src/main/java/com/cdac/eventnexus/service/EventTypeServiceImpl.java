package com.cdac.eventnexus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.EventTypeRepository;
import com.cdac.eventnexus.entities.EventType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class EventTypeServiceImpl implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    @Override
    public EventType save(EventType eventtype) {
        return eventTypeRepository.save(eventtype);
    }

    @Override
    public EventType getById(Long id) {
        return eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Type not found with ID: " + id));
    }


    @Override
    public List<EventType> getAll() {
        return eventTypeRepository.findByIsActiveTrue(); 
    }

    @Override
    public void deleteById(Long id) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete: Event Type not found with ID: " + id));

        eventType.setActive(false); 
        eventTypeRepository.save(eventType);
    }
    
    @Override
    public boolean existsByName(String name) {
        return eventTypeRepository.existsByName(name);
    }

    @Override
    public List<EventType> searchByKeyword(String keyword) {
        return eventTypeRepository.findByNameContainingIgnoreCase(keyword);
    }
}
