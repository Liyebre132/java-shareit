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

    UserDto user;

    @BeforeEach
    void init() {
        user = new UserDto();
        user.setName("testUser");
        user.setEmail("e@mail.ru");
    }

    @Test
    void addTest() {
        UserDto userDto = userController.add(user);
        assertEquals(userDto.getId(), userController.getById(userDto.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.add(user);
        UserDto userDto = new UserDto(1L, "updateName", "update@mail.ru");
        userController.update(1L, userDto);
        assertEquals(userDto.getEmail(), userController.getById(1L).getEmail());
    }

    @Test
    void updateWrongUserTest() {
        assertThrows(EntityNotFoundException.class, () -> userController.update(100L, user));
    }

    @Test
    void getByIdTest() {
        userController.add(user);
        assertEquals(user.getEmail(), userController.getById(1L).getEmail());
    }

    @Test
    void getByWrongIdTest() {
        assertThrows(EntityNotFoundException.class, () -> userController.getById(100L));
    }

    @Test
    void getAllTest() {
        assertEquals(0, userController.getAll().size());
        userController.add(user);
        assertEquals(1, userController.getAll().size());
    }

    @Test
    void deleteTest() {
        userController.add(user);
        assertEquals(1, userController.getAll().size());
        userController.delete(1L);
        assertEquals(0, userController.getAll().size());
    }
}
