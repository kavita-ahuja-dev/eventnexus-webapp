package com.cdac.eventnexus.entities;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments",
uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "event_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "customer_id", nullable = false)
private User customer;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "event_id", nullable = false)
private Event event;

@Column(name = "registration_date", updatable = false)
private LocalDateTime registrationDate = LocalDateTime.now();

@Column(name = "payment_date")
private LocalDateTime paymentDate;

private BigDecimal amount;


//private String paymentMode;

@Enumerated(EnumType.STRING)
@Column(name = "payment_mode",nullable = false)
private PaymentMode mode;


}