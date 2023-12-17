package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.MenuItemDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMenuItem(id));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<MenuItemDTO>> getTopThreePopularOrder() {
        return ResponseEntity.ok(service.getTopMenuItem(3));
    }

    @PostMapping("/manage/add")
    public ResponseEntity<MenuItemDTO> addMenuItem(@Valid @RequestBody MenuItemDTO menuItem) {
        return ResponseEntity.ok(service.addMenuItem(menuItem));
    }

    @DeleteMapping("/manage/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        service.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item was deleted");
    }
}
