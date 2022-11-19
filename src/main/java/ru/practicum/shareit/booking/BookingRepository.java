package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByItem_Id(Long itemId);

    List<Booking> findByItemIn(Collection<Item> items, Sort sort);

    List<Booking> findByBooker_IdAndEndIsBefore(Long userId, LocalDateTime localDateTime, Sort sort);

    List<Booking> findByItem_Owner_IdAndEndIsBefore(Long userId, LocalDateTime localDateTime, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1")
    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.end BETWEEN b.start and b.end AND " +
            "b.status = 'REJECTED'")
    List<Booking> findAllByBookerIdCurrent(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.start > current_date")
    List<Booking> findAllByBookerIdFuture(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.status = 'WAITING'")
    List<Booking> findAllByBookerIdWaiting(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.status = 'REJECTED'")
    List<Booking> findAllByBookerIdRejected(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1")
    List<Booking> findAllByOwnerId(Long ownerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.end BETWEEN b.start and b.end AND " +
            "b.status = 'REJECTED'")
    List<Booking> findAllByOwnerIdCurrent(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.start > current_date")
    List<Booking> findAllByOwnerIdFuture(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.status = 'WAITING'")
    List<Booking> findAllByOwnerIdWaiting(Long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "b.status = 'REJECTED'")
    List<Booking> findAllByOwnerIdRejected(Long bookerId, Sort sort);
}
