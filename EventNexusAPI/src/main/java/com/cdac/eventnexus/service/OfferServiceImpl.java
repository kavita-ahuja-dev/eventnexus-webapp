package com.cdac.eventnexus.service;

import com.cdac.eventnexus.custom_exceptions.ApiException;
import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.EventRepository;
import com.cdac.eventnexus.dao.OfferRepository;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.OfferRequestDto;
import com.cdac.eventnexus.dto.OfferResponseDto;
import com.cdac.eventnexus.entities.Event;
import com.cdac.eventnexus.entities.Offer;
import com.cdac.eventnexus.entities.User;

import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
    
    @Service
    @Transactional
    @AllArgsConstructor
    @Validated
    public class OfferServiceImpl implements OfferService {

        private final OfferRepository offerRepository;
        private final EventRepository eventRepository;
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;
        
       
        @Override
        public String updateOfferEndDateDesc(Long offerId, OfferRequestDto dto) {
            int updated = offerRepository.updateOfferEndDateDesc(dto.getDescription(), dto.getEndDate(), offerId);

            if (updated == 0) {
                throw new ResourceNotFoundException("No offer found with ID: " + offerId);
            }

            return "Offer updated successfully.";
        }

        @Override
        public OfferResponseDto createOffer(OfferRequestDto dto) {
        	
        	// Check for duplicate offer (one offer per exhibitor per event)
            if (offerRepository.existsByEvent_IdAndExhibitor_Id(dto.getEventId(), dto.getExhibitorId())) {
                throw new ApiException("Offer already exists for this event by this exhibitor.");
            }
        	
            // Map OfferRequestDto to Offer entity
            Offer offer = modelMapper.map(dto, Offer.class);

            // Set associated Event (throws if not found)
            Event event = eventRepository.findById(dto.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid Event ID"));
            offer.setEvent(event);

            // Set associated Exhibitor (throws if not found)
            User exhibitor = userRepository.findById(dto.getExhibitorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid Exhibitor ID"));
            offer.setExhibitor(exhibitor);

            // Save and return mapped DTO
            Offer saved = offerRepository.save(offer);
            
            OfferResponseDto response = modelMapper.map(saved, OfferResponseDto.class);
            response.setExhibitorName(exhibitor.getUsername());
            response.setEventTitle(event.getTitle());

            return response;
        }

        @Override
        public List<OfferResponseDto> getAllOffers() {
        	return offerRepository.findByIsActiveTrue().stream()
                    .map(offer -> {
                        OfferResponseDto dto = modelMapper.map(offer, OfferResponseDto.class);
                        dto.setEventTitle(offer.getEvent().getTitle());
                        dto.setExhibitorName(offer.getExhibitor().getUsername());
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        @Override
        public OfferResponseDto getOfferById(Long id) {
        	Offer offer = offerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
            OfferResponseDto dto = modelMapper.map(offer, OfferResponseDto.class);
            dto.setEventTitle(offer.getEvent().getTitle());
            dto.setExhibitorName(offer.getExhibitor().getUsername());
            return dto;
        }

        @Override
        public void deleteOffer(Long id) {
        	 Offer offer = offerRepository.findById(id)
                     .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
             offer.setActive(false);
             offerRepository.save(offer);
        }

		@Override
		public List<OfferResponseDto> getOffersByEventId(Long eventId) {
			
			  return offerRepository.findByEvent_IdAndIsActiveTrue(eventId).stream()
		                .map(offer -> {
		                    OfferResponseDto dto = modelMapper.map(offer, OfferResponseDto.class);
		                    dto.setEventTitle(offer.getEvent().getTitle());
		                    dto.setExhibitorName(offer.getExhibitor().getUsername());
		                    return dto;
		                })
		                .collect(Collectors.toList());
		}

		@Override
		public List<OfferResponseDto> getOffersByExhibitorId(Long exhibitorId) {
			return offerRepository.findByExhibitor_IdAndIsActiveTrue(exhibitorId).stream()
	                .map(offer -> {
	                    OfferResponseDto dto = modelMapper.map(offer, OfferResponseDto.class);
	                    dto.setEventTitle(offer.getEvent().getTitle());
	                    dto.setExhibitorName(offer.getExhibitor().getUsername());
	                    return dto;
	                })
	                .collect(Collectors.toList());
		}

		@Override
		public OfferResponseDto updateOffer(Long id, OfferRequestDto dto) {

			 Offer offer = offerRepository.findById(id)
		                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));

		        offer.setTitle(dto.getTitle());
		        offer.setDescription(dto.getDescription());
		        offer.setStartDate(dto.getStartDate());
		        offer.setEndDate(dto.getEndDate());


		        Offer updated = offerRepository.save(offer);
		        OfferResponseDto response = modelMapper.map(updated, OfferResponseDto.class);
		        response.setEventTitle(updated.getEvent().getTitle());
		        response.setExhibitorName(updated.getExhibitor().getUsername());

		        return response;
		}
    }

   
