package ru.practicum.shareit.item_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResult;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemMapperTest {

    private final Item item = new Item(
            1L,
            "name",
            "desc",
            true,
            new User(),
            1L
    );

    private final User user = new User(1L, "name", "e@mail.ru");

    private final ItemResult itemResult = new ItemResult(
            1L,
            "name",
            "desc",
            true,
            null,
            null,
            new ArrayList<>(),
            null
    );

    private final Comment comment = new Comment(1L, "text", item, user, LocalDateTime.now());

    private final CommentDto commentDto = new CommentDto(1L,
            "text", item.getId(), item.getName(), LocalDateTime.now());

    @Test
    void toItemResultTest() {
        ItemResult res = ItemMapper.toItemResult(item);
        assertEquals(res.getId(), item.getId());
    }

    @Test
    void toItemTest() {
        Item res = ItemMapper.toItem(itemResult, user);
        assertEquals(res.getOwner().getId(), user.getId());
    }

    @Test
    void mapToItemResultTest() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        List<ItemResult> itemResultList = ItemMapper.mapToItemResult(itemList);
        assertEquals(itemResultList.size(), itemList.size());
    }

    @Test
    void toCommentTest() {
        Comment res = ItemMapper.toComment(user, item, commentDto);
        assertEquals(res.getAuthor().getName(), user.getName());
    }

    @Test
    void toCommentDtoTest() {
        CommentDto res = ItemMapper.toCommentDto(comment);
        assertEquals(res.getId(), comment.getId());
    }

    @Test
    void mapToCommentDto() {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        List<CommentDto> commentDtoList = ItemMapper.mapToCommentDto(commentList);
        assertEquals(commentDtoList.size(), commentList.size());
    }

}
