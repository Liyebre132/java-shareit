package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public List<ItemResult> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        if (userId != null) {
            return itemService.getAllByUser(userId);
        } else {
            return itemService.getAll();
        }
    }

    @GetMapping("/search")
    public List<ItemResult> search(@RequestParam String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.search(text);
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
