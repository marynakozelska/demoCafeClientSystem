package com.example.democafeclientsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    MenuItemDTO menuItem;
    int count;
}
