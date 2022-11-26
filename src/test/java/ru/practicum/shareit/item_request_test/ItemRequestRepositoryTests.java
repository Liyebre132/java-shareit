package ru.practicum.shareit.item_request_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestRepositoryTests {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByRequestorIdTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("e@mail.ru");
        User createdUser = userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest(1L, "desc", createdUser, LocalDateTime.now());

        itemRequestRepository.save(itemRequest);
        List<ItemRequest> items = itemRequestRepository.findByRequestor_Id(user.getId());
        assertThat(items.size(), equalTo(1));
        assertThat(items.get(0).getRequestor().getId(), equalTo(1L));
    }

    @Test
    void findAllByIdIsNotTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("e@mail.ru");
        User createdUser = userRepository.save(user);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(createdUser);
        itemRequest.setDescription("desc");
        itemRequest.setCreated(LocalDateTime.now());

        assertThat(itemRequestRepository.findAllByIdIsNot(createdUser.getId(), Pageable.ofSize(10))
                .stream().count(), equalTo(0L));

        User user2 = new User();
        user2.setName("name2");
        user2.setEmail("e2@mail.ru");
        User createdUser2 = userRepository.save(user2);

        assertThat(itemRequestRepository.findAllByIdIsNot(createdUser2.getId(), Pageable.ofSize(10))
                .stream().count(), equalTo(0L));
    }
}
