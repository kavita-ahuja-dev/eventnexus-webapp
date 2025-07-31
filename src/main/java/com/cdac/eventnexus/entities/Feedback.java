package com.cdac.eventnexus.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Feedback left by any user on an event
 */
@Entity
@Table(name = "feedbacks",
uniqueConstraints = {
	    @UniqueConstraint(
	      name = "UK_feedbacks_user_event",          
	      columnNames = { "user_id", "event_id" }  
	    )}
    )
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(length = 1000)
    private String comment;

    private Integer rating;
}

