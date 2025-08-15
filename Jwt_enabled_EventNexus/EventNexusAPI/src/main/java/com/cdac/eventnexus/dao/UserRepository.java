package com.cdac.eventnexus.dao;

import com.cdac.eventnexus.entities.User;
import com.cdac.eventnexus.entities.UserRole;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	// Fetch by user name
    Optional<User> findByUsername(String username);

    // Fetch by email
    Optional<User> findByEmail(String email);

    // Find all users by role
    List<User> findByRole(UserRole role);

    // Find all active users
    List<User> findByIsActiveTrue();

    // Find by user name and isActive = true
    Optional<User> findByUsernameAndIsActiveTrue(String username);

    // Find by email and isActive = true
    Optional<User> findByEmailAndIsActiveTrue(String email);

    // Optional: Search users by partial user name (for search features)
    List<User> findByUsernameContainingIgnoreCase(String keyword);
    
    //Added 04-08-2025
    long countByIsActiveTrue();
    long countByRole(UserRole role);
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username); // user name is unique 


    
}
