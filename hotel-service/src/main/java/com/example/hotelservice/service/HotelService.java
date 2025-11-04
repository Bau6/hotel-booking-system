package com.example.hotelservice.service;

import com.example.hotelservice.dto.HotelRequest;
import com.example.hotelservice.entity.Hotel;
import com.example.hotelservice.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public Hotel addHotel(HotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setAddress(request.getAddress());
        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
}