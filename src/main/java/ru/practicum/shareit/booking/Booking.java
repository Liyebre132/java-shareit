package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Booking {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long item;
    Long booker;
    BookingStatus status;
}
