package ru.practicum.shareit.booking;

import java.util.List;

interface BookingService {
    BookingResult addNewBooking(long userId, BookingDto bookingDto);

    BookingResult approved(long userId, long bookingId, boolean approved);

    BookingResult getById(long userId, long id);

    List<BookingResult> getAllByBooker(long userId, String state, int from, int size);

    List<BookingResult> getAllByOwner(long userId, String state, int from, int size);

}
