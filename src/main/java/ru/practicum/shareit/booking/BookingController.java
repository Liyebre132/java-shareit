package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.exception.BookingDateException;
import ru.practicum.shareit.booking.exception.BookingNotValidException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResult add(@RequestHeader("X-Sharer-User-Id") long userId,
                             @Valid @RequestBody BookingDto bookingDto) {
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            throw new BookingDateException("Неверное время бронирования");
        }
        return bookingService.addNewBooking(userId, bookingDto);
    }

    @GetMapping("{id}")
    public BookingResult getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return bookingService.getById(userId, id);
    }

    @GetMapping
    public List<BookingResult> getAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size < 0) {
            throw new BookingNotValidException("Некорректно переданы данные для поиска");
        }
        return bookingService.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResult> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size < 0) {
            throw new BookingNotValidException("Некорректно переданы данные для поиска");
        }
        return bookingService.getAllByOwner(userId, state, from, size);
    }

    @PatchMapping("{bookingId}")
    public BookingResult approved(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId,
                                  @RequestParam boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }
}
