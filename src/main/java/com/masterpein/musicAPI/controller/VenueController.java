package com.masterpein.musicAPI.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.masterpein.musicAPI.dto.VenueDTO;
import com.masterpein.musicAPI.entity.Venue;
import com.masterpein.musicAPI.service.VenueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
	private final VenueService venueService;
	
	public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }
	
	@GetMapping
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        List<Venue> venues = venueService.findAllVenues();
        List<VenueDTO> venueDTOs = venues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(venueDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        Venue venue = venueService.findVenueById(id);
        return new ResponseEntity<>(convertToDTO(venue), HttpStatus.OK);
    }
	
	@GetMapping("/capacity")
    public ResponseEntity<List<VenueDTO>> getVenuesByCapacity(@RequestParam int minCapacity) {
        List<Venue> venues = venueService.findVenuesByCapacityGreaterThanEqual(minCapacity);
        List<VenueDTO> venueDTOs = venues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(venueDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/location")
    public ResponseEntity<List<VenueDTO>> getVenuesByLocation(@RequestParam String city) {
        List<Venue> venues = venueService.searchVenuesByLocation(city);
        List<VenueDTO> venueDTOs = venues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(venueDTOs, HttpStatus.OK);
    }
	
	@PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueDTO venueDTO) {
        Venue venue = convertToEntity(venueDTO);
        Venue createdVenue = venueService.createVenue(venue);
        return new ResponseEntity<>(convertToDTO(createdVenue), HttpStatus.CREATED);
    }
	
	@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueDTO> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueDTO venueDTO) {
        Venue venue = convertToEntity(venueDTO);
        Venue updatedVenue = venueService.updateVenue(id, venue);
        return new ResponseEntity<>(convertToDTO(updatedVenue), HttpStatus.OK);
    }
	
	@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isVenueOwner(#id)")
    public ResponseEntity<ApiResponse> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return new ResponseEntity<>(new ApiResponse(true, "Venue deleted successfully"), HttpStatus.OK);
    }
	
	private VenueDTO convertToDTO(Venue venue) {
        VenueDTO venueDTO = new VenueDTO();
        venueDTO.setName(venue.getName());
        venueDTO.setAddress(venue.getAddress());
        venueDTO.setCapacity(venue.getCapacity());
        return venueDTO;
    }

    private Venue convertToEntity(VenueDTO venueDTO) {
        Venue venue = new Venue();
        venue.setName(venueDTO.getName());
        venue.setAddress(venueDTO.getAddress());
        venue.setCapacity(venueDTO.getCapacity());
        return venue;
    }
}
