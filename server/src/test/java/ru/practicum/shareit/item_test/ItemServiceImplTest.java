package ru.practicum.shareit.item_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {

    private ItemServiceImpl itemService;

    private ItemRepository itemRepository;

    private UserRepository userRepository;

    private BookingRepository bookingRepository;

    private CommentRepository commentRepository;

    private ItemRequestRepository itemRequestRepository;


    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository
        );
        when(itemRepository.save(any())).then(invocation -> invocation.getArgument(0));
    }

    @Test
    void getAllTest() {
        var item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        var result = itemRepository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.get(0).getId());
    }

    @Test
    void getByIdTest() {
        var item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);
        when(itemRepository.getById(1L)).thenReturn(item);
        var result = itemRepository.getById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(item.getId(), result.getId());
    }
}
