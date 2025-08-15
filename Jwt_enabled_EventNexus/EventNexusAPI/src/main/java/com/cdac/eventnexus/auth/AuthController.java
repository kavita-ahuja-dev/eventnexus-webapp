package com.cdac.eventnexus.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.eventnexus.dao.CustomerRepository;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.AuthResponse;
import com.cdac.eventnexus.dto.LoginRequest;
import com.cdac.eventnexus.dto.RegisterRequest;
import com.cdac.eventnexus.entities.User;
import com.cdac.eventnexus.entities.UserRole;
import com.cdac.eventnexus.security.JwtService;
import com.cdac.eventnexus.security.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final CustomerRepository customerRepo;

    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail().trim())
                .password(encoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .build();

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
    
//  Added 15 Aug 2025
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
//        // Normalize email (avoid trailing spaces)
//        final String email = request.getEmail() == null ? null : request.getEmail().trim();
//
//        try {
//            // 1) Verify credentials
//            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
//        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
//            // Aligns with earlier behavior
//            return ResponseEntity.status(401).body(java.util.Map.of("error", "Invalid email or password"));
//        }
//
//        // 2) Load user
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 3) Block inactive accounts (restored behavior)
//        if (Boolean.FALSE.equals(user.getIsActive())) {
//            return ResponseEntity.status(403).body(java.util.Map.of("message", "Account is inactive"));
//            // (If your frontend checks `error` not `message`, change the key accordingly)
//        }
//
//        // 4) Generate JWT
//        String token = jwtService.generateToken(new UserPrincipal(user));
//
//        // 5) Optional: lookup linked Customer id (may be null)
//        Long customerId = customerRepo.findByUserId(user.getId())
//                .map(c -> c.getId())
//                .orElse(null);
//
//        // 6) Build response 
//        AuthResponse resp = AuthResponse.builder()
//                .token(token)
//                // choose the id field your AuthResponse actually has:
//                // .id(user.getId())                          
//                .userId(user.getId())                          
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .role(user.getRole().name())                   // "ADMIN" / "EXHIBITOR" / "CUSTOMER"
//                .customerId(customerId)
//                .isActive(user.getIsActive())
//                .build();
//
//        return ResponseEntity.ok()
//                .header("Authorization", "Bearer " + token)    
//                .body(resp);
//    }
    
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
//        // Normalize email (avoid trailing spaces)
//        final String email = request.getEmail() == null ? null : request.getEmail().trim();
//
//        try {
//            // 1) Verify credentials
//            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
//        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
//            // Same 401 as before
//            return ResponseEntity.status(401).body(java.util.Map.of("error", "Invalid email or password"));
//        }
//
//        // 2) Load user
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 3) Block inactive accounts — SAME as the old endpoint
//        if (Boolean.FALSE.equals(user.getIsActive())) {
//            return ResponseEntity.status(403)
//                    .header("WWW-Authenticate", "Bearer error=\"account_inactive\", error_description=\"Account is inactive\"")
//                    .body(java.util.Map.of("error", "Account is inactive"));
//            // note: UI already checks for err.response.data.message OR .error and 'inactive' substring
//        }
//
//        // 4) Generate JWT
//        String token = jwtService.generateToken(new UserPrincipal(user));
//
//        // 5) Optional: lookup linked Customer id (may be null)
//        Long customerId = customerRepo.findByUserId(user.getId())
//                .map(c -> c.getId())
//                .orElse(null);
//
//        // 6) Build response (unchanged AuthResponse shape)
//        AuthResponse resp = AuthResponse.builder()
//                .token(token)
//                .userId(user.getId())           // or .id(user.getId()) if that's your field
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .role(user.getRole().name())    // "ADMIN" / "EXHIBITOR" / "CUSTOMER"
//                .customerId(customerId)
//                .isActive(user.getIsActive())
//                .build();
//
//        return ResponseEntity.ok()
//                .header("Authorization", "Bearer " + token)
//                .body(resp);
//    }

    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        final String email = request.getEmail() == null ? null : request.getEmail().trim();

        // Verify credentials
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Invalid email or password"));
        }

        // Load user
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If CUSTOMER, try to find linked customer profile
        Long customerId = null;
        if (user.getRole() == UserRole.CUSTOMER) {
            customerId = customerRepo.findByUserId(user.getId())
                    .map(c -> c.getId())
                    .orElse(null);
        }

        //  Inactive rules:
        //    - isActive == false
        //    - CUSTOMER without customer profile
        boolean inactive = Boolean.FALSE.equals(user.getIsActive())
                || (user.getRole() == UserRole.CUSTOMER && customerId == null);

        if (inactive) {
            String reason = Boolean.FALSE.equals(user.getIsActive())
                    ? "Account is inactive"
                    : "Account is inactive: customer profile not created";
            return ResponseEntity.status(403)
                    .header("WWW-Authenticate",
                            "Bearer error=\"account_inactive\", error_description=\"" + reason + "\"")
                    .body(java.util.Map.of("error", reason)); // UI already checks `error/message` containing 'inactive'
        }

        // Generate JWT
        String token = jwtService.generateToken(new UserPrincipal(user));

        // Build the AuthResponse your UI expects 
        AuthResponse resp = AuthResponse.builder()
                .token(token)
                .id(user.getId())                 // <-- use .id if that's your DTO field
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())      // "ADMIN"/"EXHIBITOR"/"CUSTOMER"
                .isActive(user.getIsActive())
                .customerId(customerId)           // null for non-customers or if not applicable
                .build();

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(resp);
    }


}
