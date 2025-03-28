package com.cliplinker.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cliplinker.service.ClickAnalyticsService;
import com.cliplinker.service.RateLimitingService;
import com.cliplinker.service.URLShortenerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class URLShortenerController {

	private final URLShortenerService urlShortenerService;
	private final ClickAnalyticsService analyticsService;
	private final RateLimitingService rateLimitingService;

	public URLShortenerController(URLShortenerService urlShortenerService, ClickAnalyticsService analyticsService,
			RateLimitingService rateLimitingService) {
		this.urlShortenerService = urlShortenerService;
		this.analyticsService = analyticsService;
		this.rateLimitingService = rateLimitingService;
	}

	// Shorten a URL

	@PostMapping("/shorten")
	public ResponseEntity<?> shortenURL(@RequestBody Map<String, String> request) {

		String originalUrl = request.get("originalUrl");
		String custromAlias = request.get("customAlias");
		LocalDateTime expireTime = request.get("expiryTime") != null ? LocalDateTime.parse(request.get("expiryTime"))
				: null;

		String shortUrl = urlShortenerService.shortenURL(originalUrl, custromAlias, expireTime);

		return ResponseEntity.ok(Map.of("ShortUrl", shortUrl));
	}

	// Redirect to original URL & track click rate

	@GetMapping("/{shortUrl}")
	public ResponseEntity<?> redirectToOriginalURL(@PathVariable String shortUrl, HttpServletRequest request) {
		if (!rateLimitingService.isAllowed(request.getRemoteAddr())) {
			return ResponseEntity.status(429).body(Map.of("error", "Too many requests. Please try again later."));
		}

		String originalUrl = urlShortenerService.getOriginalURL(shortUrl);
		analyticsService.trackClick(shortUrl, request.getRemoteAddr());

		return ResponseEntity.ok(Map.of("originalUrl", originalUrl));
	}

	// Delete a shortened URL

	@DeleteMapping("/{shortUrl}")
	public ResponseEntity<?> deleteShortURL(@PathVariable String shortUrl) {
		urlShortenerService.deleteShortURL(shortUrl);

		return ResponseEntity.ok(Map.of("message", "Shortened URL deleted Successfully."));
	}
}
