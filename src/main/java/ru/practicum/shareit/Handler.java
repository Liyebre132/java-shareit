package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.CommentIncorrectException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotValidException;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.exception.ItemRequestNotValidException;
import ru.practicum.shareit.user.exception.UserExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class Handler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> itemNotFound(final ItemNotFoundException e) {
        log.info("Не удалось найти вещь");
        return Map.of(
                "error", "NOT_FOUND",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> bookingNotFound(final BookingNotFoundException e) {
        log.info("Не удалось найти бронирование");
        return Map.of(
                "error", "NOT_FOUND",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> bookingNotAvailable(final BookingNotAvailableException e) {
        log.info("Вещь недоступна для аренды");
        return Map.of(
                "error", "BAD_REQUEST",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> bookingIncorrectApproved(final BookingIncorrectApprovedException e) {
        log.info("Вещь уже забронирована и утверждена");
        return Map.of(
                "error", "BAD_REQUEST",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> bookingIncorrectState(final BookingIncorrectStateException e) {
        log.info("Получено неверное состояние бронирования");
        return Map.of(
                "error", "Unknown state: UNSUPPORTED_STATUS",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> commentIncorrect(final CommentIncorrectException e) {
        log.info("Неудалось оставить комментарий", e.fillInStackTrace());
        return Map.of(
                "error", "BAD_REQUEST",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> bookingDateIncorrect(final BookingDateException e) {
        log.info("Попытка бронирования на время раньше времени его завершения");
        return Map.of(
                "error", "BAD_REQUEST",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> itemRequestNotValid(final ItemRequestNotValidException e) {
        log.info("Переданы неверные данные для получения всех запросов");
        return Map.of(
                "error", "BAD_REQUEST",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> itemNotValid(final ItemNotValidException e) {
        log.info("Переданы неверные данные для получения всех вещей");
        return Map.of(
                "error", "BAD_REQUEST",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> bookingNotValid(final BookingNotValidException e) {
        log.info("Переданы неверные данные для получения всех бронирований");
        return Map.of(
                "error", "BAD_REQUEST",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> userExists(final UserExistsException e) {
        log.info("Пользователь пытался зарегистрироваться на занятный email");
        return Map.of(
                "error", "CONFLICT",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userNotFound(final UserNotFoundException e) {
        log.info("Пользователь не был найден");
        return Map.of(
                "error", "NOT_FOUND",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> itemRequestNotFound(final ItemRequestNotFoundException e) {
        log.info("Запрос не был найден");
        return Map.of(
                "error", "NOT_FOUND",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(final EntityNotFoundException e) {
        log.info("Не удалось найти запрашиваемую информацию");
        return Map.of(
                "error", "NOT_FOUND",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> constraintViolation(ConstraintViolationException e) {
        log.info("Получены неверные данные, валидация не пройдена");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> methodArgumentNotValid(MethodArgumentNotValidException e) {
        log.info("Получены неверные данные, валидация не пройдена");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> throwable(Throwable e) {
        log.error("Произошлка ошибка: {}, Трейс: {}", e.getMessage(), e.fillInStackTrace());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
