package com.cdac.eventnexus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cdac.eventnexus.dao.EventImageRepository;
import com.cdac.eventnexus.entities.EventImage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class EventImageServiceImpl implements EventImageService {

    
    private final EventImageRepository eventimageRepository;

    @Override
    public EventImage save(EventImage eventimage) {
        return eventimageRepository.save(eventimage);
    }

    @Override
    public EventImage getById(Long id) {
        return eventimageRepository.findById(id).orElse(null);
    }

    @Override
    public List<EventImage> getAll() {
        return eventimageRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        eventimageRepository.deleteById(id);
    }
}
