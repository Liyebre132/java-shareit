package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResult;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto addNewItemRequest(long userId, ItemRequestDto itemRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Такой пользователь не найден");
        }
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(user.get());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestResult getById(long userId, long requestId) {
        checkUser(userId);
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest.isEmpty()) {
            throw new ItemRequestNotFoundException("Такой запрос не найден");
        }
        ItemRequestResult result = ItemRequestMapper.toItemRequestResult(itemRequest.get());
        setItems(result);
        return result;
    }

    @Override
    public List<ItemRequestResult> getAllbyRequestor(long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestor_Id(userId);
        return setItems(itemRequests);
    }

    @Override
    public List<ItemRequestResult> getAll(int from, int size, Long userId) {
        checkUser(userId);
        List<ItemRequest> results = itemRequestRepository.findAllByIdIsNot(userId,
                        PageRequest.of(from / size, size))
                .stream()
                .collect(Collectors.toList());

        return setItems(results);
    }

    private void setItems(ItemRequestResult itemRequest) {
        itemRequest.setItems(itemRepository.findByRequest(itemRequest.getId()).stream()
                .map(ItemMapper::toItemResult)
                .collect(Collectors.toList()));
    }

    private List<ItemRequestResult> setItems(List<ItemRequest> itemRequests) {
        List<Long> itemsIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        Map<Long, List<Item>> itemRequestMap = itemRepository.findByRequestIn(itemsIds).stream()
                .collect(groupingBy(Item::getRequest, toList()));
        List<ItemRequestResult> results = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            ItemRequestResult itemRequestResult = ItemRequestMapper.toItemRequestResult(itemRequest);
            if (itemRequestMap.get(itemRequest.getId()) != null) {
                itemRequestResult.setItems(ItemMapper.mapToItemResult(itemRequestMap.get(itemRequestResult.getId())));
            } else {
                itemRequestResult.setItems(Collections.emptyList());
            }
            results.add(itemRequestResult);
        }
        return results;
    }

    private void checkUser(long userId) {
        userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("Такой пользователь не найден"));
    }
}
