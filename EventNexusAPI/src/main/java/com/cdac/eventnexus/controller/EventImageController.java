package com.cdac.eventnexus.controller;

import com.cdac.eventnexus.dto.EventImageRequestDto;
import com.cdac.eventnexus.dto.EventImageResponseDto;
import com.cdac.eventnexus.entities.Event;
import com.cdac.eventnexus.entities.EventImage;
import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.EventImageRepository;
import com.cdac.eventnexus.dao.EventRepository;
import com.cdac.eventnexus.service.EventImageService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-images")
@RequiredArgsConstructor
public class EventImageController {

    private final EventImageService eventImageService;
    private final EventRepository eventRepository;

    // Create event image
    @PostMapping
    @Operation(summary = "Upload event image")
    public ResponseEntity<EventImageResponseDto> uploadImage(@Valid @RequestBody EventImageRequestDto dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + dto.getEventId()));

        EventImage entity = EventImage.builder()
                .url(dto.getUrl())
                .event(event)
                .build();

        EventImage saved = eventImageService.save(entity);

        EventImageResponseDto responseDto = EventImageResponseDto.builder()
                .id(saved.getId())
                .url(saved.getUrl())
                .eventId(saved.getEvent().getId())
                .createdOn(saved.getCreatedOn())
                .lastUpdatedOn(saved.getLastUpdatedOn())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // Get all event images
    @GetMapping
    @Operation(summary = "Fetch all event images")
    public ResponseEntity<List<EventImageResponseDto>> getAllImages() {
        List<EventImageResponseDto> images = eventImageService.getAll()
                .stream()
                .map(img -> EventImageResponseDto.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .eventId(img.getEvent().getId())
                        .createdOn(img.getCreatedOn())
                        .lastUpdatedOn(img.getLastUpdatedOn())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(images);
    }

    // Get image by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get event image by ID")
    public ResponseEntity<EventImageResponseDto> getImageById(@PathVariable Long id) {
        EventImage image = eventImageService.getById(id);
        if (image == null) {
            throw new ResourceNotFoundException("Image not found with ID: " + id);
        }

        EventImageResponseDto dto = EventImageResponseDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .eventId(image.getEvent().getId())
                .createdOn(image.getCreatedOn())
                .lastUpdatedOn(image.getLastUpdatedOn())
                .build();

        return ResponseEntity.ok(dto);
    }

    // Delete image by ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event image by ID")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        eventImageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
