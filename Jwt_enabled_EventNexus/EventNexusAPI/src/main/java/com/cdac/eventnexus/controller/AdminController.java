package com.cdac.eventnexus.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cdac.eventnexus.service.UserService;

import lombok.RequiredArgsConstructor;

import com.cdac.eventnexus.dto.ApiResponse;
import com.cdac.eventnexus.dto.DashboardSummaryDto;
import com.cdac.eventnexus.dto.UserResponseDto;
import com.cdac.eventnexus.entities.UserRole;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/users/{id}/toggle-active")
    public ApiResponse toggleUserActive(@PathVariable Long id) {
        return userService.toggleUserStatus(id);
    }

    @PatchMapping("/users/{id}/role")
    public ApiResponse updateRole(@PathVariable Long id, @RequestParam UserRole role) {
        return userService.updateUserRole(id, role);
    }

    @GetMapping("/dashboard")
    public DashboardSummaryDto getDashboardSummary() {
        return userService.getSummary();
    }
}

