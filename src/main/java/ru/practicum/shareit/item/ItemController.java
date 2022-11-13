package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId, @Valid
                       @RequestBody ItemDto item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long id,
                          @RequestBody ItemDto item) {
        return itemService.update(userId, id, item);
    }

    @GetMapping("{id}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemService.getById(userId, id);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        if (userId != null) {
            return itemService.getAllByUser(userId);
        } else {
            return itemService.getAll();
        }
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
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
