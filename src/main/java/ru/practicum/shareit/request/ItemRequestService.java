package ru.practicum.shareit.request;

import java.util.List;

interface ItemRequestService {
    ItemRequestDto addNewItemRequest(long userId, ItemRequestDto itemRequestDto);

    ItemRequestResult getById(long userId, long requestId);

    List<ItemRequestResult> getAllbyRequestor(long userId);

    List<ItemRequestResult> getAll(int from, int size, Long userId);
}
