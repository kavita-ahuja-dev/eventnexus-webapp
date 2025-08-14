package com.cdac.eventnexus.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.cdac.eventnexus.custom_exceptions.DuplicateProfileException;
import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.EventRepository;
import com.cdac.eventnexus.dao.ExhibitorRepository;
import com.cdac.eventnexus.dao.PaymentRepository;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.ExhibitorRequestDto;
import com.cdac.eventnexus.dto.ExhibitorResponseDto;
import com.cdac.eventnexus.dto.ExhibitorSummaryDto;
import com.cdac.eventnexus.entities.Exhibitor;
import com.cdac.eventnexus.entities.User;
import com.cdac.eventnexus.entities.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class ExhibitorServiceImpl implements ExhibitorService {

    
    private final ExhibitorRepository exhibitorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public ExhibitorResponseDto createExhibitor(ExhibitorRequestDto dto) {
        
        Long userId = dto.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 
        if (user.getRole() != UserRole.EXHIBITOR) {
            throw new IllegalStateException("User is not assigned role EXHIBITOR.");
        }

        // 
        if (exhibitorRepository.existsById(userId)) {
           // throw new IllegalStateException("Exhibitor profile already exists for this user.");
            throw new DuplicateProfileException("Exhibitor profile already exists for this user.");

        }
        

        // Manual mapping due to @MapsId
        Exhibitor exhibitor = new Exhibitor();
        exhibitor.setUser(user);
        exhibitor.setCompanyName(dto.getCompanyName());
        exhibitor.setContactInfo(dto.getContactInfo());
        exhibitor.setActive(true);

        Exhibitor saved = exhibitorRepository.save(exhibitor);

        ExhibitorResponseDto response = modelMapper.map(saved, ExhibitorResponseDto.class);
        response.setUserId(user.getId());
        return response;
    }

    @Override
    public ExhibitorResponseDto getExhibitorById(Long id) {
        Exhibitor exhibitor = exhibitorRepository.findById(id)
                .filter(Exhibitor::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Exhibitor not found"));

        ExhibitorResponseDto dto = modelMapper.map(exhibitor, ExhibitorResponseDto.class);
        dto.setUserId(exhibitor.getUser().getId());
        return dto;
    }

    @Override
    public List<ExhibitorResponseDto> getAllExhibitors() {
        return exhibitorRepository.findByIsActiveTrue()
                .stream()
                .map(exhibitor -> {
                    ExhibitorResponseDto dto = modelMapper.map(exhibitor, ExhibitorResponseDto.class);
                    dto.setUserId(exhibitor.getUser().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExhibitor(Long id) {
        Exhibitor exhibitor = exhibitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exhibitor not found"));
        exhibitor.setActive(false);
        exhibitorRepository.save(exhibitor);
    }

	@Override
	public ExhibitorSummaryDto getSummary(Long exhibitorId) {
		long myEvents        = eventRepository.countByExhibitor_Id(exhibitorId);
	    long myRegistrations = paymentRepository.countByEvent_Exhibitor_Id(exhibitorId);
	    long myCustomers     = paymentRepository.countDistinctCustomersByExhibitor(exhibitorId);

	    return ExhibitorSummaryDto.builder()
	            .myEvents(myEvents)
	            .myRegistrations(myRegistrations)
	            .myCustomers(myCustomers)
	            .build();
	}

}
