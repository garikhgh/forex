package com.forex.forex.services;

import com.forex.forex.dto.CurrencyRateDto;
import com.forex.forex.entities.CurrencyRateEntity;
import com.forex.forex.mapping.CurrencyRateMapper;
import com.forex.forex.repositories.CurrencyRateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateService {

    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRateRepository currencyRateRepository;

    public List<CurrencyRateDto> getAllCurrencyRates() {
        List<CurrencyRateEntity> allRates = currencyRateRepository.findAll();
        return convertToDto(allRates);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean createCurrencyRate(CurrencyRateDto currencyRateDto) {
        log.info("Creating new currency from {}  to {} rate {} ", currencyRateDto.getCurrencyFrom(), currencyRateDto.getCurrencyTo(), currencyRateDto.getRate());
        CurrencyRateEntity entity = currencyRateMapper.toEntity(currencyRateDto);
        CurrencyRateEntity save = currencyRateRepository.save(entity);
        return currencyRateMapper.toDto(save) != null;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean simulateCurrencyRate(CurrencyRateDto currencyRateDto) {
        log.info("Simulating currency rate {}", currencyRateDto);
        Optional<CurrencyRateEntity> byId = currencyRateRepository.findById(currencyRateDto.getId());
        if (byId.isPresent()) {
            CurrencyRateEntity currencyRateEntity = byId.get();
            currencyRateMapper.updateCurrencyRate(currencyRateDto, currencyRateEntity);
            currencyRateRepository.save(currencyRateEntity);
            log.info("Currency rate {} simulated", currencyRateDto);
            return true;
        }
        log.info("No currency rate found for id {}", currencyRateDto.getId());
        return false;
    }


    private List<CurrencyRateDto> convertToDto(List<CurrencyRateEntity> entities) {
        return entities.stream().map(currencyRateMapper::toDto)
                .toList();

    }
}
