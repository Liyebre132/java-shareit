package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResult;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequestResult toItemRequestResult(ItemRequest itemRequest) {
        ItemRequestResult result = new ItemRequestResult();
        result.setId(itemRequest.getId());
        result.setDescription(itemRequest.getDescription());
        result.setCreated(itemRequest.getCreated());
        return result;
    }

    public static List<ItemRequestResult> mapToItemRequestResult(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestResult)
                .collect(Collectors.toList());
    }
}
