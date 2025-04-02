package com.masterpein.musicAPI.dto;

import java.util.Set;

import jakarta.validation.constraints.NotNull;



public class ArtistDTO {
	@NotNull
    private String genre;
    private Set<Long> eventIds;
	private Set<Long> venueIds;
    private Set<Long> ticketIds;
    
    public ArtistDTO() {}

	public ArtistDTO(@NotNull String genre, Set<Long> eventIds, Set<Long> venueIds, Set<Long> ticketIds) {
		this.genre = genre;
		this.eventIds = eventIds;
		this.venueIds = venueIds;
		this.ticketIds = ticketIds;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Set<Long> getEventIds() {
		return eventIds;
	}

	public void setEventIds(Set<Long> eventIds) {
		this.eventIds = eventIds;
	}

	public Set<Long> getVenueIds() {
		return venueIds;
	}

	public void setVenueIds(Set<Long> venueIds) {
		this.venueIds = venueIds;
	}

	public Set<Long> getTicketIds() {
		return ticketIds;
	}

	public void setTicketIds(Set<Long> ticketIds) {
		this.ticketIds = ticketIds;
	}

	public boolean isAvailable() {
		return false;
	}

	public void setAvailable(boolean available) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return "ArtistDTO [genre=" + genre + ", eventIds=" + eventIds + ", venueIds=" + venueIds + ", ticketIds="
				+ ticketIds + "]";
	}
    
}
