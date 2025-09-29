package com.forex.forex.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerDto {
    private String name;
    private String email;
    private BigDecimal balance;
}
