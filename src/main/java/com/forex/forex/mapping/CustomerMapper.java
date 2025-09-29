package com.forex.forex.mapping;

import com.forex.forex.dto.CustomerDto;
import com.forex.forex.entities.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    CustomerEntity toEntity(CustomerDto dto);
    CustomerDto toDto(CustomerEntity entity);
}
