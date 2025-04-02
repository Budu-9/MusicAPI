package com.masterpein.musicAPI.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterpein.musicAPI.entity.Artist;
import com.masterpein.musicAPI.entity.Event;
import com.masterpein.musicAPI.entity.Venue;
import com.masterpein.musicAPI.repository.ArtistRepository;
import com.masterpein.musicAPI.repository.EventRepository;
import com.masterpein.musicAPI.repository.VenueRepository;
import com.masterpein.musicAPI.service.exception.BusinessLogicException;
import com.masterpein.musicAPI.service.exception.ResourceNotFoundException;

@Service
public class EventService {
	private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    
    public EventService(EventRepository eventRepository, VenueRepository venueRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.artistRepository = artistRepository;
    }
    
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }
    
    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }
    
    public List<Event> findEventsByArtistId(Long artistId) {
        return eventRepository.findByArtistId(artistId);
    }
    
    public List<Event> findEventsByVenueId(Long venueId) {
        return eventRepository.findByVenueId(venueId);
    }
    
    public List<Event> findUpcomingEvents() {
        return eventRepository.findByDateTimeAfter(LocalDateTime.now());
    }
    
    @Transactional
    public Event createEvent(Event event, Long venueId, Long artistId) {
        // Validate venue and artist existence
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + venueId));
        
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + artistId));
        
        // Check if artist is available
        if (!artist.isAvailable()) {
            throw new BusinessLogicException("Artist is not available for booking");
        }
        
        // Check if the venue is already booked for the given time
        List<Event> existingEvents = eventRepository.findByVenueIdAndDateTimeBetween(
                venueId, 
                event.getDateTime().minusHours(4),  // Assuming an event lasts about 4 hours
                event.getDateTime().plusHours(4)
        );
        
        if (!existingEvents.isEmpty()) {
            throw new BusinessLogicException("Venue is already booked for this time");
        }
        
        // Set venue and artist
        event.setVenue(venue);
        event.setArtist(artist);
        
        // Set default values if not provided
        if (event.getAvailableTickets() == 0) {
            event.setAvailableTickets(venue.getCapacity());
        }
        
        return eventRepository.save(event);
    }
    
    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = findEventById(id);
        
        // Update event fields
        if (eventDetails.getDateTime() != null) {
            // Check venue availability for new time
            if (!event.getDateTime().equals(eventDetails.getDateTime())) {
                List<Event> existingEvents = eventRepository.findByVenueIdAndDateTimeBetween(
                        event.getVenue().getId(),
                        eventDetails.getDateTime().minusHours(4),
                        eventDetails.getDateTime().plusHours(4)
                );
                
                // Filter out the current event
                existingEvents.removeIf(e -> e.getId().equals(id));
                
                if (!existingEvents.isEmpty()) {
                    throw new BusinessLogicException("Venue is already booked for this time");
                }
            }
            
            event.setDateTime(eventDetails.getDateTime());
        }
        
        if (eventDetails.getTicketPrice() != null) {
            event.setTicketPrice(eventDetails.getTicketPrice());
        }
        
        return eventRepository.save(event);
    }
    
    public List<Event> searchEvents(String keyword, String genre, LocalDateTime fromDate) {
        if (fromDate == null) {
            fromDate = LocalDateTime.now();
        }
        
        if (keyword != null && genre != null) {
            return eventRepository.findByTitleContainingIgnoreCaseAndArtistGenreAndDateTimeAfter(
                    keyword, genre, fromDate);
        } else if (keyword != null) {
            return eventRepository.findByTitleContainingIgnoreCaseAndDateTimeAfter(
                    keyword, fromDate);
        } else if (genre != null) {
            return eventRepository.findByArtistGenreAndDateTimeAfter(
                    genre, fromDate);
        } else {
            return eventRepository.findByDateTimeAfter(fromDate);
        }
    }

	public void cancelEvent(Long id) {
		Event event = findEventById(id);
		eventRepository.delete(event);
	}
}
