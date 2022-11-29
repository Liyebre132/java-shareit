package ru.practicum.shareit.item_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void searchTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("e@mail.ru");
        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setName("test");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(createdUser);
        itemRepository.save(item);

        Page<Item> items = itemRepository.search("desc", Pageable.ofSize(10));
        assertThat(items.stream().count(), equalTo(1L));
    }

    @Test
    void findAllByOwnerIdTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("e@mail.ru");
        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setName("test");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(createdUser);
        itemRepository.save(item);

        Page<Item> items = itemRepository.findAllByOwner_Id(user.getId(), Pageable.ofSize(10));
        assertThat(items.stream().count(), equalTo(1L));
    }

    @Test
    void findByRequestTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("e@mail.ru");
        User createdUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("name2");
        user2.setEmail("e2@mail.ru");
        User createdUser2 = userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("desc");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(createdUser2);
        ItemRequest itemRequestCreated = itemRequestRepository.save(itemRequest);

        Item item = new Item();
        item.setName("test");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(createdUser);
        item.setRequest(itemRequest.getId());
        itemRepository.save(item);

        assertThat(itemRepository.findByRequest(itemRequestCreated.getId()).size(), equalTo(1));
    }

    @Test
    void findAllCommentByItemIdTest() {
        User user = new User();
        user.setName("name");
        user.setEmail("e@mail.ru");
        User createdUser = userRepository.save(user);

        User user2 = new User();
        user2.setName("name2");
        user2.setEmail("e2@mail.ru");
        User createdUser2 = userRepository.save(user2);

        Item item = new Item();
        item.setName("test");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(createdUser);
        Item createdItem = itemRepository.save(item);

        Comment comment = new Comment();
        comment.setText("comment");
        comment.setItem(createdItem);
        comment.setAuthor(createdUser2);
        commentRepository.save(comment);

        assertThat(commentRepository.findByItem_Id(item.getId()).size(), equalTo(1));
    }
}
