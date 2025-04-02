package com.masterpein.musicAPI.dto;

import java.time.LocalDateTime;

public class BookingDTO {
	private String name;
	private String venueName;
	private String artistName;
	private Long userId;
    private Long eventId;
    private Long ticketId;
    private int quantity;
    private LocalDateTime bookingTime;
    
    public BookingDTO() {}

	public BookingDTO(String name, String venueName, String artistName, Long userId, Long eventId, Long ticketId, int quantity, LocalDateTime bookingTime) {
		this.name = name;
		this.venueName = venueName;
		this.artistName = artistName;
		this.userId = userId;
		this.eventId = eventId;
		this.ticketId = ticketId;
		this.quantity = quantity;
		this.bookingTime = bookingTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	@Override
	public String toString() {
		return "BookingDTO [userId=" + userId + ", eventId=" + eventId + ", ticketId=" + ticketId + ", quantity="
				+ quantity + ", bookingTime=" + bookingTime + "]";
	}
    
    
}
