package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResult;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingResult toBookingResult(Booking booking) {
        return new BookingResult(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingResult.Item(booking.getItem().getId(),
                        booking.getItem().getName()),
                new BookingResult.Booker(booking.getBooker().getId(),
                        booking.getBooker().getName()),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User user, BookingStatus status) {
        return new Booking(
                0L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user,
                status
        );
    }

    public static List<BookingResult> mapToBookingResult(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingResult)
                .collect(Collectors.toList());
    }
}
