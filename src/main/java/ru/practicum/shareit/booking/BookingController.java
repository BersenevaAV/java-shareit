package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody BookingDto booking) {
        return bookingService.createBooking(userId, booking);
    }

    @GetMapping("/{bookingId}")
    public Booking findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @PathVariable Long bookingId) {
        return bookingService.findById(bookingId);
    }

    @PatchMapping("/{bookingId}")
    public Booking changeStatusBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId,
                                       @RequestParam("approved") boolean approved) {
        return bookingService.changeStatusBooking(userId, bookingId, approved);
    }

    @GetMapping
    public List<Booking> findByState(@RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findByState(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> findByOwnerAndState(@RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findByOwnerAndState(userId, state);
    }
}
