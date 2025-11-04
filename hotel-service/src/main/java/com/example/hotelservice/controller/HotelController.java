package com.example.hotelservice.controller;

import com.example.hotelservice.dto.HotelRequest;
import com.example.hotelservice.entity.Hotel;
import com.example.hotelservice.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Hotel addHotel(@RequestBody HotelRequest request) {
        return hotelService.addHotel(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }
}