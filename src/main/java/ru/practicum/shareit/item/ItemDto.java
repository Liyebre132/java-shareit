package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.LastBookingDto;
import ru.practicum.shareit.booking.NextBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
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
