package com.cliplinker.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shortened_urls")
public class ShortenedUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String originalUrl;

	@Column(unique = true, nullable = false)
	private String shortUrl;

	@Column(unique = true)
	private String customAlias;

	private LocalDateTime expiryTime;

	private int clickCount = 0;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getCustomAlias() {
		return customAlias;
	}

	public void setCustomAlias(String customAlias) {
		this.customAlias = customAlias;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public ShortenedUrl(UUID id, String originalUrl, String shortUrl, String customAlias, LocalDateTime expiryTime,
			int clickCount) {
		super();
		this.id = id;
		this.originalUrl = originalUrl;
		this.shortUrl = shortUrl;
		this.customAlias = customAlias;
		this.expiryTime = expiryTime;
		this.clickCount = clickCount;
	}

	public ShortenedUrl() {
		super();
	}
}
