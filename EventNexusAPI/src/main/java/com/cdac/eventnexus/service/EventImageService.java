package com.cdac.eventnexus.service;

import com.cdac.eventnexus.entities.EventImage;
import java.util.List;

public interface EventImageService {
    EventImage save(EventImage eventimage);
    EventImage getById(Long id);
    List<EventImage> getAll();
    void deleteById(Long id);
}
