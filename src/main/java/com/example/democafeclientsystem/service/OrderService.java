package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.MenuItemDTO;
import com.example.democafeclientsystem.dto.OrderDTO;
import com.example.democafeclientsystem.dto.OrderItemDTO;
import com.example.democafeclientsystem.dto.OrderResponse;
import com.example.democafeclientsystem.entities.*;
import com.example.democafeclientsystem.enums.OrderStatus;
import com.example.democafeclientsystem.exceptions.ExceededOrderLimitException;
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

    public List<OrderResponse> getActiveOrdersByAuth(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = repository.findByUserAndOrderStatusNot(user, OrderStatus.ARCHIVE);

        return orders
                .stream()
                .map(this::orderToResponse)
                .toList();
    }

    public List<OrderResponse> getPreviousOrdersByAuth(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = repository.findByUserAndOrderStatusIs(user, OrderStatus.ARCHIVE);

        return orders
                .stream()
                .map(this::orderToResponse)
                .toList();
    }

    public OrderDTO addOrderByAuth(Authentication authentication,
                                   OrderDTO orderDTO) {
        User user = (User) authentication.getPrincipal();

        if (getActiveOrdersByAuth(authentication).size() >= 2) {
            throw new ExceededOrderLimitException();
        }

        Order order = dtoToOrder(orderDTO);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.NEW);

        Order newOrder = repository.save(order);
        return orderToDto(newOrder);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = repository.findAll();

        return orders
                .stream()
                .map(this::orderToResponse)
                .toList();
    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = repository.findByOrderStatusIs(status);

        return orders
                .stream()
                .map(this::orderToResponse)
                .toList();
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
                .orderStatus(OrderStatus.NEW)
                .build();
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return order;
    }

    private OrderResponse orderToResponse(Order order) {
        OrderItemDTO[] items = order
                .getItems()
                .stream()
                .map(orderItem -> OrderItemDTO
                        .builder()
                        .menuItem(
                                MenuItemDTO.builder()
                                        .id(orderItem.getMenuItem().getId())
                                        .name(orderItem.getMenuItem().getName())
                                        .description(orderItem.getMenuItem().getDescription())
                                        .price(orderItem.getMenuItem().getPrice())
                                        .category(orderItem.getMenuItem().getCategory().name())
                                        .build()
                        )
                        .count(orderItem.getANumberOfItems())
                        .build()
                )
                .toArray(OrderItemDTO[]::new);

        return OrderResponse
                .builder()
                .id(order.getId())
                .menuItems(items)
                .user(order.getUser())
                .tableNumber(order.getTableNumber())
                .status(order.getOrderStatus().name())
                .build();
    }

    public OrderResponse changeOrderStatus(Long id, String status) {
        Order order = repository.getReferenceById(id);
        OrderStatus orderStatus = switch (status) {
            case "IN PROCESS" -> OrderStatus.IN_PROCESS;
            case "WAITING PAYMENT" -> OrderStatus.WAITING_PAYMENT;
            case "ARCHIVE" -> OrderStatus.ARCHIVE;
            default -> OrderStatus.NEW;
        };

        order.setOrderStatus(orderStatus);
        repository.save(order);

        return orderToResponse(order);
    }
}
