package com.forex.forex.repositories;

import com.forex.forex.entities.CurrencyName;
import com.forex.forex.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
