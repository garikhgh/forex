package com.forex.forex.controllers;

import com.forex.forex.dto.CustomerDto;
import com.forex.forex.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomersController {

    private final CustomerService customerService;

    @PostMapping()
    public ResponseEntity<CustomerDto>  createCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        return  ResponseEntity.ok(createdCustomer);

    }

    @GetMapping("/customerId")
    public ResponseEntity<CustomerDto> getCustomer(@RequestParam("customerId") Long customerId) {
        CustomerDto customerDto = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customerDto);
    }

}
