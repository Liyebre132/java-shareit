package ru.practicum.shareit.request.exception;

public class ItemRequestNotValidException extends RuntimeException {
    public ItemRequestNotValidException(String message) {
        super(message);
    }
}
