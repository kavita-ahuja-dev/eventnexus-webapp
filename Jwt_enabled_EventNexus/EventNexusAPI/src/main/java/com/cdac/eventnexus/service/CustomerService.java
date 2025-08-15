package com.cdac.eventnexus.service;

import java.util.List;

import com.cdac.eventnexus.dto.CustomerRequestDto;
import com.cdac.eventnexus.dto.CustomerResponseDto;

public interface CustomerService {
  CustomerResponseDto createCustomer(CustomerRequestDto dto);
  List<CustomerResponseDto> getAllCustomers();
  CustomerResponseDto getCustomerById(Long id);
  void deleteCustomer(Long id);
}
