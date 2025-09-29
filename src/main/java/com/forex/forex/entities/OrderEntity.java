package com.forex.forex.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long accountId;

    @Enumerated(EnumType.STRING)
    private CurrencyName currencyNameFrom;
    @Enumerated(EnumType.STRING)
    private CurrencyName currencyNameTo;
    private BigDecimal amount;
    private BigDecimal rate;
    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastUpdated;
}
