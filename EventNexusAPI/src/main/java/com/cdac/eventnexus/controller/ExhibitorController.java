package com.cdac.eventnexus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.eventnexus.dto.ExhibitorRequestDto;
import com.cdac.eventnexus.dto.ExhibitorResponseDto;
import com.cdac.eventnexus.dto.ExhibitorSummaryDto;
import com.cdac.eventnexus.service.ExhibitorService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exhibitors")
@RequiredArgsConstructor
public class ExhibitorController {

    private final ExhibitorService exhibitorService;

    @Operation(summary = "Create new exhibitor profile")
    @PostMapping
    public ResponseEntity<ExhibitorResponseDto> createExhibitor(@Valid @RequestBody ExhibitorRequestDto dto) {
        return new ResponseEntity<>(exhibitorService.createExhibitor(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all exhibitor profiles")
    @GetMapping
    public ResponseEntity<List<ExhibitorResponseDto>> getAll() {
        return ResponseEntity.ok(exhibitorService.getAllExhibitors());
    }

    @Operation(summary = "Get exhibitor by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ExhibitorResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(exhibitorService.getExhibitorById(id));
    }

    @Operation(summary = "delete exhibitor by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exhibitorService.deleteExhibitor(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/dashboard/{exhibitorId}")
    public ExhibitorSummaryDto getDashboard(@PathVariable Long exhibitorId) {
        return exhibitorService.getSummary(exhibitorId);
    }
}
