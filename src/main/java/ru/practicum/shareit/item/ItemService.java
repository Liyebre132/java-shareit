package ru.practicum.shareit.item;

import java.util.List;

interface ItemService {
    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto update(long userId, long id, ItemDto item);

    ItemDto getById(long id);

    void delete(long userId, long id);

    List<ItemDto> getAllByUser(long userId);

    List<ItemDto> search(String text);

    List<ItemDto> getAll();
}
