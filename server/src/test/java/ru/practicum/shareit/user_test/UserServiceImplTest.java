package ru.practicum.shareit.user_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    private UserServiceImpl userService;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        when(userRepository.save(any())).then(invocation -> invocation.getArgument(0));
    }

    @Test
    void getAllTest() {
        var user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("mail@email.com");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        var result = userService.getAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.get(0).getId());
    }

    @Test
    void getByIdTest() {
        var user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("mail@email.com");
        when(userRepository.getById(1L)).thenReturn(user);
        var result = userService.getById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getId(), result.getId());
    }
}
