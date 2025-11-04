package com.example.hotelservice.repository;

import com.example.hotelservice.entity.RoomLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoomLockRepository extends JpaRepository<RoomLock, Long> {

    List<RoomLock> findByRoomIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
            Long roomId, LocalDate start, LocalDate end);

    void deleteByLockId(String lockId);
}