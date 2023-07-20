package com.example.democafeclientsystem.entities;

import com.example.democafeclientsystem.enums.FoodCategory;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Set;

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
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column
    private double price;

    @Column
    private String description;

    @Column
    private FoodCategory category;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private Set<OrderItem> items;
}
