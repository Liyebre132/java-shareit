package ru.practicum.shareit.user_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserMapperTest {

    private final UserDto userDto = new UserDto(1L, "test", "test@mail.ru");

    private final User user = new User(1L, "test", "test@mail.ru");

    @Test
    void toUserDtoTest() {
        UserDto res = UserMapper.toUserDto(user);
        assertEquals(res.getId(), user.getId());
    }

    @Test
    void toUserTest() {
        User res = UserMapper.toUser(userDto);
        assertEquals(res.getId(), userDto.getId());
    }

    @Test
    void mapToUserDtoTest() {
        List<User> userList = new ArrayList<>();
        List<UserDto> userDtoList = new ArrayList<>();
        userList.add(user);
        userDtoList.add(userDto);
        assertEquals(userList.get(0).getId(), userDtoList.get(0).getId());
    }

}
