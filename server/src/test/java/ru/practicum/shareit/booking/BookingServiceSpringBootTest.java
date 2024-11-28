package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.List;
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
    void changeStatusBookingWithApproved() {
        UserDto newUser = userService.createUser(user);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        bookingDto.setItemId(newItem.getId());
        Booking newBooking = bookingService.createBooking(newUser.getId(),bookingDto);
        assertEquals(newBooking.getStatus(),Status.WAITING);
        Booking changeStatusBooking = bookingService.changeStatusBooking(newUser.getId(),newBooking.getId(),true);
        assertEquals(changeStatusBooking.getStatus(),Status.APPROVED);
    }

    @Test
    void changeStatusBookingWithNotApproved() {
        UserDto newUser = userService.createUser(user);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        bookingDto.setItemId(newItem.getId());
        Booking newBooking = bookingService.createBooking(newUser.getId(),bookingDto);
        assertEquals(newBooking.getStatus(),Status.WAITING);
        Booking changeStatusBooking = bookingService.changeStatusBooking(newUser.getId(),newBooking.getId(),false);
        assertEquals(changeStatusBooking.getStatus(),Status.REJECTED);
    }

    @Test
    void createBookingWithWrongUser() {
        UserDto newUser = userService.createUser(user);
        assertThrows(ResponseStatusException.class,
                () -> bookingService.createBooking(newUser.getId() + 5,bookingDto));
    }

    @Test
    void createBookingWithUnavailableItem() {
        UserDto newUser = userService.createUser(user);
        itemRequestDto.setAvailable(false);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        bookingDto.setItemId(newItem.getId());
        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(newUser.getId(),bookingDto));
    }

    @Test
    void createRightBooking() {
        UserDto newUser = userService.createUser(user);
        ItemDto newItem = itemService.createItem(newUser.getId(),itemRequestDto);
        bookingDto.setItemId(newItem.getId());
        Booking newBooking = bookingService.createBooking(newUser.getId(),bookingDto);
        assertEquals(bookingDto.getStart(),newBooking.getStart());
        assertEquals(bookingDto.getEnd(),newBooking.getEnd());
        assertEquals(newBooking.getStatus(),Status.WAITING);
    }

    @Test
    void findByStateAllVariants() {
        List<Booking> bookings;
        bookings = bookingService.findByState(1L,State.ALL.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByState(1L,State.CURRENT.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByState(1L,State.PAST.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByState(1L,State.FUTURE.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByState(1L,State.WAITING.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByState(1L,State.REJECTED.toString());
        assertEquals(0,bookings.size());
        assertThrows(IllegalArgumentException.class,() -> bookingService.findByState(1L,"Present"));
    }

    @Test
    void findByOwnerAndStateAllVariants() {
        List<Booking> bookings;
        bookings = bookingService.findByOwnerAndState(1L,State.ALL.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByOwnerAndState(1L,State.CURRENT.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByOwnerAndState(1L,State.PAST.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByOwnerAndState(1L,State.FUTURE.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByOwnerAndState(1L,State.WAITING.toString());
        assertEquals(0,bookings.size());
        bookings = bookingService.findByOwnerAndState(1L,State.REJECTED.toString());
        assertEquals(0,bookings.size());
        assertThrows(IllegalArgumentException.class,() -> bookingService.findByOwnerAndState(1L,"Present"));
    }
}