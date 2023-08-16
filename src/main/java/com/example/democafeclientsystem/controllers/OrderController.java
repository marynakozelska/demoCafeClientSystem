package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.OrderDTO;
import com.example.democafeclientsystem.dto.OrderResponse;
import com.example.democafeclientsystem.enums.OrderStatus;
import com.example.democafeclientsystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService service;

    @GetMapping
    public ResponseEntity<OrderDTO> getOrder(Authentication authentication) {

        return new ResponseEntity<>(service.getActiveOrderByAuth(authentication), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<OrderDTO> addOrder(Authentication authentication,
                                             @RequestBody OrderDTO orderDTO) {

        return new ResponseEntity<>(service.addOrderByAuth(authentication, orderDTO), HttpStatus.OK);
    }

    @GetMapping("/manage")
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return new ResponseEntity<>(service.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/manage/active/new")
    public ResponseEntity<List<OrderResponse>> getNewOrders() {
        return new ResponseEntity<>(service.getOrdersByStatus(OrderStatus.NEW), HttpStatus.OK);
    }

    @GetMapping("/manage/active/process")
    public ResponseEntity<List<OrderResponse>> getOrdersInProcess() {
        return new ResponseEntity<>(service.getOrdersByStatus(OrderStatus.IN_PROCESS), HttpStatus.OK);
    }

    @GetMapping("/manage/active/waiting-payment")
    public ResponseEntity<List<OrderResponse>> getOrdersWaitingForPayment() {
        return new ResponseEntity<>(service.getOrdersByStatus(OrderStatus.WAITING_PAYMENT), HttpStatus.OK);
    }

    @PostMapping("/manage/active/{id}")
    public ResponseEntity<OrderResponse> changeOrderStatus(@PathVariable Long id, @RequestBody String status) {
        return new ResponseEntity<>(service.changeOrderStatus(id, status), HttpStatus.OK);
    }

}
