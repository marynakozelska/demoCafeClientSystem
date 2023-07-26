package com.example.democafeclientsystem.entities;

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

    private boolean isActive;

    private boolean isPayed;

    public Order(Set<OrderItem> items, User user, int tableNumber) {
        this.items = items;
        this.user = user;
        this.tableNumber = tableNumber;
        isActive = true;
        isPayed = false;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public boolean getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(boolean payed) {
        isPayed = payed;
    }
}
