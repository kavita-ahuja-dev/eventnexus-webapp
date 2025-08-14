package com.cdac.eventnexus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto  {
	
	//uncommented 07-08-2025
	//private Long id;
	@NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;
	
	@NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
	@Pattern(
	        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
	        message = "Invalid email format"
	    )
    private String email;
	
	@NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
    
    @Pattern(regexp = "ADMIN|CUSTOMER|EXHIBITOR", message = "Invalid role. Valid values: ADMIN, CUSTOMER, EXHIBITOR")
    private String role; // CUSTOMER, EXHIBITOR, ADMIN
}

