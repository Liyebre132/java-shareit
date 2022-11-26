package ru.practicum.shareit.item_request_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestResult;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.exception.ItemRequestNotValidException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestControllerTests {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    private ItemRequestDto itemRequestDto;

    private UserDto userDto;

    @BeforeEach
    void init() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("item request description");

        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("user@email.com");
    }

    @Test
    void createTest() {
        UserDto user = userController.add(userDto);
        ItemRequestDto itemRequest = itemRequestController.add(user.getId(), itemRequestDto);
        assertEquals(1L, itemRequestController.getById(itemRequest.getId(), user.getId()).getId());
    }

    @Test
    void createByWrongUserTest() {
        assertThrows(UserNotFoundException.class, () -> itemRequestController.add(1L, itemRequestDto));
    }

    @Test
    void getByIdTest() {
        UserDto user = userController.add(userDto);
        ItemRequestDto itemRequest = itemRequestController.add(user.getId(), itemRequestDto);
        assertEquals(1L, itemRequestController.getById(user.getId(), itemRequest.getId()).getId());
    }

    @Test
    void getByIdWithNotUserTest() {
        UserDto user = userController.add(userDto);
        ItemRequestDto itemRequest = itemRequestController.add(user.getId(), itemRequestDto);
        assertThrows(UserNotFoundException.class, () ->
                itemRequestController.getById(2L, itemRequest.getId()));
    }

    @Test
    void getByIdWithNotRequestTest() {
        UserDto user = userController.add(userDto);
        assertThrows(ItemRequestNotFoundException.class, () ->
                itemRequestController.getById(user.getId(), 2L));
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.add(userDto);
        itemRequestController.add(user.getId(), itemRequestDto);
        assertEquals(1, itemRequestController.getAllByRequestor(user.getId()).size());
    }

    @Test
    void getAllByUserWithWrongUserTest() {
        assertThrows(UserNotFoundException.class, () -> itemRequestController.getAllByRequestor(1L));
    }

    @Test
    void getAll() {
        UserDto user = userController.add(userDto);
        itemRequestController.add(user.getId(), itemRequestDto);
        assertEquals(0, itemRequestController.getAll(0, 10, user.getId()).size());

        UserDto user2 = new UserDto();
        user2.setEmail("email@ya.ru");
        user2.setName("name2");
        UserDto createUser2 = userController.add(user2);

        assertEquals(1, itemRequestController.getAll(0, 10, createUser2.getId()).size());
    }

    @Test
    void getAllByWrongUser() {
        assertThrows(UserNotFoundException.class, () -> itemRequestController.getAll(0, 10, 1L));
    }

    @Test
    void getAllWithWrongFrom() {
        assertThrows(ItemRequestNotValidException.class, () ->
                itemRequestController.getAll(-1, 10, 1L));
    }
}
