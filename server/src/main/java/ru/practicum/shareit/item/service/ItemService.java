package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResult;

import java.util.List;

public interface ItemService {
    ItemResult addNewItem(long userId, ItemDto itemDto);

    ItemResult update(long userId, long id, ItemDto item);

    ItemResult getById(long userId, long id);

    void delete(long userId, long id);

    List<ItemResult> getAllByUser(long userId, int from, int size);

    List<ItemResult> search(String text, int from, int size);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
