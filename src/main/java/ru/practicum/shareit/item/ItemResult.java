package ru.practicum.shareit.item;

import lombok.*;

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

    private Long request;

    @Data
    @AllArgsConstructor
    public static class NextBooking {
        private Long id;
        private Long bookerId;
    }

    @Data
    @AllArgsConstructor
    public static class LastBooking {
        private Long id;
        private Long bookerId;
    }


}
