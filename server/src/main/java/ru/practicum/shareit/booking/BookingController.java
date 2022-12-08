package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResult;
import ru.practicum.shareit.booking.exception.BookingDateException;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResult add(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestBody BookingDto bookingDto) {
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
        return bookingService.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResult> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllByOwner(userId, state, from, size);
    }

    @PatchMapping("{bookingId}")
    public BookingResult approved(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId,
                                  @RequestParam boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }
}
