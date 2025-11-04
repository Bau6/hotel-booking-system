package com.example.hotelservice.service;

import com.example.hotelservice.entity.Hotel;
import com.example.hotelservice.entity.Room;
import com.example.hotelservice.entity.RoomLock;
import com.example.hotelservice.repository.HotelRepository;
import com.example.hotelservice.repository.RoomLockRepository;
import com.example.hotelservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomLockRepository roomLockRepository;

    public Room addRoom(Long hotelId, String number) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Room room = new Room();
        room.setHotel(hotel);
        room.setNumber(number);
        room.setTimesBooked(0);

        return roomRepository.save(room);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    public List<Room> getRecommendedRooms() {
        return roomRepository.findRecommendedRooms();
    }

    @Transactional
    public String confirmAvailability(Long roomId, LocalDate start, LocalDate end) {
        List<RoomLock> locks =
                roomLockRepository.findByRoomIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
                        roomId, start, end);

        if (!locks.isEmpty()) {
            return "BUSY";
        }

        String lockId = UUID.randomUUID().toString();

        RoomLock lock = new RoomLock();
        lock.setLockId(lockId);
        lock.setRoomId(roomId);
        lock.setStartDate(start);
        lock.setEndDate(end);
        roomLockRepository.save(lock);

        return lockId;
    }

    @Transactional
    public void releaseLock(String lockId) {
        roomLockRepository.deleteByLockId(lockId);
    }
}