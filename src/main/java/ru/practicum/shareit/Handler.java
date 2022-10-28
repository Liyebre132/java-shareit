package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class Handler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> itemNotFound(final ItemNotFoundException e) {
        log.info("Не удалось найти вещь");
        return Map.of(
                "error", "Вещь с таким ID не найдена",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> userExists(final UserExistsException e) {
        log.info("Пользователь пытался зарегистрироваться на занятный email");
        return Map.of(
                "error", "Пользователь с таким email уже существует",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotFount(final UserNotFoundException e) {
        log.info("Не удалось найти пользователя");
        return Map.of(
                "error", "Пользователь с таким ID не найден",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException e){
        log.info("Получены неверные данные, валидация не пройдена");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> throwable(Throwable e){
        log.error("Произошлка ошибка");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
