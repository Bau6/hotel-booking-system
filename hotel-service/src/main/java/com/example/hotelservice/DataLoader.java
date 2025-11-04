package com.example.hotelservice;

import com.example.hotelservice.entity.Hotel;
import com.example.hotelservice.entity.Room;
import com.example.hotelservice.repository.HotelRepository;
import com.example.hotelservice.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public DataLoader(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        Hotel h1 = new Hotel();
        h1.setName("Hilton");
        h1.setAddress("Main Street 1");
        hotelRepository.save(h1);

        Room r1 = new Room();
        r1.setNumber("101");
        r1.setHotel(h1);

        Room r2 = new Room();
        r2.setNumber("102");
        r2.setHotel(h1);

        roomRepository.save(r1);
        roomRepository.save(r2);
    }
}