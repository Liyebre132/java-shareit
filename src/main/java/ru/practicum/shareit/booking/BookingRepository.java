package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByItem_Id(Long itemId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerId(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.end > current_date AND " +
            "b.status = 'REJECTED'" +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdCurrent(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.status = 'APPROVED'" +
            "ORDER BY b.end")
    List<Booking> findAllByBookerIdPast(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.start > current_date " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdFuture(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.status = 'WAITING' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdWaiting(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 AND " +
            "b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdRejected(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner = ?1 AND " +
            "b.end > current_date AND " +
            "b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdCurrent(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner = ?1 AND " +
            "b.status = 'APPROVED' " +
            "ORDER BY b.end")
    List<Booking> findAllByOwnerIdPast(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner = ?1 AND " +
            "b.start > current_date " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdFuture(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner = ?1 AND " +
            "b.status = 'WAITING' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdWaiting(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner = ?1 AND " +
            "b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdRejected(Long bookerId);
}
