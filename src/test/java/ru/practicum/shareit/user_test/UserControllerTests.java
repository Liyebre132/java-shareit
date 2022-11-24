package ru.practicum.shareit.user_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTests {
    @Autowired
    private UserController userController;

    private UserDto user;

    @BeforeEach
    void init() {
        user = new UserDto();
        user.setName("name");
        user.setEmail("user@ya.ru");
    }

    @Test
    void createTest() {
        UserDto userDto = userController.add(user);
        assertEquals(userDto.getId(), userController.getById(userDto.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.add(user);
        UserDto userDto = user;
        userDto.setName("update name");
        userDto.setEmail("updateEmail@ya.ru");
        userController.update(1L, userDto);
        assertEquals(userDto.getEmail(), userController.getById(1L).getEmail());
    }

    @Test
    void updateByWrongUserTest() {
        assertThrows(EntityNotFoundException.class, () -> userController.update(99L, user));
    }

    @Test
    void deleteTest() {
        UserDto userDto = userController.add(user);
        assertEquals(1, userController.getAll().size());
        userController.delete(userDto.getId());
        assertEquals(0, userController.getAll().size());
    }

    @Test
    void getByWrongIdTest() {
        assertThrows(EntityNotFoundException.class, () -> userController.getById(99L));
    }
}