package com.example.democafeclientsystem.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDTO {

    private long[] items;

    private int tableNumber;

}
