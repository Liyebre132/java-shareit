package ru.practicum.shareit.booking_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingResult;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemResult;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerMockMvcTests {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private UserDto userDto;

    private ItemResult itemResult;

    private BookingDto bookingDto;

    private BookingResult bookingResult;

    @BeforeEach
    void init() {
        userDto = new UserDto(1L, "name", "user@ya.ru");
        itemResult = new ItemResult(
                1L,
                "name",
                "desc",
                true,
                null,
                null,
                new ArrayList<>(),
                null);
        bookingDto = new BookingDto(
                LocalDateTime.of(2022, 12, 12, 10, 0),
                LocalDateTime.of(2022, 12, 20, 10, 0),
                itemResult.getId());
        bookingResult = new BookingResult(
                1L,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                new BookingResult.Item(itemResult.getId(), itemResult.getName()),
                new BookingResult.Booker(userDto.getId(), userDto.getName()),
                WAITING
        );
    }

    @Test
    void createTest() throws Exception {
        when(bookingService.addNewBooking(anyLong(), any()))
                .thenReturn(bookingResult);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingResult)));
    }

    @Test
    void approveTest() throws Exception {
        bookingResult.setStatus(APPROVED);
        when(bookingService.approved(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResult);
        mvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingResult)));
    }

    @Test
    void getAllByOwnerTest() throws Exception {
        when(bookingService.getAllByOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResult));
        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingResult))));
    }

    @Test
    void getAllByUserTest() throws Exception {
        when(bookingService.getAllByBooker(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResult));
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingResult))));
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(bookingResult);
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingResult)));
    }
}
