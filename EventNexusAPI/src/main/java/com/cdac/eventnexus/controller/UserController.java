package com.cdac.eventnexus.controller;

import com.cdac.eventnexus.dto.LoginRequest;
import com.cdac.eventnexus.dto.UserRequestDto;
import com.cdac.eventnexus.dto.UserResponseDto;
import com.cdac.eventnexus.entities.UserRole;
import com.cdac.eventnexus.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto dto) {
        UserResponseDto saved = userService.createUser(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    

    //@GetMapping
    @GetMapping("/admin/users")
    @Operation(summary = "Fetch all active users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role (ADMIN, CUSTOMER, EXHIBITOR)")
    public ResponseEntity<List<UserResponseDto>> getUsersByRole(@PathVariable UserRole role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Find active user by email")
    public ResponseEntity<UserResponseDto> getByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Find active user by username")
    public ResponseEntity<UserResponseDto> getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    //Added 07-08-2025
    @PostMapping("/login")
    @Operation(summary = "Login user and return JWT token")
    public ResponseEntity<Optional<UserResponseDto>> login(@RequestBody LoginRequest loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }
}
