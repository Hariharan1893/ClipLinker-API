package com.cliplinker.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "click_analytics")
public class ClickAnalytics {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "short_url_id", nullable = false)
	private ShortenedUrl shortUrl;
	
	private String ipAddress;
	
	private LocalDateTime timestamp = LocalDateTime.now();

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ShortenedUrl getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(ShortenedUrl shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public ClickAnalytics(UUID id, ShortenedUrl shortUrl, String ipAddress, LocalDateTime timestamp) {
		super();
		this.id = id;
		this.shortUrl = shortUrl;
		this.ipAddress = ipAddress;
		this.timestamp = timestamp;
	}
	
	public ClickAnalytics() {
		super();
	}
}
