package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.*;
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