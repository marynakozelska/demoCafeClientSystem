package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.OrderDTO;
import com.example.democafeclientsystem.entities.MenuItem;
import com.example.democafeclientsystem.entities.Order;
import com.example.democafeclientsystem.entities.OrderItem;
import com.example.democafeclientsystem.entities.User;
import com.example.democafeclientsystem.enums.FoodCategory;
import com.example.democafeclientsystem.repositories.MenuRepository;
import com.example.democafeclientsystem.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private OrderService underTest;

    static User user;
    static Authentication authentication;
    static OrderItem orderItem1;
    static OrderItem orderItem2;
    static MenuItem menuItem1;
    static MenuItem menuItem2;

    @BeforeAll
    static void beforeAll() {
        user = User
                .builder()
                .firstName("Andriy")
                .email("andriy@gmail.com")
                .build();
        authentication = new UsernamePasswordAuthenticationToken(user, null);

        menuItem1 = new MenuItem(1L, "Margarita", 255f, null, FoodCategory.PIZZA, null);
        menuItem2 = new MenuItem(2L, "Spaghetti", 180f, null, FoodCategory.SIDE_DISH, null);
        orderItem1 = new OrderItem(menuItem1, 1);
        orderItem2 = new OrderItem(menuItem2, 4);
    }

    @BeforeEach
    void setUp() {
        underTest = new OrderService(orderRepository, menuRepository);
    }

    @Test
    void canGetActiveOrderByAuth() {
//        given
        Order order = Order
                .builder()
                .tableNumber(11)
                .user(user)
                .items(Set.of(orderItem1))
                .isActive(true)
                .build();
        when(orderRepository.findByUserAndIsActiveTrue(user)).thenReturn(order);

//        when
        OrderDTO result = underTest.getActiveOrderByAuth(authentication);

//        then
        assertThat(result.getTableNumber()).isEqualTo(order.getTableNumber());
        assertThat(result.getItems().length).isEqualTo(order.getItems().size());
    }

    @Test
    void canGetNoActiveOrderByAuth() {
//        given
        when(orderRepository.findByUserAndIsActiveTrue(user)).thenReturn(null);

//        when
        OrderDTO result = underTest.getActiveOrderByAuth(authentication);

//        then
        assertThat(result).isEqualTo(null);
    }

    @Test
    void canAddOrderByAuth() {
//        given
        OrderDTO orderDTO = OrderDTO
                .builder()
                .tableNumber(2)
                .items(new long[]{1L})
                .build();
        Order order = Order
                .builder()
                .tableNumber(2)
                .items(Set.of(orderItem1))
                .user(user)
                .build();

        when(menuRepository.findById(1L)).thenReturn(Optional.ofNullable(menuItem1));
        when(orderRepository.save(any())).thenReturn(order);

//        when
        underTest.addOrderByAuth(authentication, orderDTO);

//        then
        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(argumentCaptor.capture());
        Order capturedOrder = argumentCaptor.getValue();

        assertThat(capturedOrder.getTableNumber()).isEqualTo(2);
        assertThat(capturedOrder.getUser()).isEqualTo(order.getUser());
        assertThat(capturedOrder.getItems().size()).isEqualTo(order.getItems().size());
    }

    @Test
    void getAllOrders() {
//        given
        Order order = Order
                .builder()
                .user(user)
                .tableNumber(2)
                .items(Set.of(orderItem1))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

//        when
        List<OrderDTO> orderDTOS = underTest.getAllOrders();

//        then
        assertThat(orderDTOS.size()).isEqualTo(1);
        assertThat(orderDTOS.get(0).getTableNumber()).isEqualTo(order.getTableNumber());
        assertThat(orderDTOS.get(0).getItems().length).isEqualTo(order.getItems().size());
    }

    @Test
    void getAllActiveOrders() {
//        given
        Order order1 = Order
                .builder()
                .id(1L)
                .isActive(true)
                .tableNumber(11)
                .items(Set.of(orderItem2))
                .build();
        Order order2 = Order
                .builder()
                .id(3L)
                .isActive(true)
                .tableNumber(13)
                .items(Set.of(orderItem1))
                .build();

        when(orderRepository.findByIsActiveTrue()).thenReturn(List.of(order1, order2));

//        when
        List<OrderDTO> result = underTest.getAllActiveOrders();

//        then
        assertThat(result.get(0).getTableNumber()).isEqualTo(order1.getTableNumber());
        assertThat(result.get(1).getTableNumber()).isEqualTo(order2.getTableNumber());

        assertThat(result.get(0).getItems()[0]).isEqualTo(orderItem2.getMenuItem().getId());
        assertThat(result.get(1).getItems()[0]).isEqualTo(orderItem1.getMenuItem().getId());

        assertThat(result.size()).isEqualTo(2);
    }
}