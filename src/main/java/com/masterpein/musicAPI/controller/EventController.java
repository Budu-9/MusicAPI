package com.masterpein.musicAPI.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.masterpein.musicAPI.dto.EventDTO;
import com.masterpein.musicAPI.entity.Event;
import com.masterpein.musicAPI.service.EventService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/events")
public class EventController {
	private final EventService eventService;
	
	public EventController(EventService eventService) {
        this.eventService = eventService;
    }
	
	@GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<Event> events = eventService.findAllEvents();
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        Event event = eventService.findEventById(id);
        return new ResponseEntity<>(convertToDTO(event), HttpStatus.OK);
    }
	
	@GetMapping("/artist/{artistId}")
    public ResponseEntity<List<EventDTO>> getEventsByArtist(@PathVariable Long artistId) {
        List<Event> events = eventService.findEventsByArtistId(artistId);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/venue/{venueId}")
    public ResponseEntity<List<EventDTO>> getEventsByVenue(@PathVariable Long venueId) {
        List<Event> events = eventService.findEventsByVenueId(venueId);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventDTOs, HttpStatus.OK);
    }
	
	@PutMapping("/{id}")
    @PreAuthorize("@securityService.isEventOrganizer(#id) or hasRole('ADMIN')")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        Event updatedEvent = eventService.updateEvent(id, event);
        return new ResponseEntity<>(convertToDTO(updatedEvent), HttpStatus.OK);
    }
	
	@DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isEventOrganizer(#id) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> cancelEvent(@PathVariable Long id) {
        eventService.cancelEvent(id);
        return new ResponseEntity<>(new ApiResponse(true, "Event cancelled successfully"), HttpStatus.OK);
    }
	
	private EventDTO convertToDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setDescription(event.getDescription());
        eventDTO.setDateTime(event.getDateTime());
        
        if (event.getVenue() != null) {
            eventDTO.setVenueId(event.getVenue().getId());
            eventDTO.setVenueName(event.getVenue().getName());
        }
        
        if (event.getArtist() != null) {
            eventDTO.setArtistId(event.getArtist().getId());
            eventDTO.setArtistName(event.getArtist().getName());
        }
        
        return eventDTO;
    }

    private Event convertToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setDescription(eventDTO.getDescription());
        event.setDateTime(eventDTO.getDateTime());
        return event;
    }
}
