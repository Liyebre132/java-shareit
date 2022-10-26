package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.exception.ItemNotFoundException;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private long id;

    @Override
    public Item save(Item item) {
        items.compute(item.getOwner(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            item.setId(getId());
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(long userId, long id, Item item) {
        List<Item> userItems = getAllByUser(userId);
        Optional<Item> currentItem = userItems.stream()
                .filter(i -> id == i.getId())
                .findAny();
        if (currentItem.isPresent()) {
            int index = userItems.indexOf(currentItem.get());
            if (item.getName() != null) {
                currentItem.get().setName(item.getName());
            }
            if (item.getDescription() != null) {
                currentItem.get().setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                currentItem.get().setAvailable(item.getAvailable());
            }
            items.get(userId).set(index, currentItem.get());
            return currentItem.get();
        } else {
            throw new ItemNotFoundException("Вещь с таким ID не найдена");
        }
    }

    @Override
    public Item getById(long id) {
        List<Item> itemList = getAll();

        Optional<Item> item = itemList.stream()
                .filter(i -> id == i.getId())
                .findAny();

        if (item.isEmpty()) {
            throw new ItemNotFoundException("Вещь с таким ID не найдена");
        } else {
            return item.get();
        }
    }

    @Override
    public void delete(long userId, long id) {
        items.remove(id);
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
        List<Item> itemList = new ArrayList<>();

        for (List<Item> i : items.values()) {
            if (!i.isEmpty()) {
                itemList.addAll(i);
            }
        }
        return itemList;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> allItems = getAll();
        List<Item> result = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : allItems) {
                boolean isItem = (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()));
                if (item.getAvailable() && isItem) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    private long getId() {
        id++;
        return id;
    }
}
