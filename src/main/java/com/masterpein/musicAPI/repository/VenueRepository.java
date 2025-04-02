package com.masterpein.musicAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterpein.musicAPI.entity.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {

	List<Venue> findByCapacityGreaterThanEqual(int capacity);

	Optional<Venue> findByName(String name);

	List<Venue> findByAddressContainingIgnoreCase(String city);
}