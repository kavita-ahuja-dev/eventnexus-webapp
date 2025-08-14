package com.cdac.eventnexus.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dao.CustomerRepository;
import com.cdac.eventnexus.dao.UserRepository;
import com.cdac.eventnexus.dto.CustomerRequestDto;
import com.cdac.eventnexus.dto.CustomerResponseDto;
import com.cdac.eventnexus.entities.Customer;
import com.cdac.eventnexus.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

  
    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Manual mapping instead of modelMapper due to @MapsId and shared PK
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setInterestArea(dto.getInterestArea());
        customer.setActive(true);

        Customer saved = customerRepository.save(customer);

        CustomerResponseDto response = modelMapper.map(saved, CustomerResponseDto.class);
        response.setUsername(user.getUsername());
        //Added 04-08-2025 - to add email with value not null
        response.setEmail(user.getEmail());
        return response;

    }
    
    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findByIsActiveTrue().stream()
            .map(customer -> {
                CustomerResponseDto dto = modelMapper.map(customer, CustomerResponseDto.class);
                dto.setUsername(customer.getUser().getUsername());
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .filter(Customer::isActive)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        CustomerResponseDto dto = modelMapper.map(customer, CustomerResponseDto.class);
        dto.setUsername(customer.getUser().getUsername());
        return dto;
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setActive(false);
        customer.getUser().setIsActive(false);

        customerRepository.save(customer); 
    }
}
