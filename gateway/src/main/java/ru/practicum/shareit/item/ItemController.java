package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") long userId, @Valid
                       @RequestBody ItemDto item) {
        return itemClient.addNewItem(userId, item);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long id,
                          @RequestBody ItemDto item) {
        return itemClient.update(userId, id, item);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long id) {
        return itemClient.getById(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                   @Positive @RequestParam(defaultValue = "10") int size) {
        return itemClient.search(text, from, size);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemClient.delete(userId, id);
    }

    @PostMapping("{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long id,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, id, commentDto);
    }
}
