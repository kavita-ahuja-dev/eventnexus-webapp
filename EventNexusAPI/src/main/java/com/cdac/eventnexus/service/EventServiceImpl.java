package com.cdac.eventnexus.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.EventImageRepository;
import com.cdac.eventnexus.dao.EventRepository;
import com.cdac.eventnexus.dao.EventTypeRepository;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.EventRequestDto;
import com.cdac.eventnexus.dto.EventResponseDto;
import com.cdac.eventnexus.entities.Event;
import com.cdac.eventnexus.entities.EventImage;
import com.cdac.eventnexus.entities.EventType;
import com.cdac.eventnexus.entities.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class EventServiceImpl implements EventService {

	 	private final EventRepository eventRepository;
	    private final EventTypeRepository eventTypeRepository;
	    private final UserRepository userRepository;
	    private final EventImageRepository eventImageRepository;

	    private final ModelMapper modelMapper;
	    
	    @Override
	    public List<Event> findByIsActiveTrue() {
	        return eventRepository.findByIsActiveTrue();
	    }
   	    
	    @Override
	    public EventResponseDto save(EventRequestDto dto) {
	    	
	    	 Event event = new Event();

	    	    event.setTitle(dto.getTitle());
	    	    event.setDescription(dto.getDescription());
	    	    event.setDate(dto.getDate());
	    	    event.setPrice(dto.getPrice());
	    	    event.setYear(dto.getYear());
	    	    event.setMode(dto.getMode());
	    	    event.setZoomUrl(dto.getZoomUrl());
	    	    event.setAddress(dto.getAddress());
	    	    event.setLatitude(dto.getLatitude());
	    	    event.setLongitude(dto.getLongitude());
	    	    event.setMapUrl(dto.getMapUrl());
	    	    event.setActive(true);

	    	    EventType type = eventTypeRepository.findById(dto.getTypeId())
	    	        .orElseThrow(() -> new ResourceNotFoundException("Invalid EventType ID"));
	    	    event.setType(type);

	    	    User exhibitor = userRepository.findById(dto.getExhibitorId())
	    	        .orElseThrow(() -> new ResourceNotFoundException("Invalid Exhibitor ID"));
	    	    event.setExhibitor(exhibitor);	    	

	    	    Event saved = eventRepository.save(event);
	    	    
	    	    if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
	    	        EventImage image = new EventImage();
	    	        image.setUrl(dto.getImageUrl().trim());
	    	        image.setEvent(saved); // 
	    	        eventImageRepository.save(image); // Save image after setting FK

	    	        // 3. Set image back to event (optional, if bidirectional or needed later)
	    	       // saved.setImage(image);
	    	    }
	    	        EventResponseDto response = new EventResponseDto();
	    	        response.setId(saved.getId());
	    	        response.setTitle(saved.getTitle());
	    	        response.setDescription(saved.getDescription());
	    	        response.setDate(saved.getDate());
	    	        response.setPrice(saved.getPrice());
	    	        response.setYear(saved.getYear());
	    	        response.setMode(saved.getMode());
	    	        response.setZoomUrl(saved.getZoomUrl());
	    	        response.setAddress(saved.getAddress());
	    	        response.setLatitude(saved.getLatitude());
	    	        response.setLongitude(saved.getLongitude());
	    	        response.setMapUrl(saved.getMapUrl());
	    	        response.setTypeId(type.getId());
	    	        response.setEventTypeName(type.getName());
	    	        response.setExhibitorId(exhibitor.getId());
	    	        response.setExhibitorName(exhibitor.getUsername());

//	    	        // Include image URL in response if available
//	    	        if (saved.getImage() != null) {
//	    	            response.setImageUrl(saved.getImage().getUrl());
//	    	        }

	    	        return response;
	    	}
	    	
	    
	    @Override
	    public EventResponseDto getById(Long id) {
	        Event event = eventRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

	        EventResponseDto dto = modelMapper.map(event, EventResponseDto.class);
	        dto.setEventTypeName(event.getType().getName());
	        dto.setExhibitorName(event.getExhibitor().getUsername());
	        return dto;
	    }

	    @Override
	    public List<EventResponseDto> getAll() {
	        return eventRepository.findByIsActiveTrue()
	                .stream()
	                .map(event -> {
	                    EventResponseDto dto = modelMapper.map(event, EventResponseDto.class);
	                    dto.setEventTypeName(event.getType().getName());
	                    dto.setExhibitorName(event.getExhibitor().getUsername());
	                    return dto;
	                })
	                .collect(Collectors.toList());
	    }
   
	    @Override
	    public void deleteById(Long id) {
	        Event event = eventRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

	        event.setActive(false); 
	        eventRepository.save(event);
	    }
	    
	    @Override
	    public List<EventResponseDto> getFilteredEvents(Long typeId, Long exhibitorId) {
	        List<Event> filtered;

	        if (typeId != null && exhibitorId != null) {
	            filtered = eventRepository.findByTypeIdAndExhibitorIdAndIsActiveTrue(typeId, exhibitorId);
	        } else if (typeId != null) {
	            filtered = eventRepository.findByTypeIdAndIsActiveTrue(typeId);
	        } else if (exhibitorId != null) {
	            filtered = eventRepository.findByExhibitorIdAndIsActiveTrue(exhibitorId);
	        } else {
	            filtered = eventRepository.findByIsActiveTrue();
	        }

	        return filtered.stream().map(event -> {
	            EventResponseDto dto = modelMapper.map(event, EventResponseDto.class);
	            dto.setEventTypeName(event.getType().getName());
	            dto.setExhibitorName(event.getExhibitor().getUsername());
	            return dto;
	        }).collect(Collectors.toList());
	    }

	    @Override
	    public List<EventResponseDto> searchByTitle(String title) {
	        return eventRepository.findByTitleContainingIgnoreCaseAndIsActiveTrue(title)
	                .stream()
	                .map(event -> {
	                    EventResponseDto dto = modelMapper.map(event, EventResponseDto.class);
	                    dto.setEventTypeName(event.getType().getName());
	                    dto.setExhibitorName(event.getExhibitor().getUsername());
	                    return dto;
	                }).collect(Collectors.toList());
	    }

	    @Override
	    @Transactional
	    public EventResponseDto update(EventRequestDto dto) {
	        if (dto.getId() == null) {
	            throw new IllegalArgumentException("Event id is required for update");
	        }

	        Event event = eventRepository.findById(dto.getId())
	            .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + dto.getId()));

	        if (dto.getTitle() != null) event.setTitle(dto.getTitle().trim());
	        if (dto.getDescription() != null) event.setDescription(dto.getDescription().trim());
	        if (dto.getDate() != null) event.setDate(dto.getDate());
	        if (dto.getPrice() != null) event.setPrice(dto.getPrice());
	        if (dto.getYear() != null) event.setYear(dto.getYear());
	        if (dto.getMode() != null) event.setMode(dto.getMode());
	        if (dto.getZoomUrl() != null) event.setZoomUrl(dto.getZoomUrl());
	        if (dto.getAddress() != null) event.setAddress(dto.getAddress());
	        if (dto.getLatitude() != null) event.setLatitude(dto.getLatitude());
	        if (dto.getLongitude() != null) event.setLongitude(dto.getLongitude());
	        if (dto.getMapUrl() != null) event.setMapUrl(dto.getMapUrl());

	        // Relationships via IDs
	        if (dto.getTypeId() != null) {
	            EventType type = eventTypeRepository.findById(dto.getTypeId())
	                .orElseThrow(() -> new ResourceNotFoundException("EventType not found: " + dto.getTypeId()));
	            event.setType(type);
	        }
	        if (dto.getExhibitorId() != null) {
	            User exhibitor = userRepository.findById(dto.getExhibitorId())
	                .orElseThrow(() -> new ResourceNotFoundException("Exhibitor not found: " + dto.getExhibitorId()));
	            event.setExhibitor(exhibitor);
	        }

	        // Image handling
//	        if (dto.getImageUrl() != null) {
//	            String img = dto.getImageUrl();
//	            if (img.isBlank()) {
//	                if (event.getEventImage() != null) {
//	                    eventImageRepository.delete(event.getEventImage());
//	                    event.setEventImage(null);
//	                }
//	            } else {
//	                if (event.getEventImage() == null) {
//	                    EventImage ei = new EventImage();
//	                    ei.setImageUrl(img.trim());
//	                    ei.setEvent(event); // set FK side
//	                    event.setEventImage(ei);
//	                } else {
//	                    event.getEventImage().setImageUrl(img.trim());
//	                }
//	            }
//	        }

	        Event saved = eventRepository.save(event);

	        EventResponseDto res = new EventResponseDto();
	        res.setId(saved.getId());
	        res.setTitle(saved.getTitle());
	        res.setDescription(saved.getDescription());
	        res.setDate(saved.getDate());
	        res.setPrice(saved.getPrice());
	        res.setYear(saved.getYear());
	        res.setMode(saved.getMode());
	        res.setZoomUrl(saved.getZoomUrl());
	        res.setAddress(saved.getAddress());
	        res.setLatitude(saved.getLatitude());
	        res.setLongitude(saved.getLongitude());
	        res.setMapUrl(saved.getMapUrl());
	        res.setTypeId(saved.getType() != null ? saved.getType().getId() : null);
	        res.setExhibitorId(saved.getExhibitor() != null ? saved.getExhibitor().getId() : null);
	       // res.setImageUrl(saved.getEventImage() != null ? saved.getEventImage().getImageUrl() : null);

	        return res;
	    }

}
