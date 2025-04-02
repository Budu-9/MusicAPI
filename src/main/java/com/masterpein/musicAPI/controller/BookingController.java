package com.masterpein.musicAPI.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.masterpein.musicAPI.dto.BookingDTO;
import com.masterpein.musicAPI.entity.Booking;
import com.masterpein.musicAPI.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	private final BookingService bookingService;
	
	public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
	
	@GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = bookingService.findAllBookings();
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    @PreAuthorize("@securityService.isBookingOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.findBookingById(id);
        return new ResponseEntity<>(convertToDTO(booking), HttpStatus.OK);
    }
	
	@GetMapping("/user")
    public ResponseEntity<List<BookingDTO>> getMyBookings(@RequestAttribute("userId") Long userId) {
        List<Booking> bookings = bookingService.findBookingsByUserId(userId);
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/event/{eventId}")
    @PreAuthorize("@securityService.isEventOrganizer(#eventId) or hasRole('ADMIN')")
    public ResponseEntity<List<BookingDTO>> getBookingsByEvent(@PathVariable Long eventId) {
        List<Booking> bookings = bookingService.findBookingsByEventId(eventId);
        List<BookingDTO> bookingDTOs = bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
    }
	
	@PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isBookingOwner(#id)")
    public ResponseEntity<BookingDTO> confirmBooking(@PathVariable Long id) {
        Booking confirmedBooking = bookingService.confirmBooking(id);
        return new ResponseEntity<>(convertToDTO(confirmedBooking), HttpStatus.OK);
    }
	
	@PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isBookingOwner(#id)")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long id) {
        Booking cancelledBooking = bookingService.cancelBooking(id);
        return new ResponseEntity<>(convertToDTO(cancelledBooking), HttpStatus.OK);
    }
	
	private BookingDTO convertToDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingTime(booking.getBookingTime());
        
        if (booking.getUser() != null) {
            bookingDTO.setUserId(booking.getUser().getId());
            bookingDTO.setName(booking.getUser().getUsername());
        }
        
        if (booking.getEvent() != null) {
            bookingDTO.setEventId(booking.getEvent().getId());
            bookingDTO.setBookingTime(booking.getEvent().getDateTime());
            
            if (booking.getEvent().getVenue() != null) {
                bookingDTO.setVenueName(booking.getEvent().getVenue().getName());
            }
            
            if (booking.getEvent().getArtist() != null) {
                bookingDTO.setArtistName(booking.getEvent().getArtist().getName());
            }
        }
        
        return bookingDTO;
    }
}
