package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    LocalDateTime now = LocalDateTime.now();

    @Test
    void findByStateEqualsAll() {
        User user = userRepository.save(new User("User", "mail1@mail.ru"));
        Booking booking = new Booking(now.plusDays(2),now.plusDays(5),null,user,Status.APPROVED);
        bookingRepository.save(booking);
        assertFalse(bookingRepository.findByStateEqualsAll(user.getId()).isEmpty());
    }

    @Test
    void findByStateEqualsTime() {
        User user = userRepository.save(new User("User", "mail1@mail.ru"));
        bookingRepository.save(new Booking(now.minusDays(5),now.plusDays(5),null,user,Status.APPROVED));
        assertEquals(1,bookingRepository.findByStateEqualsCurrent(user.getId()).size());
        bookingRepository.save(new Booking(now.minusDays(5),now.minusDays(2),null,user,Status.APPROVED));
        assertEquals(1,bookingRepository.findByStateEqualsPast(user.getId()).size());
        bookingRepository.save(new Booking(now.plusDays(2),now.plusDays(5),null,user,Status.APPROVED));
        assertEquals(1,bookingRepository.findByStateEqualsFuture(user.getId()).size());
    }

    @Test
    void findByStateEqualsStatus() {
        User user = userRepository.save(new User("User", "mail1@mail.ru"));
        Booking booking1 = new Booking(LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),null,user,Status.WAITING);
        bookingRepository.save(booking1);
        assertEquals(1,bookingRepository.findByStateEqualsWaiting(user.getId()).size());
        Booking booking2 = new Booking(LocalDateTime.now().minusDays(5),LocalDateTime.now().plusDays(5),null,user,Status.REJECTED);
        bookingRepository.save(booking2);
        assertEquals(1,bookingRepository.findByStateEqualsRejected(user.getId()).size());
    }

    @Test
    void findByOwnerAndStateEqualsTime() {
        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Desc1");
        item.setAvailable(true);
        User user = userRepository.save(new User("User", "mail1@mail.ru"));
        item.setOwner(user);
        Item saveItem = itemRepository.save(item);
        bookingRepository.save(new Booking(now.minusDays(5),now.plusDays(5),saveItem,user,Status.APPROVED));
        assertEquals(1,bookingRepository.findByOwnerAndStateEqualsCurrent(saveItem.getOwner().getId()).size());
        bookingRepository.save(new Booking(now.minusDays(5),now.minusDays(2),saveItem,user,Status.APPROVED));
        assertEquals(1,bookingRepository.findByOwnerAndStateEqualsPast(saveItem.getOwner().getId()).size());
        bookingRepository.save(new Booking(now.plusDays(2),now.plusDays(5),saveItem,user,Status.APPROVED));
        assertEquals(1,bookingRepository.findByOwnerAndStateEqualsFuture(saveItem.getOwner().getId()).size());
    }

    @Test
    void findByOwnerAndStateEqualsAll() {
        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Desc1");
        item.setAvailable(true);
        User user = userRepository.save(new User("User", "mail1@mail.ru"));
        item.setOwner(user);
        Item saveItem = itemRepository.save(item);
        Booking booking = new Booking(now.plusDays(2),now.plusDays(5),saveItem,user,Status.APPROVED);
        bookingRepository.save(booking);
        assertFalse(bookingRepository.findByOwnerAndStateEqualsAll(saveItem.getOwner().getId()).isEmpty());
    }


    @Test
    void findByOwnerAndStateEqualsStatus() {
        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Desc1");
        item.setAvailable(true);
        User user = userRepository.save(new User("User", "mail1@mail.ru"));
        item.setOwner(user);
        Item saveItem = itemRepository.save(item);
        bookingRepository.save(new Booking(now.minusDays(5),now.plusDays(5),saveItem,user,Status.WAITING));
        assertEquals(1,bookingRepository.findByOwnerAndStateEqualsWaiting(saveItem.getOwner().getId()).size());
        bookingRepository.save(new Booking(now.minusDays(5),now.minusDays(2),saveItem,user,Status.REJECTED));
        assertEquals(1,bookingRepository.findByOwnerAndStateEqualsRejected(saveItem.getOwner().getId()).size());
    }

    @Test
    void findByItemAndBooker() {
        Item item = new Item();
        item.setName("Item1");
        item.setDescription("Desc1");
        item.setAvailable(true);
        User user = userRepository.save(new User("User", "mail1@mail.ru"));
        item.setOwner(user);
        Item saveItem = itemRepository.save(item);
        Booking booking = bookingRepository.save(new Booking(now.minusDays(5),now.minusDays(3),saveItem,user,Status.WAITING));
        assertTrue(bookingRepository.findByItemAndBooker(booking.getItem().getId(),
                        booking.getBooker().getId(),
                        LocalDateTime.now()).isPresent());
    }
}