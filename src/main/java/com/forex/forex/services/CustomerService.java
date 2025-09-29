package com.forex.forex.services;

import com.forex.forex.dto.CustomerDto;
import com.forex.forex.entities.CustomerEntity;
import com.forex.forex.mapping.CustomerMapper;
import com.forex.forex.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.info("creating new customer {} ", customerDto.getEmail());
        CustomerEntity entity = customerMapper.toEntity(customerDto);
        CustomerEntity save = customerRepository.save(entity);
        return customerMapper.toDto(save);
    }

    public CustomerDto getCustomerById(Long customerId) {
        Optional<CustomerEntity> customerByID = customerRepository.findById(customerId);
        return customerByID.map(customerMapper::toDto).orElse(new CustomerDto());
    }

    public CustomerDto saveCustomer(CustomerDto customerDto) {
        log.info("saving new customer {} ", customerDto.getEmail());
        CustomerEntity entity = customerMapper.toEntity(customerDto);
        CustomerEntity save = customerRepository.save(entity);
        return customerMapper.toDto(save);
    }
}
