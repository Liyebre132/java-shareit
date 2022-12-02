package ru.practicum.shareit.booking_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResult;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResult;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.enums.BookingStatus.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTests {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDto itemDto;

    private UserDto userDto;

    private UserDto userDto2;

    private BookingDto bookingDto;


    @BeforeEach
    void init() {
        itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("user@email.com");

        userDto2 = new UserDto();
        userDto2.setName("name2");
        userDto2.setEmail("user2@email.com");

        bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(2));
        bookingDto.setEnd(LocalDateTime.of(2023, 11, 10, 13, 0));
        bookingDto.setItemId(1L);
    }

    @Test
    void createTest() {
        UserDto user = userController.add(userDto);
        itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(userDto2);
        BookingResult booking = bookingController.add(user2.getId(), bookingDto);
        assertEquals(1L, bookingController.getById(user2.getId(), booking.getId()).getId());
    }

    @Test
    void createByWrongUserTest() {
        assertThrows(EntityNotFoundException.class, () -> bookingController.add(1L, bookingDto));
    }

    @Test
    void createForWrongItemTest() {
        userController.add(userDto);
        assertThrows(EntityNotFoundException.class, () -> bookingController.add(1L, bookingDto));
    }

    @Test
    void createByOwnerTest() {
        UserDto user = userController.add(userDto);
        itemController.add(user.getId(), itemDto);
        assertThrows(BookingNotFoundException.class, () -> bookingController.add(1L, bookingDto));
    }

    @Test
    void createToUnavailableItemTest() {
        UserDto user = userController.add(userDto);
        itemDto.setAvailable(false);
        itemController.add(user.getId(), itemDto);
        userController.add(userDto2);
        assertThrows(BookingNotAvailableException.class, () -> bookingController.add(2L, bookingDto));
    }

    @Test
    void createWithWrongEndDate() {
        UserDto user = userController.add(userDto);
        itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(userDto2);
        bookingDto.setEnd(LocalDateTime.of(2022, 9, 24, 12, 30));
        assertThrows(BookingDateException.class, () -> bookingController.add(user2.getId(), bookingDto));
    }

    @Test
    void approveTest() {
        UserDto user = userController.add(userDto);
        ItemResult item = itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(userDto2);

        BookingResult bookingResult = bookingController.add(user2.getId(), bookingDto);

        assertEquals(WAITING, bookingController.getById(user2.getId(), bookingResult.getId()).getStatus());
        bookingController.approved(user.getId(), bookingResult.getId(), true);
        assertEquals(APPROVED, bookingController.getById(user2.getId(), bookingResult.getId()).getStatus());
    }

    @Test
    void approveRejectedTest() {
        UserDto user = userController.add(userDto);
        ItemResult item = itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(userDto2);
        BookingResult bookingResult = bookingController.add(user2.getId(), bookingDto);
        bookingController.approved(user.getId(), bookingResult.getId(), false);
        assertEquals(REJECTED, bookingController.getById(user2.getId(), bookingResult.getId()).getStatus());
    }

    @Test
    void approveToWrongBookingTest() {
        assertThrows(EntityNotFoundException.class, () ->
                bookingController.approved(1L, 1L, true));
    }

    @Test
    void approveByWrongUserTest() {
        UserDto user = userController.add(userDto);
        itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(userDto2);
        bookingController.add(user2.getId(), bookingDto);
        assertThrows(BookingNotFoundException.class, () ->
                bookingController.approved(2L, 1L, true));
    }

    @Test
    void approveToBookingWithWrongStatus() {
        UserDto user = userController.add(userDto);
        itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(userDto2);
        bookingController.add(user2.getId(), bookingDto);
        bookingController.approved(1L, 1L, true);
        assertThrows(BookingIncorrectApprovedException.class, () ->
                bookingController.approved(1L, 1L, true));
    }

    @Test
    void getAllByOwnerAndBookerTest() {
        UserDto user = userController.add(userDto);
        itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(userDto2);
        BookingResult booking = bookingController.add(user2.getId(), bookingDto);

        assertEquals(1,
                bookingController.getAllByBooker(user2.getId(), "WAITING", 0, 10).size());
        assertEquals(1,
                bookingController.getAllByBooker(user2.getId(), "ALL", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByBooker(user2.getId(), "PAST", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByBooker(user2.getId(), "CURRENT", 0, 10).size());
        assertEquals(1,
                bookingController.getAllByBooker(user2.getId(), "FUTURE", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByBooker(user2.getId(), "REJECTED", 0, 10).size());

        bookingController.approved(booking.getId(), user.getId(), true);

        assertEquals(0,
                bookingController.getAllByOwner(user.getId(), "CURRENT", 0, 10).size());
        assertEquals(1,
                bookingController.getAllByOwner(user.getId(), "ALL", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByOwner(user.getId(), "WAITING", 0, 10).size());
        assertEquals(1,
                bookingController.getAllByOwner(user.getId(), "FUTURE", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByOwner(user.getId(), "REJECTED", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByOwner(user.getId(), "PAST", 0, 10).size());

        assertThrows(BookingIncorrectStateException.class, () ->
                bookingController.getAllByBooker(1L, "SSSS", 0, 10));

        assertThrows(BookingIncorrectStateException.class, () ->
                bookingController.getAllByOwner(1L, "SSS", 0, 10));

        assertThrows(BookingIncorrectStateException.class, () ->
                bookingController.getAllByBooker(user2.getId(), "SSSS", 0, 10));

        assertThrows(BookingIncorrectStateException.class, () ->
                bookingController.getAllByOwner(user.getId(), "SSS", 0, 10));
    }

    @Test
    void getAllByUserWithIncorrectParams() {
        assertThrows(ConstraintViolationException.class, () ->
                bookingController.getAllByBooker(1L, "ALL", -1, 10).size());

        assertThrows(ConstraintViolationException.class, () ->
                bookingController.getAllByBooker(1L, "ALL", -1, -1).size());

        assertThrows(ConstraintViolationException.class, () ->
                bookingController.getAllByBooker(1L, "ALL", 10, -1).size());

        assertThrows(ConstraintViolationException.class, () ->
                bookingController.getAllByOwner(1L, "ALL", -1, 10).size());

        assertThrows(ConstraintViolationException.class, () ->
                bookingController.getAllByOwner(1L, "ALL", -1, -1).size());

        assertThrows(ConstraintViolationException.class, () ->
                bookingController.getAllByOwner(1L, "ALL", 10, -1).size());
    }

    @Test
    void getAllByWrongUserTest() {
        assertThrows(UserNotFoundException.class, () ->
                bookingController.getAllByBooker(1L, "ALL", 0, 10));

        assertThrows(UserNotFoundException.class, () ->
                bookingController.getAllByOwner(1L, "ALL", 0, 10));
    }

    @Test
    void getByWrongIdTest() {
        assertThrows(EntityNotFoundException.class, () -> bookingController.getById(1L, 1L));
    }

    @Test
    void getByWrongUser() {
        UserDto user = userController.add(userDto);
        itemController.add(user.getId(), itemDto);
        UserDto user1 = userController.add(userDto2);
        bookingController.add(user1.getId(), bookingDto);

        UserDto user3 = new UserDto(3L, "user3", "m@mail.ru");
        userController.add(user3);

        assertThrows(BookingNotFoundException.class, () -> bookingController.getById(3L, 1L));
        assertThrows(BookingNotFoundException.class, () -> bookingController.getById(3L, 1L));
    }
}
