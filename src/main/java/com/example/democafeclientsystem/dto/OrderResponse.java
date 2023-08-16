package com.example.democafeclientsystem.dto;

import com.example.democafeclientsystem.entities.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {

    private Long id;
    private OrderItemDTO[] menuItems;
    private User user;
    private int tableNumber;
    private String status;

}
