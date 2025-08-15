package com.cdac.eventnexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto extends BaseDTO {
    private Long userId;
    //added 03-08-2025
    private String username;         
    
    private String firstName;
    private String lastName;
    private String interestArea;
    
    private String email;

}
