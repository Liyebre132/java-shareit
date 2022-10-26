package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequest {
    Long id;
    String description;
    Long requestor;
    LocalDateTime created;
}
