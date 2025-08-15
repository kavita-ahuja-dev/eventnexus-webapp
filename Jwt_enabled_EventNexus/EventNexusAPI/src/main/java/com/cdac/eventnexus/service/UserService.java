package com.cdac.eventnexus.service;

import com.cdac.eventnexus.dto.ApiResponse;
import com.cdac.eventnexus.dto.DashboardSummaryDto;
import com.cdac.eventnexus.dto.LoginRequest;
import com.cdac.eventnexus.dto.UserRequestDto;
import com.cdac.eventnexus.dto.UserResponseDto;
import com.cdac.eventnexus.entities.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    void deleteUser(Long id);
    //Added 03-08-25
    List<UserResponseDto> getUsersByRole(UserRole role);
    Optional<UserResponseDto> findByEmail(String email);
    Optional<UserResponseDto> findByUsername(String username);
    
    //Added 04-08-2025
    ApiResponse toggleUserStatus(Long userId);
    ApiResponse updateUserRole(Long userId, UserRole newRole);
    DashboardSummaryDto getSummary();
    
    //Added 7-8-25
    Optional<UserResponseDto> login(LoginRequest loginRequest);

}
