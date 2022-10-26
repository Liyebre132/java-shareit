package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.exception.UserExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements  UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public User save(User user) {
        checkEmail(user.getEmail());
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(long id, User user) {
        User currentUser = getById(id);

        if (user.getName() != null) {
            currentUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            currentUser.setEmail(user.getEmail());
        }

        users.put(id, currentUser);
        return currentUser;
    }

    @Override
    public User getById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        } else {
            return user;
        }
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private long getId() {
        id++;
        return id;
    }

    private void checkEmail(String email) {
        for (User u : users.values()) {
            if (u.getEmail().equals(email)) {
                throw new UserExistsException("Пользователь с таким email уже существует");
            }
        }
    }
}