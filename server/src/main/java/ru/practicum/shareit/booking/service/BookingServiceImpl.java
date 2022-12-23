package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResult;
import ru.practicum.shareit.booking.enums.BookingStates;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.exception.BookingIncorrectApprovedException;
import ru.practicum.shareit.booking.exception.BookingIncorrectStateException;
import ru.practicum.shareit.booking.exception.BookingNotAvailableException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResult addNewBooking(long userId, BookingDto bookingDto) {
        Item item = itemRepository.getById(bookingDto.getItemId());
        User user = userRepository.getById(userId);
        if (item.getOwner().getId() == userId) {
            throw new BookingNotFoundException("Забронировать свою же вещь нельзя");
        }
        if (!item.isAvailable()) {
            throw new BookingNotAvailableException("Вещь не доступна для аренды");
        }
        Booking booking = repository.save(BookingMapper.toBooking(bookingDto, item, user, BookingStatus.WAITING));
        return BookingMapper.toBookingResult(booking);
    }

    @Override
    @Transactional
    public BookingResult approved(long userId, long bookingId, boolean approved) {
        Booking booking = repository.getById(bookingId);
        if (userId != booking.getItem().getOwner().getId()) {
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
        return BookingMapper.toBookingResult(booking);
    }

    @Override
    public BookingResult getById(long userId, long id) {
        Booking booking = repository.getById(id);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new BookingNotFoundException("Бронирование не найдено");
        }
        return BookingMapper.toBookingResult(booking);
    }

    @Override
    public List<BookingResult> getAllByBooker(long userId, String state, int from, int size) {
        checkUser(userId);
        BookingStates st = BookingStates.findByName(state);
        switch (st) {
            case ALL:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerId(userId,
                        setPageRequest(from, size)));
            case PAST:
                return BookingMapper.mapToBookingResult(repository.findByBooker_IdAndEndIsBefore(userId,
                        LocalDateTime.now(), setPageRequest(from, size)));
            case FUTURE:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdFuture(userId,
                        setPageRequest(from, size)));
            case CURRENT:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdCurrent(userId,
                        setPageRequest(from, size)));
            case WAITING:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdWaiting(userId,
                        setPageRequest(from, size)));
            case REJECTED:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdRejected(userId,
                        setPageRequest(from, size)));
            default:
                throw new BookingIncorrectStateException("UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingResult> getAllByOwner(long userId, String state, int from, int size) {
        checkUser(userId);
        BookingStates st = BookingStates.findByName(state);
        switch (st) {
            case ALL:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerId(userId,
                        setPageRequest(from, size)));
            case PAST:
                return BookingMapper.mapToBookingResult(repository.findByItem_Owner_IdAndEndIsBefore(userId,
                        LocalDateTime.now(), setPageRequest(from, size)));
            case FUTURE:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdFuture(userId,
                        setPageRequest(from, size)));
            case CURRENT:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdCurrent(userId,
                        setPageRequest(from, size)));
            case WAITING:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdWaiting(userId,
                        setPageRequest(from, size)));
            case REJECTED:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdRejected(userId,
                        setPageRequest(from, size)));
            default:
                throw new BookingIncorrectStateException("UNSUPPORTED_STATUS");
        }
    }

    private void checkUser(long userId) {
        userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("Пользователь с таким ID не найден"));
    }

    private PageRequest setPageRequest(int from, int size) {
        return PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.DESC, "start"));
    }
}
