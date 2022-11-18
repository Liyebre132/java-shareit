package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.item.exception.CommentIncorrectException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemResult addNewItem(long userId, ItemDto itemDto) {
        User user = userRepository.getById(userId);
        if (user.getName().isBlank()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        ItemResult itemResult = new ItemResult();
        itemResult.setName(itemDto.getName());
        itemResult.setDescription(itemDto.getDescription());
        itemResult.setAvailable(itemDto.getAvailable());
        Item item = itemRepository.save(ItemMapper.toItem(itemResult, user));
        return ItemMapper.toItemResult(item);
    }

    @Override
    @Transactional
    public ItemResult update(long userId, long id, ItemDto itemDto) {
        Item currentItem = itemRepository.getById(id);
        if (itemRepository.getById(id).getOwner().getId() != userId) {
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
        return ItemMapper.toItemResult(currentItem);
    }

    @Override
    public ItemResult getById(long userId, long id) {
        ItemResult itemResult = ItemMapper.toItemResult(itemRepository.getById(id));

        List<Booking> bookingList = bookingRepository.findByItem_Id(itemResult.getId());

        Booking nextBooking = bookingList.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min((Comparator.comparing(Booking::getStart)))
                .orElse(null);

        Booking lastBooking = bookingList.stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .max((Comparator.comparing(Booking::getEnd)))
                .orElse(null);

        if (nextBooking != null && nextBooking.getBooker().getId() != userId) {
            itemResult.setNextBooking(new ItemResult.NextBooking(nextBooking.getId(), nextBooking.getBooker().getId()));
        }
        if (lastBooking != null && lastBooking.getBooker().getId() != userId) {
            itemResult.setLastBooking(new ItemResult.LastBooking(lastBooking.getId(), lastBooking.getBooker().getId()));
        }

        itemResult.setComments(ItemMapper.mapToCommentDto(commentRepository.findByItem_Id(id)));

        return itemResult;
    }

    @Override
    @Transactional
    public void delete(long userId, long id) {
//        List<Comment> comments = commentRepository.findAll();
//        Map<Long, List<Comment>> commentsMap = comments.stream()
//                        .collect(Collectors.groupingBy(k -> k.getItem().getId()));
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemResult> getAllByUser(long userId) {
        Map<Long, List<Booking>> bookingsMap = bookingRepository.findAll().stream()
                        .collect(Collectors.groupingBy(k -> k.getItem().getId()));
        Map<Long, List<Comment>> commentsMap = commentRepository.findAll().stream()
                        .collect(Collectors.groupingBy(k -> k.getItem().getId()));
        List<ItemResult> results = ItemMapper.mapToItemDto(itemRepository.getAllByUser(userId));
        for (ItemResult itemResult : results) {
            List<Booking> bookingList = bookingsMap.get(itemResult.getId());
            if (bookingList != null) {
                Booking nextBooking = bookingList.stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .min((Comparator.comparing(Booking::getStart)))
                        .orElse(null);
                Booking lastBooking = bookingList.stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .max((Comparator.comparing(Booking::getEnd)))
                        .orElse(null);
                if (nextBooking != null) {
                    itemResult.setNextBooking(new ItemResult.NextBooking(nextBooking.getId(),
                            nextBooking.getBooker().getId()));
                }
                if (lastBooking != null) {
                    itemResult.setLastBooking(new ItemResult.LastBooking(lastBooking.getId(),
                            lastBooking.getBooker().getId()));
                }
            }
            if (commentsMap.get(itemResult.getId()) != null) {
                itemResult.setComments(ItemMapper.mapToCommentDto(commentsMap.get(itemResult.getId())));
            }
        }
        return results;
    }

    @Override
    public List<ItemResult> search(String text) {
        return ItemMapper.mapToItemDto(itemRepository.search(text));
    }

    @Override
    public List<ItemResult> getAll() {
        return ItemMapper.mapToItemDto(itemRepository.findAll());
    }

    @Override
    @Transactional
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
        List<Booking> bookingList = bookingRepository.findAllByBookerId(userId,
                Sort.by(Sort.Direction.DESC, "start"));
        Booking booking = bookingList.stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .max((Comparator.comparing(Booking::getEnd)))
                .orElse(null);
        return booking != null && booking.getItem().getId() == itemId &&
                booking.getStatus().equals(BookingStatus.APPROVED);
    }
}
