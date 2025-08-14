package com.cdac.eventnexus.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.cdac.eventnexus.custom_exceptions.DuplicateResourceException;
import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.ApiResponse;
import com.cdac.eventnexus.dto.DashboardSummaryDto;
import com.cdac.eventnexus.dto.LoginRequest;
import com.cdac.eventnexus.dto.UserRequestDto;
import com.cdac.eventnexus.dto.UserResponseDto;
import com.cdac.eventnexus.entities.Customer;
import com.cdac.eventnexus.entities.User;
import com.cdac.eventnexus.entities.UserRole;
import com.cdac.eventnexus.dao.CustomerRepository;
import com.cdac.eventnexus.dao.EventRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
//@AllArgsConstructor
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {
	
	
    private final PasswordEncoder passwordEncoder; 
	
    // Dependencies
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

	private final EventRepository eventRepository;

	private final CustomerRepository customerRepository;

  @Override
  public UserResponseDto createUser(UserRequestDto dto) {
	  
	  if (userRepository.existsByEmail(dto.getEmail())) {
	        throw new DuplicateResourceException("Email already registered: " + dto.getEmail());
	    }

	  if (userRepository.existsByUsername(dto.getUsername())) {
	      throw new DuplicateResourceException("Username is already taken.");
	    }
 
	  User user = modelMapper.map(dto, User.class);
      user.setIsActive(true);
      
      //Added 07-08-2025
      String encoded = passwordEncoder.encode(user.getPassword());
      user.setPassword(encoded);


      User savedUser = userRepository.save(user);
      return modelMapper.map(savedUser, UserResponseDto.class);
  }

  @Override
  public List<UserResponseDto> getAllUsers() {
	    return userRepository.findByIsActiveTrue().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
  }

  @Override
  public UserResponseDto getUserById(Long id) {
	  User user = userRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
      return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  public void deleteUser(Long id) {
      User user = userRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("User ID not found"));

      user.setIsActive(false);

      // Save updated entity
      userRepository.save(user);
  }
  
  @Override
  public List<UserResponseDto> getUsersByRole(UserRole role) {

	  List<User> users = userRepository.findByRole(role);
	    if (users == null) return List.of(); // fallback safety

	    return users.stream()
	            .filter(User::getIsActive)
	            .map(user -> modelMapper.map(user, UserResponseDto.class))
	            .collect(Collectors.toList());
  }

  @Override
  public Optional<UserResponseDto> findByEmail(String email) {
      return userRepository.findByEmailAndIsActiveTrue(email)
              .map(user -> modelMapper.map(user, UserResponseDto.class));
  }

  @Override
  public Optional<UserResponseDto> findByUsername(String username) {
      return userRepository.findByUsernameAndIsActiveTrue(username)
              .map(user -> modelMapper.map(user, UserResponseDto.class));
  }

  //Added 04-08-2025
  
  @Override
  public ApiResponse toggleUserStatus(Long userId) {
      User user = userRepository.findById(userId)
              .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      user.setIsActive(!user.getIsActive());
      userRepository.save(user);

      return new ApiResponse("User status toggled successfully");
  }

  @Override
  public ApiResponse updateUserRole(Long userId, UserRole newRole) {
      User user = userRepository.findById(userId)
              .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      user.setRole(newRole);
      userRepository.save(user);

      return new ApiResponse("User role updated successfully");
  }

  @Override
  public DashboardSummaryDto getSummary() {
      long totalUsers = userRepository.count();
      long activeUsers = userRepository.countByIsActiveTrue();
      long exhibitors = userRepository.countByRole(UserRole.EXHIBITOR);
      long customers = userRepository.countByRole(UserRole.CUSTOMER);
      
      //Added 08-08-2025
      long activeEventsCount = eventRepository.countByIsActiveTrue();
      long eventsCount = eventRepository.count();



      return DashboardSummaryDto.builder()
              .totalUsers(totalUsers)
              .activeUsers(activeUsers)
              .totalExhibitors(exhibitors)
              .totalCustomers(customers)
              .activeEvents(activeEventsCount)
              .totalEvents(eventsCount)
              .build();
  }
 


  @Override
  public Optional<UserResponseDto> login(LoginRequest loginRequest) {
      Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

      if (userOpt.isPresent()) {
          User user = userOpt.get();

          
          boolean userActive = user.getIsActive();
          boolean isCustomerRole = user.getRole() == UserRole.CUSTOMER;
          boolean customerActive = !isCustomerRole || customerRepository.findById(user.getId())
                  .map(Customer::isActive)
                  .orElse(false);

          if (!userActive || !customerActive) {
              throw new IllegalStateException("Account is inactive.");
          }

          
          //Migrate plain password to BCrypt if not already
          if (!user.getPassword().startsWith("$2a$") &&
              !user.getPassword().startsWith("$2b$") &&
              !user.getPassword().startsWith("$2y$")) {
              String encoded = passwordEncoder.encode(user.getPassword());
              user.setPassword(encoded);
              userRepository.save(user);
              System.out.println("Password migrated for: " + user.getEmail());
          }
          
          
          
          if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
             // return Optional.of(mapToDto(user)); // success
        	  
        	  return Optional.of(modelMapper.map(user, UserResponseDto.class));
          }
      }
      return Optional.empty(); // invalid login
  }


  
}
