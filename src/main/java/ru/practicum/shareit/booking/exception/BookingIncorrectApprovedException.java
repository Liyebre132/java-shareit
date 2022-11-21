package ru.practicum.shareit.booking.exception;

public class BookingIncorrectApprovedException extends RuntimeException {
    public BookingIncorrectApprovedException(String message) {
        super(message);
    }
}
