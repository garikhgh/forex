package com.forex.forex.dto;

import com.forex.forex.entities.CurrencyName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class CurrencyRateDto {

    private Long id;
    @NotNull
    private CurrencyName currencyFrom;
    @NotNull
    private CurrencyName currencyTo;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal rate;
}
