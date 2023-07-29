package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.MenuItemDTO;
import com.example.democafeclientsystem.entities.MenuItem;
import com.example.democafeclientsystem.repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository repository;

    public Map<String, List<MenuItemDTO>> getMenu() {

        List<MenuItem> menu = repository.findAll();

        List<MenuItemDTO> menuItemDTOS = menu
                .stream()
                .map(this::menuItemToDto)
                .toList();

        return menuItemDTOS.stream()
                .collect(Collectors.groupingBy(MenuItemDTO::getCategory));
    }

    public MenuItemDTO addMenuItem(MenuItem menuItem) {
        MenuItem menuItemSaved = repository.save(menuItem);
        return menuItemToDto(menuItemSaved);
    }

    public MenuItemDTO getMenuItem(Long id) {
        return menuItemToDto(
                repository.getReferenceById(id)
        );
    }

    public void deleteMenuItem(Long id) {
        MenuItem menuItem = repository.getReferenceById(id);
        repository.delete(menuItem);
    }

    private MenuItemDTO menuItemToDto(MenuItem menuItem) {
        String category;
        switch (menuItem.getCategory()) {
            case DESSERTS -> category = "DESSERTS";
            case PIZZA -> category = "PIZZA";
            case SOUPS -> category = "SOUPS";
            case DRINKS -> category = "DRINKS";
            case SALADS -> category = "SALADS";
            case SIDE_DISH -> category = "SIDE DISH";
            case BREAKFAST -> category = "BREAKFAST";
            default -> category = null;
        }

        return MenuItemDTO
                .builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .category(category)
                .build();
    }
}
