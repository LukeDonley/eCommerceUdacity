package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderControllerTest {

    OrderController orderController;
    OrderRepository orderRepo = mock(OrderRepository.class);
    UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrder() {
        User user = getUser();
        when(userRepo.findByUsername("TestUser")).thenReturn(user);
        UserOrder order = orderController.submit("TestUser").getBody();

        assertNotNull(order);
        assertEquals(3, order.getItems().size());
        assertEquals(BigDecimal.valueOf(84), order.getTotal());
    }

    @Test
    public void getOrdersForUser() {
        User user = getUser();
        when(userRepo.findByUsername("TestUser")).thenReturn(user);
        List<UserOrder> orders = new ArrayList<>();
        orders.add(UserOrder.createFromCart(user.getCart()));
        when(orderRepo.findByUser(user)).thenReturn(orders);

        List<UserOrder> orderResult = orderController.getOrdersForUser("TestUser").getBody();
        assertNotNull(orderResult);
        assertEquals(1, orderResult.size());
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setPassword("TestPassword");
        user.setCart(getCart());
        return user;
    }

    private Cart getCart() {
        List<Item> items = createItems();
        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(items.get(0));
        cart.addItem(items.get(1));
        cart.addItem(items.get(2));
        return cart;
    }

    private List<Item> createItems() {
        Item book = new Item();
        book.setId(1L);
        book.setName("Very Good Book");
        book.setPrice(new BigDecimal(10));
        book.setDescription("#1 Best Seller");

        Item game = new Item();
        game.setId(2L);
        game.setName("Exciting Video Game");
        game.setPrice(new BigDecimal(60));
        game.setDescription("Fighting Game");

        Item poster = new Item();
        poster.setId(3L);
        poster.setName("Movie Poster");
        poster.setDescription("An Extra Large Movie Poster");
        poster.setPrice(new BigDecimal(14));

        return new ArrayList<>(Arrays.asList(new Item[]{book, game, poster}));
    }
}
