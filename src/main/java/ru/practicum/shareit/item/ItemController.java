package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResult;
import ru.practicum.shareit.item.exception.ItemNotValidException;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResult add(@RequestHeader("X-Sharer-User-Id") long userId, @Valid
                       @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("{id}")
    public ItemResult update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long id,
                          @RequestBody ItemDto item) {
        return itemService.update(userId, id, item);
    }

    @GetMapping("{id}")
    public ItemResult getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemService.getById(userId, id);
    }

    @GetMapping
    public List<ItemResult> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size < 1) {
            throw new ItemNotValidException("Некорректно переданы данные для поиска");
        }
        return itemService.getAllByUser(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemResult> search(@RequestParam String text,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        if (from < 0 || size < 1) {
            throw new ItemNotValidException("Некорректно переданы данные для поиска");
        }
        return itemService.search(text, from, size);
    }

    @DeleteMapping("{id}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        itemService.delete(userId, id);
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long id,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, id, commentDto);
    }
}
