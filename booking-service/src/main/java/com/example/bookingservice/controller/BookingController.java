package com.example.bookingservice.controller;

import com.example.bookingservice.dto.BookingRequest;
import com.example.bookingservice.entity.Booking;
import com.example.bookingservice.service.AuthenticationService;
import com.example.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final AuthenticationService authService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody BookingRequest request, Authentication auth) {
        Long userId = authService.getCurrentUserId();
        Booking booking = bookingService.createBooking(
                userId,
                request.getRoomId(),
                request.getStartDate(),
                request.getEndDate()
        );
        return ResponseEntity.status(201).body(booking);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<Booking>> getUserBookings(Authentication auth) {
        Long userId = authService.getCurrentUserId();
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id, Authentication auth) {
        Long userId = authService.getCurrentUserId();
        return ResponseEntity.ok(bookingService.getBookingByIdForUser(id, userId));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication auth) {
        Long userId = authService.getCurrentUserId();
        bookingService.cancelBooking(id, userId);
        return ResponseEntity.noContent().build();
    }
}