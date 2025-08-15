package com.cdac.eventnexus.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "event_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventType extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    //for making unidirectional comment below lines
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    
    
}