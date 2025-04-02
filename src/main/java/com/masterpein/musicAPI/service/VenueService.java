package com.masterpein.musicAPI.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterpein.musicAPI.entity.Venue;
import com.masterpein.musicAPI.repository.VenueRepository;
import com.masterpein.musicAPI.service.exception.ResourceAlreadyExistsException;
import com.masterpein.musicAPI.service.exception.ResourceNotFoundException;
import com.masterpein.musicAPI.service.exception.ValidationException;

@Service
public class VenueService {
	private final VenueRepository venueRepository;
	
	public VenueService(VenueRepository venueRepository) {
		this.venueRepository = venueRepository;
	}
	
	public List<Venue> findAllVenues() {
        return venueRepository.findAll();
    }
	
	public Venue findVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
    }
	
	public List<Venue> findVenuesByCapacityGreaterThanEqual(int capacity) {
        return venueRepository.findByCapacityGreaterThanEqual(capacity);
    }
	
	@Transactional
	public Venue createVenue(Venue venue) {
		validateVenue(venue);
		// Check if venue name already exists
	    if (venueRepository.findByName(venue.getName()).isPresent()) {
	        throw new ResourceAlreadyExistsException("Name already exists: " + venue.getName());
	    }
	    
	    return venueRepository.save(venue);
	}
	
	private void validateVenue(Venue venue) {
        if (venue.getName().trim().isEmpty()) {
            throw new ValidationException("Venue name cannot be empty");
        }
        if (venue.getAddress().trim().isEmpty()) {
            throw new ValidationException("Venue location cannot be empty");
        }
        if (venue.getCapacity() < 10 || venue.getCapacity() > 100000) {
            throw new ValidationException("Capacity must be between 10 and 100,000");
        }
    }
	
	@Transactional
	public Venue updateVenue(Long id, Venue venueDetails) {
		Venue venue = findVenueById(id);
		
		// Update venue fields
        if (venueDetails.getName() != null) {
            venue.setName(venueDetails.getName());
        }
        
        if (venueDetails.getAddress() != null) {
            venue.setAddress(venueDetails.getAddress());
        }
        
        if (venueDetails.getCapacity() > 0) {
            venue.setCapacity(venueDetails.getCapacity());
        }
        
        return venueRepository.save(venue);
	}
	
	@Transactional
    public void deleteVenue(Long id) {
        Venue venue = findVenueById(id);
        venueRepository.delete(venue);
    }
	
	public List<Venue> searchVenuesByLocation(String city) {
        return venueRepository.findByAddressContainingIgnoreCase(city);
    }
}
