package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User save(User user);

    User update(long id, User user);

    User getById(long id);

    void delete(long id);

    List<User> getAll();
}
