package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.dto.OrderDTO;
import com.example.democafeclientsystem.dto.OrderResponse;
import com.example.democafeclientsystem.entities.MenuItem;
import com.example.democafeclientsystem.entities.Order;
import com.example.democafeclientsystem.entities.OrderItem;
import com.example.democafeclientsystem.entities.User;
import com.example.democafeclientsystem.enums.FoodCategory;
import com.example.democafeclientsystem.enums.OrderStatus;
import com.example.democafeclientsystem.repositories.MenuRepository;
import com.example.democafeclientsystem.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
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

    @Test
    void canGetNoActiveOrderByAuth() {
        when(orderRepository.findByUserAndOrderStatusNot(user, OrderStatus.ARCHIVE)).thenReturn(null);

        OrderDTO result = underTest.getActiveOrderByAuth(authentication);

        assertThat(result).isEqualTo(null);
    }

    @Test
    void canAddOrderByAuth() {
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

        underTest.addOrderByAuth(authentication, orderDTO);

        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(argumentCaptor.capture());
        Order capturedOrder = argumentCaptor.getValue();

        assertThat(capturedOrder.getTableNumber()).isEqualTo(2);
        assertThat(capturedOrder.getUser()).isEqualTo(order.getUser());
        assertThat(capturedOrder.getItems().size()).isEqualTo(order.getItems().size());
    }

    @Test
    void getAllOrders() {
        Order order = Order
                .builder()
                .user(user)
                .tableNumber(2)
                .items(Set.of(orderItem1))
                .orderStatus(OrderStatus.NEW)
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponse> orderResponse = underTest.getAllOrders();

        assertThat(orderResponse.size()).isEqualTo(1);
        assertThat(orderResponse.get(0).getTableNumber()).isEqualTo(order.getTableNumber());
        assertThat(orderResponse.get(0).getMenuItems().length).isEqualTo(order.getItems().size());
    }

    @Test
    void getAllActiveOrders() {
        Order order1 = Order
                .builder()
                .id(1L)
                .tableNumber(11)
                .orderStatus(OrderStatus.IN_PROCESS)
                .items(Set.of(orderItem2))
                .build();
        Order order2 = Order
                .builder()
                .id(3L)
                .tableNumber(13)
                .orderStatus(OrderStatus.IN_PROCESS)
                .items(Set.of(orderItem1))
                .build();

        when(orderRepository.findByOrderStatusIs(OrderStatus.IN_PROCESS)).thenReturn(List.of(order1, order2));

        List<OrderResponse> result = underTest.getOrdersByStatus(OrderStatus.IN_PROCESS);

        assertThat(result.get(0).getTableNumber()).isEqualTo(order1.getTableNumber());
        assertThat(result.get(1).getTableNumber()).isEqualTo(order2.getTableNumber());

        assertThat(result.get(0).getMenuItems()[0].getMenuItem().getId()).isEqualTo(orderItem2.getMenuItem().getId());
        assertThat(result.get(1).getMenuItems()[0].getMenuItem().getId()).isEqualTo(orderItem1.getMenuItem().getId());

        assertThat(result.size()).isEqualTo(2);
    }
}