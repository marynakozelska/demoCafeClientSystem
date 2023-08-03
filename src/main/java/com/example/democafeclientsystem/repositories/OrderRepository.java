package com.example.democafeclientsystem.repositories;

import com.example.democafeclientsystem.entities.Order;
import com.example.democafeclientsystem.entities.User;
import com.example.democafeclientsystem.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserAndOrderStatusNot(User user, OrderStatus orderStatus);

    List<Order> findByOrderStatusIs(OrderStatus orderStatus);

}
