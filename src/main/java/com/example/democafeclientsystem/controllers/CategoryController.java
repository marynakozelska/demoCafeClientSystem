package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.enums.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        FoodCategory[] category = FoodCategory.values();
        List<String> stringCategories = new ArrayList<>();

        for (FoodCategory value : category) {
            stringCategories.add(value.name());
        }

        return ResponseEntity.ok(stringCategories);
    }

}
