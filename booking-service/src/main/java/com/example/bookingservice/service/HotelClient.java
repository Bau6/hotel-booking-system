package com.example.bookingservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@FeignClient(
        name = "hotel-service",
        configuration = HotelClientConfig.class
)
public interface HotelClient {

    @PostMapping("/api/rooms/{roomId}/confirm-availability")
    Boolean confirmAvailability(
            @PathVariable("roomId") Long roomId,
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end
    );

    @PostMapping("/api/rooms/{roomId}/release")
    void release(@PathVariable("roomId") Long roomId);
}