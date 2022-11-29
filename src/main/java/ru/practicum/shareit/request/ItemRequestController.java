package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.exception.ItemRequestNotValidException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResult;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addNewItemRequest(userId, itemRequestDto);
    }

    @GetMapping("{id}")
    public ItemRequestResult getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return itemRequestService.getById(userId, id);
    }

    @GetMapping
    public List<ItemRequestResult> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getAllbyRequestor(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResult> getAll(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (from < 0 || size < 1) {
            throw new ItemRequestNotValidException("Некорректно переданы данные для поиска");
        }
        return itemRequestService.getAll(from, size, userId);
    }
}
