package com.forex.forex.controllers;


import com.forex.forex.dto.OrderDto;
import com.forex.forex.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto orderById = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderById);
    }


    @PostMapping("/make/order")
    public ResponseEntity<OrderDto> derivedOrder(@RequestBody OrderDto orderDto) {
        OrderDto madeOrder = orderService.placeOrder(orderDto);
        return ResponseEntity.ok(madeOrder);
    }
}
