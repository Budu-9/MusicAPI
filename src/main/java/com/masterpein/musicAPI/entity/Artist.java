package com.masterpein.musicAPI.entity;

import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Artist {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@OneToOne
	private User user;
    private String name;
    private String genre;
    private boolean available;
    @OneToMany(mappedBy = "artist")
    private Set<Event> events;
    @OneToMany(mappedBy = "artist")
    private Set<Venue> venues;
    
    
    public Artist() {}

    public Artist(User user, String name, String genre, boolean available) {
    	this.user = user;
        this.name = name;
        this.genre = genre;
        this.setAvailable(available);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public Set<Venue> getVenues() {
		return venues;
	}

	public void setVenues(Set<Venue> venues) {
		this.venues = venues;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	

}
