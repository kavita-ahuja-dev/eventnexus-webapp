package com.cdac.eventnexus.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cdac.eventnexus.dao.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));

        var auths = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        boolean enabled = Boolean.TRUE.equals(user.getIsActive()); // safe for nulls

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            enabled,            // enabled
            true,               // accountNonExpired
            true,               // credentialsNonExpired
            true,               // accountNonLocked
            auths
        );
    }


}


