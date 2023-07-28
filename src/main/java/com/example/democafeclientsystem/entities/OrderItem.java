package com.example.democafeclientsystem.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @EmbeddedId
    OrderItemKey orderItemKey = new OrderItemKey();

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    MenuItem menuItem;

    int aNumberOfItems;

    public OrderItem(Order order, MenuItem menuItem, int aNumberOfItems) {
        this.order = order;
        this.menuItem = menuItem;
        this.aNumberOfItems = aNumberOfItems;
    }

    public OrderItem(MenuItem menuItem, int aNumberOfItems) {
        this.menuItem = menuItem;
        this.aNumberOfItems = aNumberOfItems;
    }
}
