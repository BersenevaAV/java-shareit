package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
class BookingServiceSpringBootTest {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookingService bookingService;
    private ItemRequestDto itemRequestDto;
    private User user;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("user");
        user.setEmail("us@mail.ru");
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("item");
        itemRequestDto.setDescription("itemDesc");
        itemRequestDto.setAvailable(true);
        bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(3));
    }

    @Test
    void changeStatusBooking() {
        UserDto newUser = userService.createUser(user);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        bookingDto.setItemId(newItem.getId());
        Booking newBooking = bookingService.createBooking(newUser.getId(),bookingDto);
        assertEquals(bookingDto.getStart(),newBooking.getStart());
        assertEquals(bookingDto.getEnd(),newBooking.getEnd());
        assertEquals(newBooking.getStatus(),Status.WAITING);
        Booking changeStatusBooking = bookingService.changeStatusBooking(newUser.getId(),newBooking.getId(),true);
        assertEquals(changeStatusBooking.getStatus(),Status.APPROVED);
    }
}