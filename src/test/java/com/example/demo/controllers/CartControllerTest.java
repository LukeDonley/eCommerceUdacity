package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private List<Item> items;
    private ModifyCartRequest modifyCartRequest;

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        User user = getUser();
        when(userRepo.findByUsername("TestUser")).thenReturn(user);
        items = createItems();

        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(items.get(0)));
        when(itemRepo.findById(2L)).thenReturn(java.util.Optional.ofNullable(items.get(1)));

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("TestUser");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(3);

        cartController.addTocart(modifyCartRequest);
    }

    @Test
    public void addToCart() {
        modifyCartRequest.setItemId(2L);
        modifyCartRequest.setQuantity(2);

        Cart cart = cartController.addTocart(modifyCartRequest).getBody();

        assertEquals(5, cart.getItems().size());
        assertEquals("Exciting Video Game", cart.getItems().get(4).getName());
    }

    @Test
    public void removeFromCart() {
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        Cart cart = cartController.removeFromcart(modifyCartRequest).getBody();

        assertEquals(2, cart.getItems().size());
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
        Cart cart = new Cart();
        cart.setId(1L);
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

        return new ArrayList<>(Arrays.asList(new Item[]{book, game}));
    }
}
