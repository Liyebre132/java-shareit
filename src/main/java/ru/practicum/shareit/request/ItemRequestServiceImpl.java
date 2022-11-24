package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Такой пользователь не найден");
        }
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
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Такой пользователь не найден");
        }

        List<ItemRequestResult> itemRequests = ItemRequestMapper
                .mapToItemRequestResult(itemRequestRepository.findByRequestor_Id(userId));

        itemRequests.forEach(this::setItems);

        return itemRequests;
    }

    @Override
    public List<ItemRequestResult> getAll(int from, int size, Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("Такой пользователь не найден");
        }

        List<ItemRequestResult> results = itemRequestRepository.findAllByIdIsNot(userId, PageRequest.of(from / size, size))
                .stream()
                .map(ItemRequestMapper::toItemRequestResult)
                .collect(Collectors.toList());

        results.forEach(this::setItems);

        return results;
    }

    private void setItems(ItemRequestResult itemRequest) {
        itemRequest.setItems(itemRepository.findByRequest(itemRequest.getId()).stream()
                .map(ItemMapper::toItemResult)
                .collect(Collectors.toList()));
    }
}
