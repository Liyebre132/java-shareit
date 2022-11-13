package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.item.exception.CommentIncorrectException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User user = userRepository.getById(userId);
        if (user.getName().isBlank()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        Item item = itemRepository.save(ItemMapper.toItem(itemDto, userId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long id, ItemDto itemDto) {
        userRepository.getById(userId);
        ItemDto currentItem = ItemMapper.toItemDto(itemRepository.getById(id));
        if (itemRepository.getById(id).getOwner() != userId) {
            throw new ItemNotFoundException("Такая вещь у пользователя не найдена");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            currentItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            currentItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            currentItem.setAvailable(itemDto.getAvailable());
        }
        Item item = itemRepository.save(ItemMapper.toItem(currentItem, userId));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getById(long userId, long id) {
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.getById(id));

        List<Booking> bookingList = bookingRepository.findByItem_Id(itemDto.getId());

        Booking nextBooking = bookingList.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min((Comparator.comparing(Booking::getStart)))
                .orElse(null);

        Booking lastBooking = bookingList.stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .max((Comparator.comparing(Booking::getEnd)))
                .orElse(null);

        if (nextBooking != null && nextBooking.getBooker().getId() != userId) {
            itemDto.setNextBooking(new NextBookingDto(nextBooking.getId(), nextBooking.getBooker().getId()));
        }
        if (lastBooking != null && lastBooking.getBooker().getId() != userId) {
            itemDto.setLastBooking(new LastBookingDto(lastBooking.getId(), lastBooking.getBooker().getId()));
        }

        itemDto.setComments(ItemMapper.mapToCommentDto(commentRepository.findByItem_Id(id)));

        return itemDto;
    }

    @Override
    public void delete(long userId, long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getAllByUser(long userId) {
        List<ItemDto> itemDtoList = ItemMapper.mapToItemDto(itemRepository.getAllByUser(userId));
        for (ItemDto itemDto : itemDtoList) {
            List<Booking> bookingList = bookingRepository.findByItem_Id(itemDto.getId());

            Booking nextBooking = bookingList.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .min((Comparator.comparing(Booking::getStart)))
                    .orElse(null);

            Booking lastBooking = bookingList.stream()
                    .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                    .max((Comparator.comparing(Booking::getEnd)))
                    .orElse(null);

            if (nextBooking != null) {
                itemDto.setNextBooking(new NextBookingDto(nextBooking.getId(), nextBooking.getBooker().getId()));
            }
            if (lastBooking != null) {
                itemDto.setLastBooking(new LastBookingDto(lastBooking.getId(), lastBooking.getBooker().getId()));
            }
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return ItemMapper.mapToItemDto(itemRepository.search(text));
        }
    }

    @Override
    public List<ItemDto> getAll() {
        return ItemMapper.mapToItemDto(itemRepository.findAll());
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        if (checkComment(userId, itemId)) {
            Item item = itemRepository.getById(itemId);
            User user = userRepository.getById(userId);
            Comment comment = commentRepository.save(ItemMapper.toComment(user, item, commentDto));
            return ItemMapper.toCommentDto(comment);
        } else {
            throw new CommentIncorrectException("Добавить комментарий не удалось");
        }

    }

    private boolean checkComment(long userId, long itemId) {
        List<Booking> bookingList = bookingRepository.findAllByBookerId(userId);
        Booking booking = bookingList.stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .max((Comparator.comparing(Booking::getEnd)))
                .orElse(null);
        return booking != null && booking.getItem().getId() == itemId &&
                booking.getStatus().equals(BookingStatus.APPROVED);
    }
}
