package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResult {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private NextBooking nextBooking;

    private LastBooking lastBooking;

    private List<CommentDto> comments;

    private Long requestId;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class NextBooking {
        private Long id;
        private Long bookerId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LastBooking {
        private Long id;
        private Long bookerId;
    }

}
