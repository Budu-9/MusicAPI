package com.masterpein.musicAPI.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterpein.musicAPI.entity.Artist;
import com.masterpein.musicAPI.entity.Role;
import com.masterpein.musicAPI.entity.User;
import com.masterpein.musicAPI.repository.ArtistRepository;
import com.masterpein.musicAPI.repository.UserRepository;
import com.masterpein.musicAPI.service.exception.ResourceNotFoundException;

@Service
public class ArtistService {
	private final ArtistRepository artistRepository;
	private final UserRepository userRepository;
	
	public ArtistService(ArtistRepository artistRepository, UserRepository userRepository) {
		this.artistRepository = artistRepository;
		this.userRepository = userRepository;
	}
	
	public List<Artist> findAllArtists() {
        return artistRepository.findAll();
    }
	
	public Artist findArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + id));
    }
	
	public List<Artist> findArtistsByGenre(String genre) {
        return artistRepository.findByGenre(genre);
    }
	
	@Transactional
	public Artist createArtist(Artist artist, Long userId) {
		// Find the user
        User user = userRepository.findById(userId)
        		.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Change user role to ARTIST if it's not already
        if (!user.getRole().equals(Role.ARTIST)) {
            user.setRole(Role.ARTIST);
            userRepository.save(user);
        }
        
        // Set the user for the artist
        artist.setUser(user);
        
        return artistRepository.save(artist);
	}
	
	@Transactional
	public Artist updateArtist(Long id, Artist artistDetails) {
		Artist artist = findArtistById(id);
		
		// Update artist fields
        if (artistDetails.getName() != null) {
            artist.setName(artistDetails.getName());
        }
        
        if (artistDetails.getGenre() != null) {
            artist.setGenre(artistDetails.getGenre());
        }
        
        return artistRepository.save(artist);
	}
	
	@Transactional
	public void deleteArtist(Long id) {
		Artist artist = findArtistById(id);
		artistRepository.delete(artist);
	}
	
	@Transactional
	public Artist updateArtistAvailability(Long id, boolean isAvailable) {
		Artist artist = findArtistById(id);
        artist.setAvailable(isAvailable);
        return artistRepository.save(artist);
	}
	
}
