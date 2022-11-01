package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    Item update(long userId, long id, Item item);

    Item getById(long id);

    void delete(long userId, long id);

    List<Item> getAllByUser(long userId);

    List<Item> search(String text);

    List<Item> getAll();
}
