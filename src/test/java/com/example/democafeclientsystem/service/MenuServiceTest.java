package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.MenuItemDTO;
import com.example.democafeclientsystem.entities.MenuItem;
import com.example.democafeclientsystem.enums.FoodCategory;
import com.example.democafeclientsystem.repositories.MenuRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private MenuService underTest;

    @Test
    void canGetMenu() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(1L, "Margarita", 255f, null, FoodCategory.PIZZA, null));
        menuItems.add(new MenuItem(2L, "Spaghetti", 180f, null, FoodCategory.SIDE_DISH, null));

        when(menuRepository.findAll()).thenReturn(menuItems);

        Map<String, List<MenuItemDTO>> result = underTest.getMenu();

        assertThat(result).isNotNull();
        assertThat(result
                .get("PIZZA")
                .get(0)
                .getName())
                .isEqualTo("Margarita");
        assertThat(result
                .get("SIDE DISH")
                .get(0)
                .getName())
                .isEqualTo("Spaghetti");
    }

    @Test
    @Disabled
    void canAddMenuItem() {
        MenuItem menuItem = MenuItem
                .builder()
                .name("Margarita")
                .price(255.0)
                .category(FoodCategory.PIZZA)
                .build();

        when(menuRepository.save(menuItem)).thenReturn(menuItem);

        MenuItemDTO menuItemDTO = MenuItemDTO
                .builder()
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .category(menuItem.getCategory().toString())
                .build();
        underTest.addMenuItem(menuItemDTO);

        ArgumentCaptor<MenuItem> argumentCaptor = ArgumentCaptor.forClass(MenuItem.class);
        verify(menuRepository).save(argumentCaptor.capture());

        MenuItem capturedMenuItem = argumentCaptor.getValue();
        assertThat(capturedMenuItem).isEqualTo(menuItem);
    }

    @Test
    void getMenuItem() {
        final Long id = 1L;
        MenuItem menuItem = new MenuItem(id, "Apple Pie", 85f, "Apples, ice cream", FoodCategory.DESSERTS, null);

        when(menuRepository.getReferenceById(id)).thenReturn(menuItem);

        MenuItemDTO result = underTest.getMenuItem(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(menuItem.getId());
        assertThat(result.getName()).isEqualTo(menuItem.getName());
        assertThat(result.getPrice()).isEqualTo(menuItem.getPrice());
    }
}