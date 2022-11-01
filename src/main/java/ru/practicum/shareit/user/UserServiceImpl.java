package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserDto addNewUser(UserDto userDto) {
        User user = repository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        User user = repository.update(id, UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getById(long id) {
        return UserMapper.toUserDto(repository.getById(id));
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public List<UserDto> getAll() {
        return UserMapper.mapToUserDto(repository.getAll());
    }
}
