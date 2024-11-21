package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select b.* from users u " +
            "right join booking b on b.booker_id = u.id " +
            "where u.id = ?1 " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByStateEqualsAll(Long userId);

    @Query(value = "select b.* from users u " +
            "right join booking b on b.booker_id = u.id " +
            "where u.id = ?1 and b.start_booking < now() and b.end_booking > now() " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByStateEqualsCurrent(Long userId);

    @Query(value = "select b.* from users u " +
            "right join booking b on b.booker_id = u.id " +
            "where u.id = ?1 and b.end_booking < now() " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByStateEqualsPast(Long userId);

    @Query(value = "select b.* from users u " +
            "right join booking b on b.booker_id = u.id " +
            "where u.id = ?1 and b.start_booking > now() " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByStateEqualsFuture(Long userId);

    @Query(value = "select b.* from users u " +
            "right join booking b on b.booker_id = u.id " +
            "where u.id = ?1 and b.status = 'WAITING'  " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByStateEqualsWaiting(Long userId);

    @Query(value = "select b.* from users u " +
            "right join booking b on b.booker_id = u.id " +
            "where u.id = ?1 and b.status = 'REJECTED' " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByStateEqualsRejected(Long userId);

    @Query(value = "select b.* from items i " +
            "right join booking b on b.item_id = i.id " +
            "where u.owner_id = ?1 " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByOwnerAndStateEqualsAll(Long userId);

    @Query(value = "select b.* from items i " +
            "right join booking b on b.item_id = i.id " +
            "where u.owner_id = ?1 and b.start_booking < now() and b.end_booking > now() " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByOwnerAndStateEqualsCurrent(Long userId);

    @Query(value = "select b.* from items i " +
            "right join booking b on b.item_id = i.id " +
            "where u.owner_id = ?1 and b.end_booking < now() " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByOwnerAndStateEqualsPast(Long userId);

    @Query(value = "select b.* from items i " +
            "right join booking b on b.item_id = i.id " +
            "where u.owner_id = ?1 and b.start_booking > now() " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByOwnerAndStateEqualsFuture(Long userId);

    @Query(value = "select b.* from items i " +
            "right join booking b on b.item_id = i.id " +
            "where u.owner_id = ?1 and b.status = 'WAITING'  " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByOwnerAndStateEqualsWaiting(Long userId);

    @Query(value = "select b.* from items i " +
            "right join booking b on b.item_id = i.id " +
            "where u.owner_id = ?1 and b.status = 'REJECTED' " +
            "order by b.start_booking desc ", nativeQuery = true)
    public List<Booking> findByOwnerAndStateEqualsRejected(Long userId);

    @Query(value = "select * from booking b " +
            "where b.end_booking < ?3 " +
            "and b.item_id = ?1 and b.booker_id = ?2 limit 1", nativeQuery = true)
    public Optional<Booking> findByItemAndBooker(Long itemId, Long userId, LocalDateTime now);
}
