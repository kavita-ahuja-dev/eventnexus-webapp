package com.cdac.eventnexus.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
//        @UniqueConstraint(columnNames = "username"),
//        @UniqueConstraint(columnNames = "email")
    	  @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
    	  @UniqueConstraint(name = "uk_users_email", columnNames = "email"),

    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class User extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @NotNull(message = "User role must not be null. Valid values: ADMIN, CUSTOMER, EXHIBITOR")
//    @Enumerated(EnumType.STRING)
//    @Column(name = "role", nullable = false)
//    private String role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @NotNull
    @Column(name = "isActive", nullable = false)
    private Boolean isActive = Boolean.TRUE;
    
    //Added 07-08-2025
        public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = UserRole.valueOf(role); 
    }
}