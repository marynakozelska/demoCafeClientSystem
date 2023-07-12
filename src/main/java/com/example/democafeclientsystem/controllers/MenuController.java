package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.MenuItemDTO;
import com.example.democafeclientsystem.entities.MenuItem;
import com.example.democafeclientsystem.enums.FoodCategory;
import com.example.democafeclientsystem.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService service;

    @GetMapping
    public ResponseEntity<Map<String, List<MenuItemDTO>>> getMenu() {
        return ResponseEntity.ok(service.getMenu());
    }

    @PostMapping("/manage/add")
    public ResponseEntity<MenuItemDTO> addMenuItem(@Valid @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(service.addMenuItem(menuItem));
    }
}
