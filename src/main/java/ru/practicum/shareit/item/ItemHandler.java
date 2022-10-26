package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.ItemNotFoundException;

import java.util.Map;

@RestControllerAdvice
public class ItemHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> itemNotFound(final ItemNotFoundException e) {
        return Map.of(
                "error", "Вещь с таким ID не найдена",
                "errorMessage", e.getMessage()
        );
    }

}
