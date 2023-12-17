package com.example.democafeclientsystem.entities;

import com.example.democafeclientsystem.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int tableNumber;

    @Enumerated
    private OrderStatus orderStatus;

    public Order(Set<OrderItem> items, User user, int tableNumber) {
        this.items = items;
        this.user = user;
        this.tableNumber = tableNumber;
        this.orderStatus = OrderStatus.NEW;
    }

}
