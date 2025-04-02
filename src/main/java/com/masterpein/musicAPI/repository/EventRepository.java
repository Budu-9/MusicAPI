package com.masterpein.musicAPI.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterpein.musicAPI.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDateTimeAfter(LocalDateTime dateTime);

	List<Event> findByArtistId(Long artistId);

	List<Event> findByVenueId(Long venueId);

	List<Event> findByVenueIdAndDateTimeBetween(Long venueId, LocalDateTime minusHours, LocalDateTime plusHours);

	List<Event> findByTitleContainingIgnoreCaseAndArtistGenreAndDateTimeAfter(String keyword, String genre,
			LocalDateTime fromDate);

	List<Event> findByTitleContainingIgnoreCaseAndDateTimeAfter(String keyword, LocalDateTime fromDate);

	List<Event> findByArtistGenreAndDateTimeAfter(String genre, LocalDateTime fromDate);
}
