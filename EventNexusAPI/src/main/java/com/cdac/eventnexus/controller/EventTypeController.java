package com.cdac.eventnexus.controller;

import com.cdac.eventnexus.custom_exceptions.DuplicateProfileException;
import com.cdac.eventnexus.dto.EventTypeRequestDto;
import com.cdac.eventnexus.dto.EventTypeResponseDto;
import com.cdac.eventnexus.entities.EventType;
import com.cdac.eventnexus.service.EventTypeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-types")
@RequiredArgsConstructor
public class EventTypeController {

    private final EventTypeService eventTypeService;

  
    @PostMapping
    @Operation(summary = "Create a new event type")
    public ResponseEntity<EventTypeResponseDto> createEventType(@Valid @RequestBody EventTypeRequestDto dto) {
        if (eventTypeService.existsByName(dto.getName())) {
            //return ResponseEntity.status(HttpStatus.CONFLICT).build();
       
            throw new DuplicateProfileException("Event type with name '" + dto.getName() + "' already exists");

        }

        EventType entity = new EventType();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(true);

        EventType saved = eventTypeService.save(entity);
        return new ResponseEntity<>(toResponseDto(saved), HttpStatus.CREATED);
    }

  
    @GetMapping
    @Operation(summary = "Fetch all active event types")
    public ResponseEntity<List<EventTypeResponseDto>> getAllEventTypes() {
        List<EventType> types = eventTypeService.getAll();
        List<EventTypeResponseDto> dtoList = types.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event type by ID")
    public ResponseEntity<EventTypeResponseDto> getEventTypeById(@PathVariable Long id) {
        EventType entity = eventTypeService.getById(id);
        return ResponseEntity.ok(toResponseDto(entity));
    }

  
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event type by ID")
    public ResponseEntity<Void> deleteEventType(@PathVariable Long id) {
        eventTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search event types by name keyword")
    public ResponseEntity<List<EventTypeResponseDto>> searchByKeyword(@RequestParam String keyword) {
        List<EventType> matches = eventTypeService.searchByKeyword(keyword);
        List<EventTypeResponseDto> result = matches.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    private EventTypeResponseDto toResponseDto(EventType entity) {
        return EventTypeResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdOn(entity.getCreatedOn())
                .lastUpdatedOn(entity.getLastUpdatedOn())
                .build();
    }
}
