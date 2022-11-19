package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<BookingResult> getAllByBooker(long userId, String state) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        BookingStates st = BookingStates.findByName(state);
        switch (st) {
            case ALL:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerId(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case PAST:
                return BookingMapper.mapToBookingResult(repository.findByBooker_IdAndEndIsBefore(userId,
                        LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")));
            case FUTURE:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdFuture(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case CURRENT:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdCurrent(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case WAITING:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdWaiting(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case REJECTED:
                return BookingMapper.mapToBookingResult(repository.findAllByBookerIdRejected(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            default:
                throw new BookingIncorrectStateException("UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingResult> getAllByOwner(long userId, String state) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        BookingStates st = BookingStates.findByName(state);
        switch (st) {
            case ALL:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerId(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case PAST:
                return BookingMapper.mapToBookingResult(repository.findByItem_Owner_IdAndEndIsBefore(userId,
                        LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start")));
            case FUTURE:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdFuture(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case CURRENT:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdCurrent(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case WAITING:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdWaiting(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            case REJECTED:
                return BookingMapper.mapToBookingResult(repository.findAllByOwnerIdRejected(userId,
                        Sort.by(Sort.Direction.DESC, "start")));
            default:
                throw new BookingIncorrectStateException("UNSUPPORTED_STATUS");
        }
    }
}
