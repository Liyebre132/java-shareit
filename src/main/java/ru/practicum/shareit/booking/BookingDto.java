package ru.practicum.shareit.booking;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @Future
    @NotNull
    private LocalDateTime end;

    private Long itemId;
}
