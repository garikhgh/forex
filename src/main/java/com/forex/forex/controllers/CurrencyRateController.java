package com.forex.forex.controllers;

import com.forex.forex.dto.CurrencyRateDto;
import com.forex.forex.services.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rates")
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @GetMapping()
    ResponseEntity<List<CurrencyRateDto>> getCurrentRates() {
        List<CurrencyRateDto> allCurrencyRates = currencyRateService.getAllCurrencyRates();
        return  ResponseEntity.ok(allCurrencyRates);
    }

    @PostMapping("/simulate")
    ResponseEntity<Void> simulate(@RequestBody CurrencyRateDto currencyRateDto) {
        boolean updated = currencyRateService.simulateCurrencyRate(currencyRateDto);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
