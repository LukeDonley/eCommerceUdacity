package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItems() {
        when(itemRepo.findAll()).thenReturn(createItems());
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response.getBody());
        assertEquals("Very Good Book", response.getBody().get(0).getName());
        assertEquals("Fighting Game", response.getBody().get(1).getDescription());
    }

    @Test
    public void getItemById() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(createItems().get(0)));
        when(itemRepo.findById(2L)).thenReturn(Optional.ofNullable(createItems().get(1)));
        ResponseEntity<Item> response1 = itemController.getItemById(1L);
        assertNotNull(response1.getBody());
        assertEquals("Very Good Book", response1.getBody().getName());

        ResponseEntity<Item> response2 = itemController.getItemById(2L);
        assertNotNull(response2.getBody());
        assertEquals("Exciting Video Game", response2.getBody().getName());
    }

    @Test
    public void getItemsByName() {
        when(itemRepo.findByName("Very Good Book")).thenReturn(createItems());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Very Good Book");
        assertNotNull(response.getBody());
        assertEquals("Very Good Book", response.getBody().get(0).getName());
        assertEquals("#1 Best Seller", response.getBody().get(0).getDescription());

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
        game.setPrice(new BigDecimal(59.99));
        game.setDescription("Fighting Game");

        return new ArrayList<>(Arrays.asList(new Item[]{book, game}));
    }
}
