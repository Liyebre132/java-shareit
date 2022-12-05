package ru.practicum.shareit.booking_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingResult;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoJsonTests {
    @Autowired
    JacksonTester<BookingResult> json;

    private final LocalDateTime startDateTime =
            LocalDateTime.of(2022, 12, 12, 10, 10, 1);

    private final LocalDateTime endDateTime =
            LocalDateTime.of(2022, 12, 20, 10, 10, 1);

    @Test
    void testBookingDto() throws Exception {
        BookingResult bookingDto = new BookingResult();
        bookingDto.setId(1L);
        bookingDto.setStart(startDateTime);
        bookingDto.setEnd(endDateTime);

        JsonContent<BookingResult> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(startDateTime.toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(endDateTime.toString());
    }
}
