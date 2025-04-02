package com.masterpein.musicAPI.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterpein.musicAPI.entity.Booking;
import com.masterpein.musicAPI.entity.BookingStatus;
import com.masterpein.musicAPI.entity.Event;
import com.masterpein.musicAPI.entity.User;
import com.masterpein.musicAPI.repository.BookingRepository;
import com.masterpein.musicAPI.repository.EventRepository;
import com.masterpein.musicAPI.repository.UserRepository;
import com.masterpein.musicAPI.service.exception.BusinessLogicException;
import com.masterpein.musicAPI.service.exception.ResourceNotFoundException;

@Service
public class BookingService {
	private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    public BookingService(BookingRepository bookingRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }
    
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }
    
    public List<Booking> findBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public List<Booking> findBookingsByEventId(Long eventId) {
        return bookingRepository.findByEventId(eventId);
    }
    
    @Transactional
    public Booking createBooking(Long userId, Long eventId, int ticketCount) {
        // Validate user and event existence
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        
        // Check if event is in the past
        if (event.getDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException("Cannot book tickets for a past event");
        }
        
        // Check ticket availability
        if (event.getAvailableTickets() < ticketCount) {
            throw new BusinessLogicException("Not enough tickets available. Only " + event.getAvailableTickets() + " tickets left.");
        }
        
        // Create new booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setTicketCount(ticketCount);
        booking.setTotalAmount(event.getTicketPrice().multiply(new BigDecimal(ticketCount)));
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING); // Will be updated after payment
        
        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        
        // Update event's available tickets
        event.setAvailableTickets(event.getAvailableTickets() - ticketCount);
        eventRepository.save(event);
        
        return savedBooking;
    }
    
    @Transactional
    public Booking confirmBooking(Long bookingId) {
    	Booking booking = findBookingById(bookingId);
    	
    	// Ensure booking is in PENDING state
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BusinessLogicException("Booking is not in PENDING state");
        }
        
        // Update booking status
        booking.setStatus(BookingStatus.CONFIRMED);
        
        return bookingRepository.save(booking);
    }
    
    @Transactional
    public Booking cancelBooking(Long bookingId) {
    	Booking booking = findBookingById(bookingId);
    	
    	// Check if booking can be cancelled (e.g., not too close to event date)
        Event event = booking.getEvent();
        LocalDateTime cancellationDeadline = event.getDateTime().minusDays(1);
        
        if (LocalDateTime.now().isAfter(cancellationDeadline)) {
            throw new BusinessLogicException("Cannot cancel booking less than 24 hours before event");
        }
        
        // Update event's available tickets
        event.setAvailableTickets(event.getAvailableTickets() + booking.getTicketCount());
        eventRepository.save(event);
        
        // Update booking status
        booking.setStatus(BookingStatus.CANCELLED);
        
        return bookingRepository.save(booking);
    }
    
}
