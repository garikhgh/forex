package com.forex.forex.mapping;

import com.forex.forex.dto.OrderDto;
import com.forex.forex.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    OrderEntity toEntity(OrderDto dto);
    OrderDto toDto(OrderEntity entity);
}
