package ru.practicum.shareit.item_request_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResult;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestMapperTest {

    private final ItemRequest itemRequest = new ItemRequest(
            1L,
            "desc",
            new User(),
            LocalDateTime.now()
    );

    private final ItemRequestResult itemRequestResult = new ItemRequestResult(
            1L,
            "desc",
            LocalDateTime.now(),
            new ArrayList<>()
    );

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "desc",
            LocalDateTime.now()
    );

    @Test
    void toItemRequestDtoTest() {
        ItemRequestDto res = ItemRequestMapper.toItemRequestDto(itemRequest);
        assertEquals(res.getId(), itemRequest.getId());
    }

    @Test
    void toItemRequestResultTest() {
        ItemRequestResult res = ItemRequestMapper.toItemRequestResult(itemRequest);
        assertEquals(res.getId(), itemRequest.getId());
    }

    @Test
    void mapToItemRequestResultTest() {
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        List<ItemRequestResult> itemRequestResultList = ItemRequestMapper.mapToItemRequestResult(itemRequestList);
        assertEquals(itemRequestList.size(), itemRequestResultList.size());
    }

}
