package com.cdac.eventnexus.entities;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.UniqueConstraint;

//Added 11-08-2025 for testing purpose
@Entity
//@Table(name = "events")

@Table(
		  name = "events",
		  uniqueConstraints = @UniqueConstraint(
		    columnNames = {"exhibitor_id", "title", "date"}
		  )
		) 
@Getter
@Setter
//@ToString(exclude = {"offers", "feedbacks", "image", "exhibitor", "type"})
@ToString(exclude = {"exhibitor", "type","feedbacks","image"}) 
public class Event extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

   // private String location;
    
    /** only populated when mode == ONLINE */
    @Column(name = "zoom_url")
    private String zoomUrl;

    /** only populated when mode == OFFLINE */
    @Column(name = "address")
    private String address;

    @Column(name = "latitude", precision = 10)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10)
    private BigDecimal longitude;

    @Column(name = "map_url")
    private String mapUrl;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer year;

    // unidirectional ManyToOne to EventType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType type;
    
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventMode mode;

    //  unidirectional ManyToOne to User (exhibitor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibitor_id", nullable = false)
    private User exhibitor;


    //uncommented on 10-08-2025
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks;

   
    
    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private EventImage image;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    
}
