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
        UserDto user = getById(id);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }
        repository.save(UserMapper.toUser(user));
        return user;
    }

    @Override
    public UserDto getById(long id) {
        return UserMapper.toUserDto(repository.getById(id));
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return UserMapper.mapToUserDto(repository.findAll());
    }
}
