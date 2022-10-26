package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long item;
    Long booker;
    BookingStatus status;
}
