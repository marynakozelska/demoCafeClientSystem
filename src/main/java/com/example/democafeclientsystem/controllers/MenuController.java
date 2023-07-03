package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.MenuItemDTO;
import com.example.democafeclientsystem.entities.MenuItem;
import com.example.democafeclientsystem.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService service;

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getMenu() {
        return ResponseEntity.ok(service.getMenu());
    }

    @PostMapping("/manage/add")
    public ResponseEntity<MenuItemDTO> addMenuItem(@Validated @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(service.addMenuItem(menuItem));
    }
}
