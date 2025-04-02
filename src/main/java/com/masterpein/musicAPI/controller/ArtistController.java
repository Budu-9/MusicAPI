package com.masterpein.musicAPI.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.masterpein.musicAPI.dto.ArtistDTO;
import com.masterpein.musicAPI.entity.Artist;
import com.masterpein.musicAPI.service.ArtistService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {
	private final ArtistService artistService;
	
	public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }
	
	@GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<Artist> artists = artistService.findAllArtists();
        List<ArtistDTO> artistDTOs = artists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(artistDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getArtistById(@PathVariable Long id) {
        Artist artist = artistService.findArtistById(id);
        return new ResponseEntity<>(convertToDTO(artist), HttpStatus.OK);
    }
	
	@GetMapping("/genre/{genre}")
    public ResponseEntity<List<ArtistDTO>> getArtistsByGenre(@PathVariable String genre) {
        List<Artist> artists = artistService.findArtistsByGenre(genre);
        List<ArtistDTO> artistDTOs = artists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(artistDTOs, HttpStatus.OK);
    }
	
	@GetMapping("/search")
    public ResponseEntity<List<ArtistDTO>> searchArtists(@RequestParam String keyword) {
        List<Artist> artists = artistService.findArtistsByGenre(keyword);
        List<ArtistDTO> artistDTOs = artists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(artistDTOs, HttpStatus.OK);
    }
	
	@PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ArtistDTO> createArtist(@Valid @RequestBody ArtistDTO artistDTO, 
                                                  @RequestAttribute("userId") Long userId) {
        Artist artist = convertToEntity(artistDTO);
        Artist createdArtist = artistService.createArtist(artist, userId);
        return new ResponseEntity<>(convertToDTO(createdArtist), HttpStatus.CREATED);
    }
	
	@PutMapping("/{id}")
    @PreAuthorize("@securityService.isArtistOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<ArtistDTO> updateArtist(@PathVariable Long id, @Valid @RequestBody ArtistDTO artistDTO) {
        Artist artist = convertToEntity(artistDTO);
        Artist updatedArtist = artistService.updateArtist(id, artist);
        return new ResponseEntity<>(convertToDTO(updatedArtist), HttpStatus.OK);
    }
	
	@DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isArtistOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return new ResponseEntity<>(new ApiResponse(true, "Artist deleted successfully"), HttpStatus.OK);
    }
	
	private ArtistDTO convertToDTO(Artist artist) {
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setGenre(artist.getGenre());
        artistDTO.setAvailable(artist.isAvailable());
        return artistDTO;
    }

    private Artist convertToEntity(ArtistDTO artistDTO) {
        Artist artist = new Artist();
        artist.setGenre(artistDTO.getGenre());
        artist.setAvailable(artistDTO.isAvailable());
        return artist;
    }
}
