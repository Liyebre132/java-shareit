package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

    public static List<ItemRequestDto> mapToItemRequestDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    public static List<ItemRequestResult> mapToItemRequestResult(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestResult)
                .collect(Collectors.toList());
    }
}
