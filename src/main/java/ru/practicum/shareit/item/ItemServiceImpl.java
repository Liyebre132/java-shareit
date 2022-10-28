package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User user = userRepository.getById(userId);
        Item item = itemRepository.save(ItemMapper.toItem(itemDto, user));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long id, ItemDto itemDto) {
        User user = userRepository.getById(userId);
        ItemDto currentItem = getById(id);
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            currentItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            currentItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            currentItem.setAvailable(itemDto.getAvailable());
        }
        Item item = itemRepository.update(userId, id, ItemMapper.toItem(currentItem, user));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getById(long id) {
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

    @Override
    public void delete(long userId, long id) {
        itemRepository.delete(userId, id);
    }

    @Override
    public List<ItemDto> getAllByUser(long userId) {
        return ItemMapper.mapToItemDto(itemRepository.getAllByUser(userId));
    }

    @Override
    public List<ItemDto> search(String text) {
        return ItemMapper.mapToItemDto(itemRepository.search(text));
    }

    @Override
    public List<ItemDto> getAll() {
        return ItemMapper.mapToItemDto(itemRepository.getAll());
    }
}
