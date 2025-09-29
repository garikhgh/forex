package com.forex.forex.services;

import com.forex.forex.dto.CustomerDto;
import com.forex.forex.entities.CustomerEntity;
import com.forex.forex.mapping.CustomerMapper;
import com.forex.forex.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDto customerDto;
    private CustomerEntity customerEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john@example.com");
        customerDto.setBalance(BigDecimal.valueOf(100));

        customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setName("John Doe");
        customerEntity.setEmail("john@example.com");
        customerEntity.setBalance(BigDecimal.valueOf(100));
    }

    @Test
    void testCreateCustomer() {
        when(customerMapper.toEntity(customerDto)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toDto(customerEntity)).thenReturn(customerDto);

        CustomerDto result = customerService.createCustomer(customerDto);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());

        verify(customerMapper).toEntity(customerDto);
        verify(customerRepository).save(customerEntity);
        verify(customerMapper).toDto(customerEntity);
    }

    @Test
    void testGetCustomerByIdFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customerEntity));
        when(customerMapper.toDto(customerEntity)).thenReturn(customerDto);

        CustomerDto result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());

        verify(customerRepository).findById(1L);
        verify(customerMapper).toDto(customerEntity);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerDto result = customerService.getCustomerById(1L);

        assertNotNull(result); // returns empty DTO
        assertNull(result.getName());
        assertNull(result.getEmail());

        verify(customerRepository).findById(1L);
        verify(customerMapper, never()).toDto(any());
    }

    @Test
    void testSaveCustomer() {
        when(customerMapper.toEntity(customerDto)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toDto(customerEntity)).thenReturn(customerDto);

        CustomerDto result = customerService.saveCustomer(customerDto);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());

        verify(customerMapper).toEntity(customerDto);
        verify(customerRepository).save(customerEntity);
        verify(customerMapper).toDto(customerEntity);
    }

}