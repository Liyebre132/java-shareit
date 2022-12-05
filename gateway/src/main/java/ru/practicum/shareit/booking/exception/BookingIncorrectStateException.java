package ru.practicum.shareit.booking.exception;

public class BookingIncorrectStateException extends RuntimeException {
    public BookingIncorrectStateException(String message) {
        super(message);
    }
}
