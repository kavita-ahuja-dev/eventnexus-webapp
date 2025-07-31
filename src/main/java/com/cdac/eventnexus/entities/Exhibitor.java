package com.cdac.eventnexus.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exhibitors")

//@Table(
//	    name = "events",
//	    uniqueConstraints = @UniqueConstraint(
//	        columnNames = {"exhibitor_id", "event_type_id", "year"}
//	    )
//	)

public class Exhibitor {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;
    
}