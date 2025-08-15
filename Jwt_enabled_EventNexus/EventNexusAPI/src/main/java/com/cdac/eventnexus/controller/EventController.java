package com.cdac.eventnexus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cdac.eventnexus.dao.PaymentRepository;
import com.cdac.eventnexus.dto.EventRequestDto;
import com.cdac.eventnexus.dto.EventResponseDto;
import com.cdac.eventnexus.entities.Event;
import com.cdac.eventnexus.entities.Payment;
import com.cdac.eventnexus.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Validated

public class EventController {

    private final EventService eventService;
    private final PaymentRepository paymentRepository;


    @Operation(summary = "Create a new Event")
    @PreAuthorize("hasAnyRole('EXHIBITOR','ADMIN')")
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody @Validated EventRequestDto dto) {
        EventResponseDto created = eventService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @Operation(summary = "Get event by ID")
    @PreAuthorize("hasAnyRole('CUSTOMER','EXHIBITOR','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getById(id);
        return ResponseEntity.ok(event);
    }

//    @Operation(summary = "Get all active events")
//    @GetMapping
//    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
//        List<EventResponseDto> events = eventService.getAll();
//        return ResponseEntity.ok(events);
//    }
    
    @Operation(summary = "Get all events with optional filtering by typeId or exhibitorId")
    @PreAuthorize("hasAnyRole('CUSTOMER','EXHIBITOR','ADMIN')")
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEvents(
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Long exhibitorId) {

        List<EventResponseDto> events;

        if (typeId == null && exhibitorId == null) {
            events = eventService.getAll(); // get all active events
        } else {
            events = eventService.getFilteredEvents(typeId, exhibitorId);
        }

        return ResponseEntity.ok(events);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete event by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    

    @Operation(summary = "Search events by partial title")
    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDto>> searchEventsByTitle(
            @RequestParam String title) {
        List<EventResponseDto> events = eventService.searchByTitle(title);
        return ResponseEntity.ok(events);
    }
    
    //without using event_registration table

    @PreAuthorize("hasAnyRole('CUSTOMER','EXHIBITOR','ADMIN')")
    @GetMapping("/my/{customerId}")
    public List<Event> getMyEvents(@PathVariable Long customerId) {
      return paymentRepository.findAllByCustomer_Id(customerId)
          .stream().map(Payment::getEvent).distinct().toList();
    }
    
    //Added 08-08-2025
    
    @PreAuthorize("hasAnyRole('EXHIBITOR','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody @Valid EventRequestDto dto) {
        if (dto.getId() != null && !dto.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(eventService.update(dto));
    }

    
    
    
    
}
