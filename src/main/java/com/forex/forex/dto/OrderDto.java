package com.forex.forex.dto;

import com.forex.forex.entities.CurrencyName;
import com.forex.forex.entities.ExchangeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long customerId;
    private CurrencyName currencyFrom;
    private CurrencyName currencyTo;
    private BigDecimal amount;
    private BigDecimal rate;
    private ExchangeStatus status;
}
