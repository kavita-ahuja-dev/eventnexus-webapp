package com.cdac.eventnexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto  {
    private String username;
    private String email;
    private String password;
    private String role; // CUSTOMER, EXHIBITOR, ADMIN
}

