package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.OrderDTO;
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
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return new ResponseEntity<>(service.getAllOrders(), HttpStatus.OK);
    }
    @GetMapping("/manage/active")
    public ResponseEntity<List<OrderDTO>> getActiveOrders() {
        return new ResponseEntity<>(service.getAllActiveOrders(), HttpStatus.OK);
    }

}
