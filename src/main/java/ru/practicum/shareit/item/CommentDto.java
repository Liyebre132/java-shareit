package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank
    private String text;

    private Long itemId;

    private String authorName;

    private LocalDateTime created;

}