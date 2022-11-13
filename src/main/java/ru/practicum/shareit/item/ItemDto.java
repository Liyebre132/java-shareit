package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.booking.LastBookingDto;
import ru.practicum.shareit.booking.NextBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private NextBookingDto nextBooking;

    private LastBookingDto lastBooking;

    private List<CommentDto> comments;

    private Long request;

}
