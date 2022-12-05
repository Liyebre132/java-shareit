package ru.practicum.shareit.item_request_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceImplTest {

    private ItemRequestServiceImpl itemRequestService;

    private ItemRequestRepository itemRequestRepository;

    private ItemRepository itemRepository;

    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(
                itemRequestRepository,
                itemRepository,
                userRepository
        );
        when(itemRequestRepository.save(any())).then(invocation -> invocation.getArgument(0));
    }

    @Test
    void getAllTest() {
        var itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("desc");
        when(itemRequestRepository.findAll()).thenReturn(Collections.singletonList(itemRequest));
        var result = itemRequestRepository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemRequest.getId(), result.get(0).getId());
    }

    @Test
    void getByIdTest() {
        var itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("desc");
        when(itemRequestRepository.getById(1L)).thenReturn(itemRequest);
        var result = itemRequestRepository.getById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(itemRequest.getId(), result.getId());
    }
}
