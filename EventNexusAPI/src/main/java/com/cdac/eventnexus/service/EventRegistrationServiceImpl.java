package com.cdac.eventnexus.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.cdac.eventnexus.dao.CustomerRepository;
import com.cdac.eventnexus.dao.EventRegistrationRepository;
import com.cdac.eventnexus.dao.EventRepository;
import com.cdac.eventnexus.dto.EventRegistrationRequestDto;
import com.cdac.eventnexus.dto.EventRegistrationResponseDto;
import com.cdac.eventnexus.entities.Customer;
import com.cdac.eventnexus.entities.Event;
import com.cdac.eventnexus.entities.EventRegistration;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class EventRegistrationServiceImpl implements EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;

    @Override
    public EventRegistrationResponseDto register(EventRegistrationRequestDto request) {
        // Basic null guard (keeps 500s away from JPA)
        if (request.getEventId() == null || request.getCustomerId() == null) {
            throw new IllegalArgumentException("Event ID and Customer ID must not be null");
        }

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        EventRegistration registration = new EventRegistration();
        registration.setEvent(event);
        registration.setCustomer(customer);             // <-- Customer here
        registration.setPaymentMode(request.getPaymentMode());
        registration.setRegisteredOn(LocalDate.now());  // optional if you track createdOn in BaseEntity

        EventRegistration saved = registrationRepository.save(registration);

        return EventRegistrationResponseDto.builder()
                .registrationId(saved.getId())
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .customerId(customer.getId())
                .customerName(customer.getFirstName())
                .paymentMode(saved.getPaymentMode())
                .registeredOn(saved.getRegisteredOn() != null ? saved.getRegisteredOn()
                                                             : LocalDate.now())
                .build();
    }

    @Override
    public List<EventRegistrationResponseDto> getAllRegistrations() {
        return registrationRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
    

    private EventRegistrationResponseDto toResponseDto(EventRegistration reg) {
        return EventRegistrationResponseDto.builder()
                .registrationId(reg.getId())
                .eventId(reg.getEvent().getId())
                .eventTitle(reg.getEvent().getTitle())
                .customerId(reg.getCustomer().getId())               
                .customerName(reg.getCustomer().getFirstName())      
                .paymentMode(reg.getPaymentMode())
                .registeredOn(reg.getRegisteredOn() != null ? reg.getRegisteredOn()
                                                            : LocalDate.now())
                .build();
    }

    @Override
    public List<EventRegistrationResponseDto> getRegistrationsByCustomer(Long customerId) {
        return registrationRepository.findByCustomerId(customerId)
            .stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }

}





