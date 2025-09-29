package com.forex.forex.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.forex.forex.dto.CustomerDto;
import com.forex.forex.dto.OrderDto;
import com.forex.forex.entities.CurrencyName;
import com.forex.forex.entities.CurrencyRateEntity;
import com.forex.forex.entities.ExchangeStatus;
import com.forex.forex.entities.OrderEntity;
import com.forex.forex.exception.NotSufficientFoundException;
import com.forex.forex.mapping.OrderMapper;
import com.forex.forex.repositories.CurrencyRateRepository;
import com.forex.forex.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderService orderService;

    private CustomerDto customerDto;
    private OrderDto orderDto;
    private CurrencyRateEntity rateEntity;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setEmail("john@example.com");
        customerDto.setBalance(BigDecimal.valueOf(1000));

        orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setAmount(BigDecimal.valueOf(100));
        orderDto.setCurrencyFrom(CurrencyName.USD);
        orderDto.setCurrencyTo(CurrencyName.EUR);

        rateEntity = new CurrencyRateEntity();
        rateEntity.setCurrencyFrom(CurrencyName.USD);
        rateEntity.setCurrencyTo(CurrencyName.EUR);
        rateEntity.setRate(BigDecimal.valueOf(0.95));
    }

    @Test
    void testGetOrderByIdFound() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.toDto(orderEntity)).thenReturn(orderDto);

        OrderDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(CurrencyName.USD, result.getCurrencyFrom());
        verify(orderRepository).findById(1L);
        verify(orderMapper).toDto(orderEntity);
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertNull(result.getCurrencyFrom());
        assertNull(result.getCurrencyTo());
        verify(orderRepository).findById(1L);
        verify(orderMapper, never()).toDto(any());
    }

    @Test
    void testPlaceOrderSuccessful() {
        when(customerService.getCustomerById(1L)).thenReturn(customerDto);
        when(currencyRateRepository.findByCurrencyFromAndCurrencyTo(CurrencyName.USD, CurrencyName.EUR))
                .thenReturn(Optional.of(rateEntity));
        when(customerService.saveCustomer(customerDto)).thenReturn(customerDto);

        OrderDto result = orderService.placeOrder(orderDto);

        assertEquals(ExchangeStatus.COMPLETED, result.getStatus());
        assertEquals(BigDecimal.valueOf(95.0).setScale(2, RoundingMode.HALF_UP), result.getAmount().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(0.95).setScale(2, RoundingMode.HALF_UP), result.getRate().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(900), customerDto.getBalance());

        verify(customerService).getCustomerById(1L);
        verify(currencyRateRepository).findByCurrencyFromAndCurrencyTo(CurrencyName.USD, CurrencyName.EUR);
        verify(customerService).saveCustomer(customerDto);
    }

    @Test
    void testPlaceOrderInsufficientFunds() {
        customerDto.setBalance(BigDecimal.valueOf(50));
        when(customerService.getCustomerById(1L)).thenReturn(customerDto);

        NotSufficientFoundException exception = assertThrows(NotSufficientFoundException.class,
                () -> orderService.placeOrder(orderDto));

        assertEquals("Funds unavailable", exception.getMessage());
        verify(customerService).getCustomerById(1L);
        verify(currencyRateRepository, never()).findByCurrencyFromAndCurrencyTo(any(), any());
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testPlaceOrderCurrencyRateNotFound() {
        when(customerService.getCustomerById(1L)).thenReturn(customerDto);
        when(currencyRateRepository.findByCurrencyFromAndCurrencyTo(CurrencyName.USD, CurrencyName.EUR))
                .thenReturn(Optional.empty());

        OrderDto result = orderService.placeOrder(orderDto);

        assertEquals(ExchangeStatus.FAILED, result.getStatus());
        verify(customerService).getCustomerById(1L);
        verify(currencyRateRepository).findByCurrencyFromAndCurrencyTo(CurrencyName.USD, CurrencyName.EUR);
        verify(customerService, never()).saveCustomer(any());
    }

}