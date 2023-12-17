package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.MenuItemDTO;
import com.example.democafeclientsystem.entities.MenuItem;
import com.example.democafeclientsystem.enums.FoodCategory;
import com.example.democafeclientsystem.repositories.MenuRepository;
import com.example.democafeclientsystem.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.democafeclientsystem.enums.FoodCategory.*;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository repository;
    private final OrderRepository orderRepository;

    public Map<String, List<MenuItemDTO>> getMenu() {

        List<MenuItem> menu = repository.findAll();

        List<MenuItemDTO> menuItemDTOS = menu
                .stream()
                .map(this::menuItemToDto)
                .toList();

        return menuItemDTOS.stream()
                .collect(Collectors.groupingBy(MenuItemDTO::getCategory));
    }

    public MenuItemDTO addMenuItem(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = dtoToMenuItem(menuItemDTO);

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

    private MenuItem dtoToMenuItem(MenuItemDTO menuItemDTO) {
        FoodCategory category;
        switch (menuItemDTO.getCategory()) {
            case "DESSERTS" -> category = DESSERTS;
            case "PIZZA" -> category = PIZZA;
            case "SOUPS" -> category = SOUPS;
            case "DRINKS" -> category = DRINKS;
            case "SALADS" -> category = SALADS;
            case "SIDE DISH" -> category = SIDE_DISH;
            case "BREAKFAST" -> category = BREAKFAST;
            default -> category = null;
        }

        return MenuItem
                .builder()
                .id(menuItemDTO.getId())
                .name(menuItemDTO.getName())
                .description(menuItemDTO.getDescription())
                .price(menuItemDTO.getPrice())
                .category(category)
                .build();
    }

    public List<MenuItemDTO> getTopMenuItem(int numberOfItems) {

        List<Object[]> popularFoodData = orderRepository.findMostPopularFood();
        List<MenuItem> topMenuItems = new ArrayList<>();

        for (Object[] data : popularFoodData) {
            Long menuItemId = (Long) data[0];
            MenuItem menuItem = repository.getReferenceById(menuItemId);

            topMenuItems.add(menuItem);

            if (topMenuItems.size() >= numberOfItems) {
                break;
            }
        }

        return topMenuItems
                .stream()
                .map(this::menuItemToDto)
                .toList();
    }
}
