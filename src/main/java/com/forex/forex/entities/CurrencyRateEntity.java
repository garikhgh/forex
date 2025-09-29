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
public class CurrencyRateEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CurrencyName currencyFrom;
    @Enumerated(EnumType.STRING)
    private CurrencyName currencyTo;
    private BigDecimal rate;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastUpdated;

    @Version
    private Long version;
}
