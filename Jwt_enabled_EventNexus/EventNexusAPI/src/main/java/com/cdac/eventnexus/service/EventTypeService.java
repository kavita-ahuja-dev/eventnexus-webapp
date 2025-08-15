package com.cdac.eventnexus.service;

import com.cdac.eventnexus.entities.EventType;
import java.util.List;

public interface EventTypeService {
    EventType save(EventType eventtype);
    EventType getById(Long id);
    List<EventType> getAll();
    void deleteById(Long id);
    
    //Added 02-08-25
    boolean existsByName(String name);
    List<EventType> searchByKeyword(String keyword);
}
