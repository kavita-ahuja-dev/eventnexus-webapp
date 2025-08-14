package com.cdac.eventnexus.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.JwtResponse;
import com.cdac.eventnexus.dto.LoginRequest;
import com.cdac.eventnexus.dto.RegisterRequest;
import com.cdac.eventnexus.entities.User;
import com.cdac.eventnexus.entities.UserRole;
import com.cdac.eventnexus.security.JwtService;
import com.cdac.eventnexus.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Validate and parse role
        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());

        User user = User.builder()
                .username(request.getUsername()) // Add username if needed
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .build();

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Authenticate using email + password
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Wrap in UserPrincipal
        UserDetails userDetails = new UserPrincipal(user);
        String jwt = jwtService.generateToken(userDetails);

        // Return JWT and role
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getAuthorities().stream().findFirst().get().getAuthority()));
    }
}
