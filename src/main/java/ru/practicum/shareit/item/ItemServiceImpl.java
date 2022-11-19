package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.item.exception.CommentIncorrectException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

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
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemResult> getAllByUser(long userId) {
        List<Item> items = itemRepository.getAllByUser(userId);
        List<ItemResult> results = new ArrayList<>();
        for (Item item : items) {
            List<Item> currentItem = new ArrayList<>();
            currentItem.add(item);

            List<Comment> comments = commentRepository.findByItemIn(currentItem, Sort.by(DESC, "created"));
            List<Booking> bookings = bookingRepository.findByItemIn(currentItem, Sort.by(DESC, "start"));

            ItemResult itemResult = ItemMapper.toItemResult(item);
            results.add(itemResult);

            if (bookings != null) {
                Booking nextBooking = bookings.stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .reduce((first, second) -> second)
                        .orElse(null);
                Booking lastBooking = bookings.stream()
                        .filter(b -> b.getEnd().isEqual(LocalDateTime.now()) || b.getEnd().isBefore(LocalDateTime.now())
                        || (b.getStart().isEqual(LocalDateTime.now()) || b.getStart().isBefore(LocalDateTime.now())))
                        .findFirst()
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
            if (!comments.isEmpty()) {
                itemResult.setComments(ItemMapper.mapToCommentDto(comments));
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
                Sort.by(DESC, "start"));
        Booking booking = bookingList.stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .max((Comparator.comparing(Booking::getEnd)))
                .orElse(null);
        return booking != null && booking.getItem().getId() == itemId &&
                booking.getStatus().equals(BookingStatus.APPROVED);
    }
}
