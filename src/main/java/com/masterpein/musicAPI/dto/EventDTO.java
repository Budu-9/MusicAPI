package com.masterpein.musicAPI.dto;

import java.time.LocalDateTime;

public class EventDTO {
    private String name;
    private String description;
    private String venueName;
    private String artistName;
    private LocalDateTime dateTime;
    private Long artistId;
    private Long venueId;

    public EventDTO() {}

	public EventDTO(String name, String description, String venueName, String artistName, LocalDateTime dateTime, Long artistId, Long venueId) {
		this.name = name;
		this.description = description;
		this.venueName = venueName;
		this.artistName = artistName;
		this.dateTime = dateTime;
		this.artistId = artistId;
		this.venueId = venueId;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Long getArtistId() {
		return artistId;
	}

	public void setArtistId(Long artistId) {
		this.artistId = artistId;
	}

	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	@Override
	public String toString() {
		return "EventDTO [name=" + name + ", description=" + description + ", venueName=" + venueName + ", artistName="
				+ artistName + ", dateTime=" + dateTime + ", artistId=" + artistId + ", venueId=" + venueId + "]";
	}

	
    
}
