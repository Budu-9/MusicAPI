package com.masterpein.musicAPI.entity;

import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Venue {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private int capacity;
    @OneToMany(mappedBy = "venue")
    private Set<Event> events;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
    
    public Venue() {}
    
	public Venue(Long id, String name, String address, int capacity, User user, Artist artist) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.capacity = capacity;
		this.user = user;
		this.artist = artist;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
    
}
