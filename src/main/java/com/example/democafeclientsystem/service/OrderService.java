package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.OrderDTO;
import com.example.democafeclientsystem.entities.*;
import com.example.democafeclientsystem.repositories.MenuRepository;
import com.example.democafeclientsystem.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final MenuRepository menuRepository;

    public OrderDTO getActiveOrderByAuth(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Order order = repository.findByUserAndIsActiveTrue(user);

        if (order != null) {
            return orderToDto(order);
        }

        return null;
    }

    public OrderDTO addOrderByAuth(Authentication authentication,
                                   OrderDTO orderDTO) {
        User user = (User) authentication.getPrincipal();

        Order order = dtoToOrder(orderDTO);
        order.setUser(user);
        order.setIsActive(true);

        Order newOrder = repository.save(order);
        return orderToDto(newOrder);
    }

    private OrderDTO orderToDto(Order order) {
        long[] items = order.getItems().stream()
                .mapToLong(item -> item.getMenuItem().getId())
                .toArray();

        return OrderDTO
                .builder()
                .items(items)
                .tableNumber(order.getTableNumber())
                .build();
    }

    private Order dtoToOrder(OrderDTO orderDTO) {
        Set<OrderItem> items = new LinkedHashSet<>();

        for (long id : orderDTO.getItems()) {
            MenuItem menuItem = menuRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("MenuItem not found with ID: " + id));

            boolean itemExists = false;
            for (OrderItem item : items) {
                if (item.getMenuItem().equals(menuItem)) {
                    item.setANumberOfItems(item.getANumberOfItems() + 1);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                OrderItem orderItem = new OrderItem(menuItem, 1);
                items.add(orderItem);
            }
        }

        Order order = Order
                .builder()
                .items(items)
                .tableNumber(orderDTO.getTableNumber())
                .build();
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return order;
    }
}
