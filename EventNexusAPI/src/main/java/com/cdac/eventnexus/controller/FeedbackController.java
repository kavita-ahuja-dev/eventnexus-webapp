package com.cdac.eventnexus.controller;

import com.cdac.eventnexus.dto.FeedbackRequestDto;
import com.cdac.eventnexus.dto.FeedbackResponseDto;
import com.cdac.eventnexus.service.FeedbackService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Create new feedback")
    public ResponseEntity<FeedbackResponseDto> createFeedback(@Valid @RequestBody FeedbackRequestDto dto) {
        FeedbackResponseDto saved = feedbackService.createFeedback(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Fetch all active feedbacks")
    public ResponseEntity<List<FeedbackResponseDto>> getAllFeedbacks() {
        List<FeedbackResponseDto> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID")
    public ResponseEntity<FeedbackResponseDto> getFeedbackById(@PathVariable Long id) {
        FeedbackResponseDto dto = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete feedback by ID")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}

