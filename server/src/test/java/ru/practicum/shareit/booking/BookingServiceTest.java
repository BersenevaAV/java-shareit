package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private BookingService bookingService;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingService(bookingRepository, userRepository, itemRepository);
    }

    @Test
    void changeStatusBooking() {
        Booking booking = bookingService.changeStatusBooking(1L,2L,true);
    }

}