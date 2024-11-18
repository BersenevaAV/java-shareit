package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Booking createBooking(Long userId, BookingDto booking) {
        log.info("Пришел запрос на создание бронирования вещи");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Вещь не найдена"));
        if (item.getAvailable().booleanValue() == false)
            throw new ValidationException("Вещь не доступна");
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(BookingMapper.fromBookingDto(booking,item,user));
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Бронирование не найдено"));
    }

    public Booking changeStatusBooking(Long userId, Long bookingId, boolean approved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не корректен"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Бронирование не найдено"));
        if (approved)
            booking.setStatus(Status.APPROVED);
        else
            booking.setStatus(Status.REJECTED);
        return bookingRepository.save(booking);
    }

    public List<Booking> findByState(Long userId, String state) {
        if (State.valueOf(state).equals(State.ALL))
            return bookingRepository.findByStateEqualsAll(userId);
        if (State.valueOf(state).equals(State.CURRENT))
            return bookingRepository.findByStateEqualsCurrent(userId);
        if (State.valueOf(state).equals(State.PAST))
            return bookingRepository.findByStateEqualsPast(userId);
        if (State.valueOf(state).equals(State.FUTURE))
            return bookingRepository.findByStateEqualsFuture(userId);
        if (State.valueOf(state).equals(State.WAITING))
            return bookingRepository.findByStateEqualsWaiting(userId);
        if (State.valueOf(state).equals(State.REJECTED))
            return bookingRepository.findByStateEqualsRejected(userId);
        throw new IllegalStateException("Неправильно заданы данные");
    }

    public List<Booking> findByOwnerAndState(Long userId, String state) {
        if (State.valueOf(state).equals(State.ALL))
            return bookingRepository.findByOwnerAndStateEqualsAll(userId);
        if (State.valueOf(state).equals(State.CURRENT))
            return bookingRepository.findByOwnerAndStateEqualsCurrent(userId);
        if (State.valueOf(state).equals(State.PAST))
            return bookingRepository.findByOwnerAndStateEqualsPast(userId);
        if (State.valueOf(state).equals(State.FUTURE))
            return bookingRepository.findByOwnerAndStateEqualsFuture(userId);
        if (State.valueOf(state).equals(State.WAITING))
            return bookingRepository.findByOwnerAndStateEqualsWaiting(userId);
        if (State.valueOf(state).equals(State.REJECTED))
            return bookingRepository.findByOwnerAndStateEqualsRejected(userId);
        throw new IllegalStateException("Неправильно заданы данные");
    }
}
