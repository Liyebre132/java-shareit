package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemResult toItemResult(Item item) {
        ItemResult result = new ItemResult();
        result.setId(item.getId());
        result.setName(item.getName());
        result.setDescription(item.getDescription());
        result.setAvailable(item.isAvailable());
        result.setRequest(item.getRequest());
        return result;
    }

    public static Item toItem(ItemResult itemResult, User user) {
        return new Item(
                0L, // так же захардкодил как и с бронированием
                itemResult.getName(),
                itemResult.getDescription(),
                itemResult.getAvailable(),
                user,
                itemResult.getRequest()
        );
    }

    public static List<ItemResult> mapToItemDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemResult)
                .collect(Collectors.toList());
    }

    public static Comment toComment(User user, Item item, CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                item,
                user,
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentDto> mapToCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
