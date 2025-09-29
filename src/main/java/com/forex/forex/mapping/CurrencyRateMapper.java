package com.forex.forex.mapping;

import com.forex.forex.dto.CurrencyRateDto;
import com.forex.forex.entities.CurrencyRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    CurrencyRateDto toDto(CurrencyRateEntity currencyFrom);
    CurrencyRateEntity toEntity(CurrencyRateDto currencyRateDto);

    @Mapping(target = "id", ignore = true)
    void updateCurrencyRate(CurrencyRateDto newCurrencyRate, @MappingTarget CurrencyRateEntity oldCurrencyRate);
}
