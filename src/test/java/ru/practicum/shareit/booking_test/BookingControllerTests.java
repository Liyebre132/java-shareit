package ru.practicum.shareit.booking_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingResult;
import ru.practicum.shareit.booking.exception.BookingDateException;
import ru.practicum.shareit.booking.exception.BookingIncorrectApprovedException;
import ru.practicum.shareit.booking.exception.BookingNotAvailableException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemResult;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.request.exception.ItemRequestNotValidException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

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
        bookingDto.setStart(LocalDateTime.of(2022, 10, 24, 12, 30));
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

        BookingDto booking = new BookingDto();
        booking.setStart(LocalDateTime.of(2022, 10, 24, 12, 30));
        booking.setEnd(LocalDateTime.of(2022, 11, 10, 13, 0));
        booking.setItemId(item.getId());
        BookingResult bookingResult = bookingController.add(user2.getId(), booking);

        assertEquals(WAITING, bookingController.getById(user2.getId(), bookingResult.getId()).getStatus());
        bookingController.approved(user.getId(), bookingResult.getId(), true);
        assertEquals(APPROVED, bookingController.getById(user2.getId(), bookingResult.getId()).getStatus());
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
    void getAllByUserTest() {
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
        assertEquals(0,
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
        assertEquals(0,
                bookingController.getAllByOwner(user.getId(), "FUTURE", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByOwner(user.getId(), "REJECTED", 0, 10).size());
        assertEquals(0,
                bookingController.getAllByOwner(user.getId(), "PAST", 0, 10).size());
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
        assertThrows(EntityNotFoundException.class, () -> bookingController.getById(1L, 10L));
    }
}
