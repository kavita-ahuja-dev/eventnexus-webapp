package com.cdac.eventnexus.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exhibitors")

//@Table(
//	    name = "events",
//	    uniqueConstraints = @UniqueConstraint(
//	        columnNames = {"exhibitor_id", "event_type_id", "year"}
//	    )
//	)

public class Exhibitor extends BaseEntity {
  
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
}