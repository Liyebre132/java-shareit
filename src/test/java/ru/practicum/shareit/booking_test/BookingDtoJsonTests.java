package ru.practicum.shareit.booking_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingResult;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoJsonTests {
    @Autowired
    JacksonTester<BookingResult> json;

    @Test
    void testBookingDto() throws Exception {
        BookingResult bookingDto = new BookingResult();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.of(2022, 12, 12, 10, 10, 1));
        bookingDto.setEnd(LocalDateTime.of(2022, 12, 20, 10, 10, 1));

        JsonContent<BookingResult> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2022, 12, 12, 10, 10, 1)
                        .toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2022, 12, 20, 10, 10, 1)
                        .toString());
    }
}
