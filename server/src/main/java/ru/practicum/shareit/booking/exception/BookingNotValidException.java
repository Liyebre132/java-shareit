package ru.practicum.shareit.booking.exception;

public class BookingNotValidException extends RuntimeException {
    public BookingNotValidException(String message) {
        super(message);
    }
}
