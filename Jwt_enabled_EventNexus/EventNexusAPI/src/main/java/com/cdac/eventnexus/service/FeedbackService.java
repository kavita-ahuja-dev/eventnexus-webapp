package com.cdac.eventnexus.service;

import com.cdac.eventnexus.dto.FeedbackRequestDto;
import com.cdac.eventnexus.dto.FeedbackResponseDto;
import java.util.List;

public interface FeedbackService {
    FeedbackResponseDto createFeedback(FeedbackRequestDto dto);
    List<FeedbackResponseDto> getAllFeedbacks();
    FeedbackResponseDto getFeedbackById(Long id);
    void deleteFeedback(Long id);
}
