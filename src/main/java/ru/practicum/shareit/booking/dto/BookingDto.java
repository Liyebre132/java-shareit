package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    @NotNull(groups = {Marker.OnCreate.class})
    @FutureOrPresent(groups = {Marker.OnCreate.class})
    private LocalDateTime start;

    @Future(groups = {Marker.OnCreate.class})
    @NotNull(groups = {Marker.OnCreate.class})
    private LocalDateTime end;

    private Long itemId;
}
