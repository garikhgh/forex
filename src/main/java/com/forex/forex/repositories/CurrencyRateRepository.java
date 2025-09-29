package com.forex.forex.repositories;

import com.forex.forex.entities.CurrencyName;
import com.forex.forex.entities.CurrencyRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRateEntity, Long> {
    // Find a single entity by currencyFrom and currencyTo
    Optional<CurrencyRateEntity> findByCurrencyFromAndCurrencyTo(CurrencyName currencyFrom,
                                                                 CurrencyName currencyTo);

}
