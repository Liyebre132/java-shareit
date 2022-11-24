package ru.practicum.shareit.item_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemResult;
import ru.practicum.shareit.item.exception.CommentIncorrectException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotValidException;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTests {
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
        itemDto = new ItemDto("name", "description", true, null);

        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("user@ya.ru");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");

        comment = new CommentDto();
        comment.setText("comment");
    }

    @Test
    void createTest() {
        UserDto user = userController.add(userDto);
        ItemResult item = itemController.add(1L, itemDto);
        assertEquals(item.getId(), itemController.getById(item.getId(), user.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        ItemDto item = new ItemDto();
        item.setName("new name");
        item.setDescription("new desc");
        item.setAvailable(false);
        itemController.update(1L, 1L, item);
        assertEquals(item.getDescription(), itemController.getById(1L, 1L).getDescription());
    }

    @Test
    void updateForWrongItemTest() {
        assertThrows(EntityNotFoundException.class, () -> itemController.update(1L, 1L, itemDto));
    }

    @Test
    void updateByWrongUserTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        itemDto.setName("update name");
        assertThrows(ItemNotFoundException.class, () -> itemController.update(10L, 1L, itemDto));
    }

    @Test
    void deleteTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        assertEquals(1, itemController.getAll(1L, 0, 10).size());
        itemController.delete(1L, 1L);
        assertEquals(0, itemController.getAll(1L, 0, 10).size());
    }

    @Test
    void searchTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        assertEquals(1, itemController.search("Desc", 0, 10).size());
    }

    @Test
    void searchEmptyTextTest() {
        userController.add(userDto);
        itemController.add(1L, itemDto);
        assertEquals(new ArrayList<ItemDto>(), itemController.search("", 0, 10));
    }

    @Test
    void searchWithWrongFrom() {
        assertThrows(ItemNotValidException.class, () -> itemController.search("t", -1, 10));
    }

    @Test
    void createCommentByWrongUser() {
        assertThrows(CommentIncorrectException.class, () -> itemController.addComment(1L, 1L, comment));
    }

    @Test
    void createCommentToWrongItem() {
        UserDto user = userController.add(userDto);
        assertThrows(CommentIncorrectException.class, () -> itemController.addComment(1L, 1L, comment));
        ItemResult item = itemController.add(1L, itemDto);
        assertThrows(CommentIncorrectException.class, () -> itemController.addComment(1L, 1L, comment));
    }

    @Test
    void getAllWithWrongFrom() {
        assertThrows(ItemNotValidException.class, () -> itemController.getAll(1L, -1, 10));
    }
}
