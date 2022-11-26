package ru.practicum.shareit.item_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingResult;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemResult;
import ru.practicum.shareit.item.exception.CommentIncorrectException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotValidException;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTests {

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemRequestController itemRequestController;

    private ItemDto itemDto;

    private UserDto userDto;

    private ItemRequestDto itemRequestDto;

    private CommentDto comment;

    @BeforeEach
    void init() {
        itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("user@email.com");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("item request description");

        comment = new CommentDto();
        comment.setText("first comment");
    }

    @Test
    void addTest() {
        UserDto user = userController.add(userDto);
        ItemResult item = itemController.add(1L, itemDto);
        assertEquals(item.getId(), itemController.getById(item.getId(), user.getId()).getId());
    }

    @Test
    void addByRequestTest() {
        UserDto user = userController.add(userDto);
        ItemRequestDto itemRequest = itemRequestController.add(user.getId(), itemRequestDto);
        itemDto.setRequestId(1L);
        UserDto user2 = new UserDto();
        user2.setEmail("user2@mail.ru");
        user2.setName("user2");
        userController.add(user2);
        ItemResult item = itemController.add(2L, itemDto);
        assertEquals(item.getId(), itemController.getById(2L, 1L).getId());
    }

    @Test
    void addByWrongUser() {
        assertThrows(UserNotFoundException.class, () -> itemController.add(1L, itemDto));
    }

    @Test
    void addByWrongRequest() {
        itemDto.setRequestId(99L);
        userController.add(userDto);
        assertThrows(ItemRequestNotFoundException.class, () -> itemController.add(1L, itemDto));
    }

    @Test
    void updateTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        ItemDto updateItem = new ItemDto();
        updateItem.setName("updateName");
        updateItem.setDescription("updateDesc");
        updateItem.setAvailable(false);
        itemController.update(1L, 1L, updateItem);
        assertEquals(updateItem.getDescription(), itemController.getById(1L, 1L).getDescription());
        assertEquals(updateItem.getName(), itemController.getById(1L, 1L).getName());
    }

    @Test
    void updateWrongItemTest() {
        assertThrows(EntityNotFoundException.class, () -> itemController.update(1L, 1L, itemDto));
    }

    @Test
    void updateByWrongUserTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);

        ItemDto updateItem = new ItemDto();
        updateItem.setName("updateName");
        updateItem.setDescription("updateDesc");

        assertThrows(ItemNotFoundException.class, () -> itemController.update(10L, 1L, updateItem));
    }

    @Test
    void getByIdTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        assertEquals(itemDto.getName(), itemController.getById(1L, 1L).getName());
    }

    @Test
    void getByIdWithNextAndLastBookingTest() {
        UserDto user = userController.add(userDto);
        ItemResult item = itemController.add(1L, itemDto);

        UserDto user2 = userController.add(new UserDto(null, "user2", "fff@mail.ru"));
        BookingResult booking = bookingController.add(user2.getId(), new BookingDto(
                LocalDateTime.of(2021, 12, 13, 10, 30),
                LocalDateTime.of(2022, 2, 1, 10, 30),
                item.getId()
        ));
        bookingController.approved(user.getId(), booking.getId(), true);

        BookingResult booking2 = bookingController.add(user2.getId(), new BookingDto(
                LocalDateTime.of(2023, 2, 2, 10, 30),
                LocalDateTime.of(2023, 3, 2, 10, 30),
                item.getId()
        ));
        bookingController.approved(user.getId(), booking2.getId(), true);

        assertEquals(1L, itemController.getById(1L, 1L).getLastBooking().getId());
        assertEquals(2L, itemController.getById(1L, 1L).getLastBooking().getBookerId());
        assertEquals(2L, itemController.getById(1L, 1L).getNextBooking().getId());
        assertEquals(2L, itemController.getById(1L, 1L).getNextBooking().getBookerId());
    }

    @Test
    void getByWrongIdTest() {
        userController.add(userDto);
        assertThrows(EntityNotFoundException.class, () -> itemController.getById(1L, 1L));
    }

    @Test
    void getAllTest() {
        userController.add(userDto);
        assertEquals(0, itemController.getAll(1L, 0, 10).size());
        itemController.add(1L, itemDto);
        assertEquals(1, itemController.getAll(1L, 0, 10).size());
    }

    @Test
    void getAllWithNextAndLastBookingTest() {
        UserDto user = userController.add(userDto);
        ItemResult item = itemController.add(1L, itemDto);
        UserDto user2 = userController.add(new UserDto(null, "user2", "fff@mail.ru"));
        BookingResult booking = bookingController.add(user2.getId(), new BookingDto(
                LocalDateTime.of(2021, 12, 13, 10, 30),
                LocalDateTime.of(2022, 2, 1, 10, 30),
                item.getId()
        ));
        bookingController.approved(user.getId(), booking.getId(), true);
        BookingResult booking2 = bookingController.add(user2.getId(), new BookingDto(
                LocalDateTime.of(2023, 2, 2, 10, 30),
                LocalDateTime.of(2023, 3, 2, 10, 30),
                item.getId()
        ));
        bookingController.approved(user.getId(), booking2.getId(), true);

        assertEquals(1L,
                itemController.getAll(1L, 0, 10).get(0).getLastBooking().getId());
        assertEquals(2L,
                itemController.getAll(1L, 0, 10).get(0).getLastBooking().getBookerId());
        assertEquals(2L,
                itemController.getAll(1L, 0, 10).get(0).getNextBooking().getId());
    }

    @Test
    void getAllWithCommentsTest() {
        UserDto user = userController.add(userDto);
        ItemResult itemResult = itemController.add(user.getId(), itemDto);
        UserDto user2 = userController.add(new UserDto(null, "user2", "fff@mail.ru"));
        BookingResult booking = bookingController.add(user2.getId(), new BookingDto(
                LocalDateTime.of(2021, 12, 13, 10, 30),
                LocalDateTime.of(2022, 2, 1, 10, 30),
                itemResult.getId()
        ));
        bookingController.approved(user.getId(), booking.getId(), true);
        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment");
        itemController.addComment(user2.getId(), itemResult.getId(), commentDto);

        assertEquals(1,
                itemController.getAll(1L, 0, 10).get(0).getComments().size());
    }

    @Test
    void getAllWithIncorrectParamTest() {
        assertThrows(ItemNotValidException.class, () -> itemController.getAll(1L, -1, -1));
    }

    @Test
    void searchTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        assertEquals(1, itemController.search("Desc", 0, 10).size());
    }

    @Test
    void searchEmptyTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        assertEquals(0, itemController.search("", 0, 10).size());
    }

    @Test
    void searchWrongDataTest() {
        assertThrows(ItemNotValidException.class, () -> itemController.search("t", -1, 10));
    }

    @Test
    void addCommentTest() {
        UserDto user = userController.add(userDto);
        ItemResult itemResult = itemController.add(user.getId(), itemDto);

        UserDto user2 = userController.add(new UserDto(null, "user2", "fff@mail.ru"));
        BookingResult booking = bookingController.add(user2.getId(), new BookingDto(
                LocalDateTime.of(2021, 12, 13, 10, 30),
                LocalDateTime.of(2022, 2, 1, 10, 30),
                itemResult.getId()
        ));

        bookingController.approved(user.getId(), booking.getId(), true);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("comment");
        itemController.addComment(user2.getId(), itemResult.getId(), commentDto);

        assertEquals(1, itemController.getById(user.getId(), itemResult.getId()).getComments().size());
    }

    @Test
    void addIncorrectComment() {
        assertThrows(CommentIncorrectException.class, () ->
                itemController.addComment(1L, 1L, new CommentDto()));
    }

    @Test
    void deleteTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        assertEquals(1, itemController.getAll(1L, 0, 10).size());
        itemController.delete(1L, 1L);
        assertEquals(0, itemController.getAll(1L, 0, 10).size());
    }
}
