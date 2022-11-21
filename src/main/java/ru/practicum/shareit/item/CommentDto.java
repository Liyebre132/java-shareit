package ru.practicum.shareit.item;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank
    private String text;

    private Long itemId;

    private String authorName;

    private LocalDateTime created;

}
