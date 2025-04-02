package com.masterpein.musicAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterpein.musicAPI.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
	List<Artist> findByGenre(String genre);
	Optional<Artist> findByName(String name);
}
