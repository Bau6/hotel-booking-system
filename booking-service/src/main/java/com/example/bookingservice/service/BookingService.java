package com.example.bookingservice.service;

import com.example.bookingservice.entity.Booking;
import com.example.bookingservice.entity.BookingStatus;
import com.example.bookingservice.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AuthenticationService authenticationService;
    private final HotelClient hotelClient;

    @Transactional
    public Booking createBooking(Long userId, Long roomId, LocalDate start, LocalDate end) {

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);

        try {
            boolean ok = hotelClient.confirmAvailability(roomId, start, end);

            if (!ok) {
                booking.setStatus(BookingStatus.CANCELLED);
                return bookingRepository.save(booking);
            }

            booking.setStatus(BookingStatus.CONFIRMED);

        } catch (Exception e) {
            booking.setStatus(BookingStatus.CANCELLED);
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking getBookingByIdForUser(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new SecurityException("Access denied");
        }

        return booking;
    }

    @Transactional
    public void cancelBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new SecurityException("Access denied");
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            try {
                hotelClient.release(booking.getRoomId());
            } catch (Exception ignored) {}
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}