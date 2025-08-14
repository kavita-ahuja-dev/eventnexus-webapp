package com.cdac.eventnexus.service;

import java.util.List;

import com.cdac.eventnexus.dto.ApiResponse;
import com.cdac.eventnexus.dto.DashboardSummaryDto;
import com.cdac.eventnexus.dto.UserResponseDto;
import com.cdac.eventnexus.entities.UserRole;

public interface AdminService {
    List<UserResponseDto> getAllUsers();
    ApiResponse toggleUserStatus(Long userId);
    ApiResponse updateUserRole(Long userId, UserRole newRole);
    DashboardSummaryDto getSummary();
}
