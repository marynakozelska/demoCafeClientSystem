package com.example.democafeclientsystem.entities;

import com.example.democafeclientsystem.enums.FoodCategory;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    @NotBlank
    private String name;
    @Column
    private double price;
    @Column
    private String description;
    @Column
    private FoodCategory category;
}
