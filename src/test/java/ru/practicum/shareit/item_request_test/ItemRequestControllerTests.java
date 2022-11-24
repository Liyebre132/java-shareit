package ru.practicum.shareit.item_request_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestDto;
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
    void getAllByRequestorTest() {
        UserDto user = userController.add(userDto);
        ItemRequestDto itemRequest = itemRequestController.add(user.getId(), itemRequestDto);
        assertEquals(1, itemRequestController.getAllByRequestor(user.getId()).size());
    }

    @Test
    void getAllByRequestorWrongUserTest() {
        assertThrows(UserNotFoundException.class, () -> itemRequestController.getAllByRequestor(1L));
    }

    @Test
    void getAllByWrongUser() {
        assertThrows(UserNotFoundException.class, () -> itemRequestController.getAll(0, 10, 1L));
    }

    @Test
    void getAllWithWrongFrom() {
        assertThrows(ItemRequestNotValidException.class, () -> itemRequestController.getAll(-1, 10, 1L));
    }
}