package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.addNewItemRequest(userId, itemRequestDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemRequestClient.getById(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getAllbyRequestor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                          @Positive @RequestParam(defaultValue = "10") int size,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getAll(from, size, userId);
    }
}
