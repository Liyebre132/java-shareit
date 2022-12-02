package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResult;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(long userId, ItemRequestDto itemRequestDto);

    ItemRequestResult getById(long userId, long requestId);

    List<ItemRequestResult> getAllbyRequestor(long userId);

    List<ItemRequestResult> getAll(int from, int size, Long userId);
}
