package com.forex.forex.setup;

import com.forex.forex.dto.CurrencyRateDto;
import com.forex.forex.dto.CustomerDto;
import com.forex.forex.entities.CurrencyName;
import com.forex.forex.services.CurrencyRateService;
import com.forex.forex.services.CustomerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Slf4j
//@Profile("local")
@Component
@RequiredArgsConstructor
public class MockTheProject {

    private final CustomerService customerService;
    private final CurrencyRateService currencyRateService;

    private final Random RANDOM = new Random();

    @Scheduled(cron = "*/20 * * * * *")
    private void simulate() {
        for (CurrencyRateDto currencyRate : currencyRateService.getAllCurrencyRates()) {
            currencyRate.setRate(BigDecimal.valueOf(0.5 + (5 * RANDOM.nextDouble()))
                    .setScale(4, RoundingMode.HALF_UP));
            currencyRateService.simulateCurrencyRate(currencyRate);
        }

    }

    @PostConstruct
    public void setUp() {
        mockCustomers();
        populateInitialCurrencyRates();
        simulate();
    }

    private void populateInitialCurrencyRates() {
        log.info("Populating initial currency rates");

        // Define common currency pairs
        CurrencyName[][] currencyPairs = {
                {CurrencyName.USD, CurrencyName.EUR},
                {CurrencyName.USD, CurrencyName.JPY},
                {CurrencyName.EUR, CurrencyName.GBP},
                {CurrencyName.GBP, CurrencyName.USD},
                {CurrencyName.EUR, CurrencyName.JPY},
                {CurrencyName.JPY, CurrencyName.USD}
        };

        for (CurrencyName[] pair : currencyPairs) {
            CurrencyRateDto rateDto = new CurrencyRateDto();
            rateDto.setCurrencyFrom(pair[0]);
            rateDto.setCurrencyTo(pair[1]);

            // Assign a realistic initial rate based on pair
            BigDecimal rate;
            if (pair[0] == CurrencyName.USD && pair[1] == CurrencyName.EUR) {
                rate = BigDecimal.valueOf(0.95);
            } else if (pair[0] == CurrencyName.USD && pair[1] == CurrencyName.JPY) {
                rate = BigDecimal.valueOf(145);
            } else if (pair[0] == CurrencyName.EUR && pair[1] == CurrencyName.GBP) {
                rate = BigDecimal.valueOf(0.85);
            } else if (pair[0] == CurrencyName.GBP && pair[1] == CurrencyName.USD) {
                rate = BigDecimal.valueOf(1.25);
            } else if (pair[0] == CurrencyName.EUR && pair[1] == CurrencyName.JPY) {
                rate = BigDecimal.valueOf(135);
            } else if (pair[0] == CurrencyName.JPY && pair[1] == CurrencyName.USD) {
                rate = BigDecimal.valueOf(0.007);
            } else {
                rate = BigDecimal.valueOf(1); // fallback
            }

            rateDto.setRate(rate.setScale(4, RoundingMode.HALF_UP));

            boolean created = currencyRateService.createCurrencyRate(rateDto);
            if (!created) {
                log.error("Failed to create currency rate {}", rateDto);
            } else {
                log.info("Successfully created currency rate {}", rateDto);
            }
        }

    }

    public void mockCustomers() {

        log.info("Mocking initial customers");
        for (int i = 1; i <= 20; i++) {
            CustomerDto customer = new CustomerDto();
            customer.setName("Customer " + i);
            customer.setEmail("customer" + i + "@example.com");
            customer.setBalance(BigDecimal.valueOf(Math.round(Math.random() * 10000)));
            customerService.createCustomer(customer);
        }
    }
}
