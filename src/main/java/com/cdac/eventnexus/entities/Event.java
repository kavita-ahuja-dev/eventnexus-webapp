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

@Entity
@Table(name = "events")

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

    // unidirectional ManyToOne → EventType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType type;
    
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventMode mode;

    //  unidirectional ManyToOne → User (exhibitor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibitor_id", nullable = false)
    private User exhibitor;

//    //  OneToMany → Offer
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "event_id")   // FK in offers table
//    private List<Offer> offers = new ArrayList<>();
//
//    // OneToMany → Feedback
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "event_id")   // FK in feedbacks table
//    private List<Feedback> feedbacks = new ArrayList<>();

//   
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "event_id")   // FK in event_images table
//    private List<EventImage> images = new ArrayList<>();
    

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks;

    
    /**
     * One‑to‑one, unidirectional: each Event has exactly one EventImage
     * FK lives in the event_images table (event_id), and is unique.
     */
    @OneToOne(fetch = FetchType.LAZY,
              cascade = CascadeType.ALL,
              orphanRemoval = true)
    @JoinColumn(name = "event_id",      // FK column in event_images
                referencedColumnName = "id",
                nullable = false,
                unique = true)         // enforce one‑to‑one
    private EventImage image;
    
}
