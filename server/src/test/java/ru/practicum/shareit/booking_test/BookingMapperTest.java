package ru.practicum.shareit.booking_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResult;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingMapperTest {

    private final Item item = new Item(
            1L,
            "name",
            "desc",
            true,
            new User(),
            1L
    );

    private final User user = new User(1L, "test", "test@mail.ru");

    private final Booking booking = new Booking(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            item,
            user,
            BookingStatus.WAITING
    );

    private final BookingDto bookingDto = new BookingDto(
            LocalDateTime.now(),
            LocalDateTime.now(),
            1L
    );

    @Test
    void toBookingResultTest() {
        BookingResult res = BookingMapper.toBookingResult(booking);
        assertEquals(res.getId(), booking.getId());
    }

    @Test
    void toBookingTest() {
        Booking res = BookingMapper.toBooking(bookingDto, item, user, BookingStatus.WAITING);
        assertEquals(res.getItem().getId(), booking.getItem().getId());
    }

    @Test
    void mapToBookingResultTest() {
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        List<BookingResult> bookingResultList = BookingMapper.mapToBookingResult(bookingList);
        assertEquals(bookingList.size(), bookingResultList.size());
    }
}
