package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.CommentIncorrectException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotValidException;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.exception.ItemRequestNotValidException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class HandlerTest {

    @Autowired
    private Handler handler;

    @Test
    void handleItemNotFoundException() {
        var ex = new ItemNotFoundException("NOT_FOUND");
        var result = handler.itemNotFound(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleBookingNotFoundException() {
        var ex = new BookingNotFoundException("msg");
        var result = handler.bookingNotFound(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleBookingNotAvailableException() {
        var ex = new BookingNotAvailableException("msg");
        var result = handler.bookingNotAvailable(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleBookingIncorrectApprovedException() {
        var ex = new BookingIncorrectApprovedException("msg");
        var result = handler.bookingIncorrectApproved(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleBookingIncorrectStateException() {
        var ex = new BookingIncorrectStateException("msg");
        var result = handler.bookingIncorrectState(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleCommentIncorrectException() {
        var ex = new CommentIncorrectException("msg");
        var result = handler.commentIncorrect(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleBookingDateException() {
        var ex = new BookingDateException("msg");
        var result = handler.bookingDateIncorrect(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleItemRequestNotValidException() {
        var ex = new ItemRequestNotValidException("msg");
        var result = handler.itemRequestNotValid(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleItemNotValidException() {
        var ex = new ItemNotValidException("msg");
        var result = handler.itemNotValid(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleBookingNotValidException() {
        var ex = new BookingNotValidException("msg");
        var result = handler.bookingNotValid(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleUserNotFoundException() {
        var ex = new UserNotFoundException("msg");
        var result = handler.userNotFound(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleItemRequestNotFoundException() {
        var ex = new ItemRequestNotFoundException("msg");
        var result = handler.itemRequestNotFound(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }

    @Test
    void handleEntityNotFoundException() {
        var ex = new EntityNotFoundException("msg");
        var result = handler.notFound(ex);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsValue(ex.getMessage()));
    }
}
