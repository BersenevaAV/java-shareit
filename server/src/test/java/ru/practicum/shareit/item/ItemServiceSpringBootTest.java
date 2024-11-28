package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
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
    @Autowired
    private BookingService bookingService;
    private ItemRequestDto itemRequestDto;
    private ItemRequest request;
    private User user;
    private BookingDto bookingDto;

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
        bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().minusDays(3));
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));
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
    void createRightItem() {
        UserDto newUser = userService.createUser(user);
        itemRequestService.createRequest(newUser.getId(),request);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        assertEquals("item",newItem.getName());
        assertEquals("itemDesc",newItem.getDescription());
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

    @Test
    void getAllItems() {
        UserDto newUser = userService.createUser(user);
        itemRequestService.createRequest(newUser.getId(),request);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        assertEquals(1,itemService.getAll(newUser.getId()).size());
    }

    @Test
    void createComment() {
        UserDto newUser1 = userService.createUser(user);
        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("us2@mail.ru");
        UserDto newUser2 = userService.createUser(user);
        ItemDto newItem = itemService.createItem(newUser1.getId(),itemRequestDto);
        bookingDto.setItemId(newItem.getId());
        bookingService.createBooking(newUser2.getId(),bookingDto);
        Comment comment = new Comment();
        comment.setText("Ok");
        CommentDto newComment = itemService.createComment(newItem.getId(),newUser2.getId(),comment);
        assertEquals("Ok",newComment.getText());
    }
}