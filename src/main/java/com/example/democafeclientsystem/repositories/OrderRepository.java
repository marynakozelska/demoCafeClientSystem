package com.example.democafeclientsystem.repositories;

import com.example.democafeclientsystem.entities.Order;
import com.example.democafeclientsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByUserAndIsActiveTrue(User user);
}
