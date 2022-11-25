package ru.practicum.shareit.booking_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTests {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;

    private Item item;

    private User user2;

    private Booking booking;

    @BeforeEach
    void init() {
        user = new User();
        user.setName("name");
        user.setEmail("email@email.com");

        item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        user2 = new User();
        user2.setName("name2");
        user2.setEmail("email2@email.com");

        booking = new Booking();
        booking.setStart(LocalDateTime.of(2023, 1, 10, 10, 30));
        booking.setEnd(LocalDateTime.of(2023, 2, 10, 10, 30));
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(WAITING);
    }

    @Test
    void findAllByItemIdTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findByItem_Id(item.getId()).size(), equalTo(1));
    }

    @Test
    void findAllByBookerIdTest() {
        userRepository.save(user);
        itemRepository.save(item);
        userRepository.save(user2);
        bookingRepository.save(booking);
        assertThat((long) bookingRepository.findAllByBookerId(
                user2.getId(),
                Pageable.ofSize(10)).size(),
                equalTo(1L)
        );
    }
}
