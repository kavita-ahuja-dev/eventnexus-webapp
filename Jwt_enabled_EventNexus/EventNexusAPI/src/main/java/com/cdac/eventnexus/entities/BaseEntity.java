package com.cdac.eventnexus.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass // below class will NOT have any table generation, sub class entities will
					// inherit these common members
@Getter
@Setter
@ToString
public class BaseEntity {
	@Id // mandatory , PK
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "created_on")
	private LocalDate createdOn;
	
	@Column(name = "last_updated")
    private LocalDateTime lastUpdatedOn;

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdatedOn = LocalDateTime.now();
    }
	
}
