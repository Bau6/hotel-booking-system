package com.example.hotelservice.repository;

import com.example.hotelservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByAvailableTrue();

    @Query("SELECT r FROM Room r ORDER BY r.timesBooked DESC")
    List<Room> findRecommendedRooms();
}