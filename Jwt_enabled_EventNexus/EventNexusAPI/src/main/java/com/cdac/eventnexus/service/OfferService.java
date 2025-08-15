package com.cdac.eventnexus.service;

import com.cdac.eventnexus.dto.OfferRequestDto;
import com.cdac.eventnexus.dto.OfferResponseDto;
import java.util.List;

public interface OfferService {
    OfferResponseDto createOffer(OfferRequestDto dto);
    List<OfferResponseDto> getAllOffers();
    OfferResponseDto getOfferById(Long id);
    void deleteOffer(Long id);
  
    //added 02-08-25
    List<OfferResponseDto> getOffersByEventId(Long eventId);
    List<OfferResponseDto> getOffersByExhibitorId(Long exhibitorId);
    OfferResponseDto updateOffer(Long id, OfferRequestDto dto);
    
    String updateOfferEndDateDesc(Long offerId, OfferRequestDto dto);
}
