package com.forex.forex.services;

import com.forex.forex.dto.CustomerDto;
import com.forex.forex.dto.OrderDto;
import com.forex.forex.entities.CurrencyName;
import com.forex.forex.entities.CurrencyRateEntity;
import com.forex.forex.entities.ExchangeStatus;
import com.forex.forex.entities.OrderEntity;
import com.forex.forex.exception.NotSufficientFoundException;
import com.forex.forex.mapping.OrderMapper;
import com.forex.forex.repositories.CurrencyRateRepository;
import com.forex.forex.repositories.CustomerRepository;
import com.forex.forex.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CurrencyRateRepository currencyRateRepository;
    private final OrderMapper orderMapper;
    private final CustomerService customerService;

    public OrderDto getOrderById(Long orderId) {
        Optional<OrderEntity> byId = orderRepository.findById(orderId);
        return byId.map(orderMapper::toDto).orElse(new OrderDto());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public OrderDto placeOrder(OrderDto orderDto) {
        // here: there should be dto for receiving the exchange request
        orderDto.setStatus(ExchangeStatus.NEW);
        CustomerDto customerById = customerService
                .getCustomerById(orderDto.getCustomerId());
        if (customerById != null && customerById.getBalance() != null
                && customerById.getBalance().compareTo(orderDto.getAmount()) >= 0) {

            BigDecimal currentBalance = customerById.getBalance();
            BigDecimal remainingBalanceAfterExchange = currentBalance
                    .subtract(orderDto.getAmount());
            BigDecimal exchangeAmount = currentBalance.subtract(remainingBalanceAfterExchange);

            Optional<CurrencyRateEntity> currencyDetailsOptional = currencyRateRepository.findByCurrencyFromAndCurrencyTo(orderDto.getCurrencyFrom(),
                    orderDto.getCurrencyTo());

            if (currencyDetailsOptional != null && currencyDetailsOptional.isPresent()) {
                CurrencyRateEntity currencyDetails = currencyDetailsOptional.get();
                CurrencyName currencyFrom = currencyDetails.getCurrencyFrom();
                CurrencyName currencyTo = currencyDetails.getCurrencyTo();
                BigDecimal exchangedAmount = exchangeAmount.multiply(currencyDetails.getRate());
                customerById.setBalance(remainingBalanceAfterExchange);
                customerService.saveCustomer(customerById);


                // all the exchanges could be stored as historical data CQRS

                return OrderDto.builder()
                        .currencyFrom(currencyDetails.getCurrencyFrom())
                        .currencyTo(currencyDetails.getCurrencyTo())
                        .amount(exchangedAmount)
                        .rate(currencyDetails.getRate())
                        .status(ExchangeStatus.COMPLETED)
                        .build();
            }

        } else {
            throw new NotSufficientFoundException("Funds unavailable");
        }
        return OrderDto.builder()
                .status(ExchangeStatus.FAILED)
                .build();
    }
}
