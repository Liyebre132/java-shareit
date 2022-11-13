package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResult addNewBooking(long userId, BookingDto bookingDto) {
        Item item = itemRepository.getById(bookingDto.getItemId());
        User user = userRepository.getById(userId);
        bookingDto.setStatus(BookingStatus.WAITING);
        if (user.getName().isBlank()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        if (item.getOwner() == userId) {
            throw new BookingNotFoundException("Забронировать свою же вещь нельзя");
        }
        if (!item.isAvailable()) {
            throw new BookingNotAvailableException("Вещь не доступна для аренды");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingDateException("Время начала бронирования раньше времени его завершения");
        }
        Booking booking = repository.save(BookingMapper.toBooking(bookingDto, item, user));
        return BookingMapper.toBookingResult(booking);
    }

    @Override
    public BookingResult approved(long userId, long bookingId, boolean approved) {
        Booking booking = repository.getById(bookingId);
        if (userId != booking.getItem().getOwner()) {
            throw new BookingNotFoundException("Бронирование не найдено");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingIncorrectApprovedException("Текущее бронирование уже утверждено");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingResult(repository.save(booking));
    }

    @Override
    public BookingResult getById(long userId, long id) {
        Booking booking = repository.getById(id);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner() != userId) {
            throw new BookingNotFoundException("Бронирование не найдено");
        }
        return BookingMapper.toBookingResult(booking);
    }

    @Override
    public List<BookingResult> getAllByBooker(long userId, String state) {
        User user = userRepository.getById(userId);
        if (user.getName().isBlank()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        if (state.equalsIgnoreCase("ALL")) {
            return BookingMapper.mapToBookingResult(repository.findAllByBookerId(userId));
        } else if (state.equalsIgnoreCase("CURRENT")) {
            return BookingMapper.mapToBookingResult(repository.findAllByBookerIdCurrent(userId));
        } else if (state.equalsIgnoreCase("PAST")) {
            List<BookingResult> list = BookingMapper.mapToBookingResult(repository.findAllByBookerIdPast(userId));
            List<BookingResult> results = new ArrayList<>();
            results.add(list.get(0));
            return results;
        } else if (state.equalsIgnoreCase("FUTURE")) {
            return BookingMapper.mapToBookingResult(repository.findAllByBookerIdFuture(userId));
        } else if (state.equalsIgnoreCase("WAITING")) {
            return BookingMapper.mapToBookingResult(repository.findAllByBookerIdWaiting(userId));
        } else if (state.equalsIgnoreCase("REJECTED")) {
            return BookingMapper.mapToBookingResult(repository.findAllByBookerIdRejected(userId));
        } else {
            throw new BookingIncorrectStateException("UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingResult> getAllByOwner(long userId, String state) {
        User user = userRepository.getById(userId);
        if (user.getName().isBlank()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        if (state.equalsIgnoreCase("ALL")) {
            return BookingMapper.mapToBookingResult(repository.findAllByOwnerId(userId));
        } else if (state.equalsIgnoreCase("CURRENT")) {
            return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdCurrent(userId));
        } else if (state.equalsIgnoreCase("PAST")) {
            List<BookingResult> list = BookingMapper.mapToBookingResult(repository.findAllByOwnerIdPast(userId));
            List<BookingResult> results = new ArrayList<>();
            results.add(list.get(0));
            return results;
        } else if (state.equalsIgnoreCase("FUTURE")) {
            return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdFuture(userId));
        } else if (state.equalsIgnoreCase("WAITING")) {
            return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdWaiting(userId));
        } else if (state.equalsIgnoreCase("REJECTED")) {
            return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdRejected(userId));
        } else {
            throw new BookingIncorrectStateException("UNSUPPORTED_STATUS");
        }
    }
}
