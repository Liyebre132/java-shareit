package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

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

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User user, BookingStatus status) {
        return new Booking(
                0L, // захардкодил 0L тк он же обязателен и передавать чет надо, умней ничего не придумал :D
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user,
                status
        );
    }

    public static List<BookingDto> mapToBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static List<BookingResult> mapToBookingResult(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingResult)
                .collect(Collectors.toList());
    }
}
