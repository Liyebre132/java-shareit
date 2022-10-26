package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        userRepository.getById(userId);
        Item item = itemRepository.save(ItemMapper.toItem(itemDto, userId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long id, ItemDto itemDto) {
        Item item = itemRepository.update(userId, id, ItemMapper.toItem(itemDto, userId));
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
