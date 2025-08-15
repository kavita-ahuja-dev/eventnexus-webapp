package com.cdac.eventnexus.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
  private String token;      // JWT
  private Long id;
  private String username;
  private String email;
  private String role;       // ADMIN / EXHIBITOR / CUSTOMER
  private Boolean isActive;
  
  //Added 15-08-2025
  private Long customerId;
  private Long userId;

}
