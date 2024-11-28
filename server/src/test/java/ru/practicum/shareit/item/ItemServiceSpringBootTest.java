package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
class ItemServiceSpringBootTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserService userService;
    private ItemRequestDto itemRequestDto;
    private ItemRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("user");
        user.setEmail("us@mail.ru");
        request = new ItemRequest();
        request.setDescription("request");
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item");
        itemRequestDto.setDescription("itemDesc");
        itemRequestDto.setAvailable(true);
    }

    @Test
    void findById() {
        UserDto newUser = userService.createUser(user);
        itemRequestService.createRequest(newUser.getId(),request);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        ItemDto findItem = itemService.findById(newItem.getId());
        assertEquals("item",findItem.getName());
    }

    @Test
    void findOfRightText() {
        UserDto newUser = userService.createUser(user);
        itemRequestService.createRequest(newUser.getId(),request);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        List<ItemDto> itemsOfText = itemService.findOfText("item");
        assertTrue(itemsOfText.contains(newItem));
    }

    @Test
    void findOfEmptyText() {
        UserDto newUser = userService.createUser(user);
        itemRequestService.createRequest(newUser.getId(),request);
        itemService.createItem(newUser.getId(),itemRequestDto);
        List<ItemDto> itemsOfText = itemService.findOfText("");
        assertTrue(itemsOfText.isEmpty());
    }

    @Test
    void updateWithEmptyName() {
        UserDto newUser = userService.createUser(user);
        itemRequestService.createRequest(newUser.getId(),request);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        ItemDto updatedItem = itemService.updateItem(newItem.getId(),new Item(), newUser.getId());
        assertEquals("item",updatedItem.getName());
        assertEquals("itemDesc",updatedItem.getDescription());
    }
}