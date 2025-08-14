package com.cdac.eventnexus.service;

import com.cdac.eventnexus.dto.CustomerRequestDto;
import com.cdac.eventnexus.dto.CustomerResponseDto;
import com.cdac.eventnexus.entities.Customer;
import java.util.List;

public interface CustomerService {
  CustomerResponseDto createCustomer(CustomerRequestDto dto);
  List<CustomerResponseDto> getAllCustomers();
  CustomerResponseDto getCustomerById(Long id);
  void deleteCustomer(Long id);
}
