package com.cdac.eventnexus.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "offers",
uniqueConstraints = {
	    @UniqueConstraint(
	      name = "UK_offers_event_exhibitor",          
	      columnNames = { "event_id", "exhibitor_id" }  
	    )}
    )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer extends BaseEntity {
    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "offer_start_date")
    private LocalDate startDate;

    @Column(name = "offer_end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibitor_id", nullable = false)
    private User exhibitor;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
