package com.example.hotelservice.controller;

import com.example.hotelservice.dto.RoomRequest;
import com.example.hotelservice.entity.Room;
import com.example.hotelservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Room addRoom(@RequestBody RoomRequest request) {
        return roomService.addRoom(request.getHotelId(), request.getNumber());
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Room> getAllAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @GetMapping("/recommend")
    @PreAuthorize("permitAll()")
    public List<Room> recommendRooms() {
        return roomService.getRecommendedRooms();
    }

    @PostMapping("/{roomId}/confirm-availability")
    @PreAuthorize("hasRole('USER')")
    public String confirmAvailability(
            @PathVariable Long roomId,
            @RequestParam("start") String start,
            @RequestParam("end") String end
    ) {
        return roomService.confirmAvailability(roomId, LocalDate.parse(start), LocalDate.parse(end));
    }

    @PostMapping("/release")
    @PreAuthorize("hasRole('USER')")
    public void release(@RequestParam("lockId") String lockId) {
        roomService.releaseLock(lockId);
    }
}