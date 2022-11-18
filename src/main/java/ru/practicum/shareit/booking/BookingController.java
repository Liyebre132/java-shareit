package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.exception.BookingDateException;

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
        if (bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new BookingDateException("Время старта не должно быть одинаковым с временем завершения");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingDateException("Время начала бронирования раньше времени его завершения");
        }
        return bookingService.addNewBooking(userId, bookingDto);
    }

    @GetMapping("{id}")
    public BookingResult getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return bookingService.getById(userId, id);
    }

    @GetMapping
    public List<BookingResult> getAllByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResult> getAllByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(userId, state);
    }

    @PatchMapping("{bookingId}")
    public BookingResult approved(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId,
                                  @RequestParam boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }
}
