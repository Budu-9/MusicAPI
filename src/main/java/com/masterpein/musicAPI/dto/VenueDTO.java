package com.masterpein.musicAPI.dto;

import java.util.Set;

public class VenueDTO {
	private String name;
	private String address;
	private int capacity;
	private Set<Long> eventIds;
	private Set<Long> artistIds;
	
	public VenueDTO() {}

	public VenueDTO(String name, String address, int capacity, Set<Long> eventIds, Set<Long> artistIds) {
		this.name = name;
		this.address = address;
		this.capacity = capacity;
		this.eventIds = eventIds;
		this.artistIds = artistIds;
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

	public Set<Long> getEventIds() {
		return eventIds;
	}

	public void setEventIds(Set<Long> eventIds) {
		this.eventIds = eventIds;
	}

	public Set<Long> getArtistIds() {
		return artistIds;
	}

	public void setArtistIds(Set<Long> artistIds) {
		this.artistIds = artistIds;
	}

	@Override
	public String toString() {
		return "VenueDTO [name=" + name + ", address=" + address + ", capacity=" + capacity + ", eventIds=" + eventIds
				+ ", artistIds=" + artistIds + "]";
	}
	
}
