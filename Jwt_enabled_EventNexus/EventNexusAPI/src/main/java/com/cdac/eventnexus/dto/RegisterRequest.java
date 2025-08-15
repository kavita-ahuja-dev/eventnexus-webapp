package com.cdac.eventnexus.dto;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    
    @Pattern(regexp = "ADMIN|CUSTOMER|EXHIBITOR", message = "Invalid role.")
    private String role;
}
