package com.cdac.eventnexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto extends BaseDTO {
	
	private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean isActive;

}
