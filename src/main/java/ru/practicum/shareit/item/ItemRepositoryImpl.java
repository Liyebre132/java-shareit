package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private final Map<Long, Item> storage = new LinkedHashMap<>();
    private long id;

    @Override
    public Item save(Item item) {
        items.compute(item.getOwner().getId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            item.setId(getId());
            userItems.add(item);
            return userItems;
        });
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(long userId, long id, Item item) {
        Item currentItem = getById(id);
        if (Objects.equals(currentItem.getOwner().getId(), item.getOwner().getId())) {
            storage.put(id, item);
            int index = items.get(userId).indexOf(currentItem);
            items.get(userId).set(index, item);
            return item;
        } else {
            throw new ItemNotFoundException("Вещь с таким ID не найдена");
        }
    }

    @Override
    public Item getById(long id) {
        Item item = storage.get(id);
        if (item != null) {
            return item;
        } else {
            throw new ItemNotFoundException("Вещь с таким ID не найдена");
        }
    }

    @Override
    public void delete(long userId, long id) {
        storage.remove(id);
        items.get(userId).remove(getById(id));
    }

    @Override
    public List<Item> getAllByUser(long userId) {
        if (items.get(userId) != null) {
            return items.get(userId);
        } else {
            throw new ItemNotFoundException("Вещей у пользователя с этим ID не найдено");
        }
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Item> search(String text) {
        List<Item> result = new ArrayList<>();
        if (!text.isBlank()) {
            result = getAll().stream()
                    .filter(i -> isRightItem(i, text))
                    .collect(Collectors.toList());
        }
        return result;
    }

    private boolean isRightItem(Item item, String text) {
        return (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.isAvailable();
    }

    private long getId() {
        id++;
        return id;
    }
}
