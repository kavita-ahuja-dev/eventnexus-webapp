package com.cdac.eventnexus.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.cdac.eventnexus.custom_exceptions.DuplicateProfileException;
import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.EventRepository;
import com.cdac.eventnexus.dao.FeedbackRepository;
import com.cdac.eventnexus.dao.PaymentRepository;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.FeedbackRequestDto;
import com.cdac.eventnexus.dto.FeedbackResponseDto;
import com.cdac.eventnexus.entities.Event;
import com.cdac.eventnexus.entities.Feedback;
import com.cdac.eventnexus.entities.User;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
@Validated
public class FeedbackServiceImpl implements FeedbackService {

    
    private FeedbackRepository feedbackRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    @Override
    public FeedbackResponseDto createFeedback(FeedbackRequestDto dto) {
    	
    	 	Long userId = dto.getUserId();
    	    Long eventId = dto.getEventId();

    	
    	// Check if user is registered for this event
        if (!paymentRepository.existsByCustomer_IdAndEvent_Id(userId, eventId)) {
            throw new DuplicateProfileException(
                "You must be registered for this event to submit feedback");
        }
        
        if (feedbackRepository.existsByUser_IdAndEvent_Id(userId, eventId)) {
            throw new DuplicateProfileException("You already submitted feedback for this event.");
        }

        
        //working code below with event_registration table
    	
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + eventId));

        // Create entity manually
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setEvent(event);
        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());
        feedback.setActive(true);

        // Save to DB
        Feedback saved = feedbackRepository.save(feedback);

        // Map entity -> response manually
        FeedbackResponseDto res = new FeedbackResponseDto();
        res.setId(saved.getId()); // from BaseDTO
        res.setUserId(saved.getUser().getId());
        res.setEventId(saved.getEvent().getId());
        res.setComment(saved.getComment());
        res.setRating(saved.getRating());
        res.setCreatedOn(saved.getCreatedOn());
        res.setLastUpdatedOn(saved.getLastUpdatedOn());

        return res;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FeedbackResponseDto> getAllFeedbacks() {
        return feedbackRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public FeedbackResponseDto getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found: " + id));
        return mapToResponse(feedback);
    }

    @Transactional
    @Override
    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found: " + id));
        feedback.setActive(false);
    }

    private FeedbackResponseDto mapToResponse(Feedback feedback) {
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId(feedback.getId());
        dto.setUserId(feedback.getUser().getId());
        dto.setEventId(feedback.getEvent().getId());
        dto.setComment(feedback.getComment());
        dto.setRating(feedback.getRating());
        return dto;
    }
}


