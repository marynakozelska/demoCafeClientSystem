package com.example.democafeclientsystem.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {
    private int id;
    private String name;
    private double price;
    private String description;
    private String category;

    public MenuItemDTO(String name, double price, String description, String category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }
}
