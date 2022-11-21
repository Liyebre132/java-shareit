package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.exception.BookingIncorrectStateException;

public enum BookingStates {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingStates findByName(String name) {
        BookingStates result = null;
        for (BookingStates state : values()) {
            if (state.name().equalsIgnoreCase(name)) {
                result = state;
                break;
            }
        }
        if (result == null) {
            throw new BookingIncorrectStateException("UNSUPPORTED_STATUS");
        }
        return result;
    }
}
