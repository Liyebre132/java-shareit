package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    UserDto addNewUser(UserDto userDto);

    UserDto update(long id, UserDto userDto);

    UserDto getById(long id);

    void delete(long id);

    List<UserDto> getAll();
}
