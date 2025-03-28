package com.cliplinker.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cliplinker.model.ShortenedUrl;
import com.cliplinker.repository.ClickAnalyticsRepository;
import com.cliplinker.repository.ShortenedUrlRepository;

@Service
public class URLShortenerService {

	@Autowired
	private ShortenedUrlRepository urlRepo;
	
	@Autowired
	private ClickAnalyticsRepository analyticsRepo;

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static final String BASE_URL = "http://localhost:8080/";

	// Creating new short url

	@Transactional
	public String shortenURL(String originalUrl, String customAlias, LocalDateTime expiryTime) {

		// checking availability of custom alias

		if (customAlias != null && urlRepo.findByCustomAlias(customAlias).isPresent()) {
			throw new RuntimeException("Custom alias already taken!");
		}

		// Generate a unique short URL

		String shortUrl = (customAlias != null) ? customAlias : UUID.randomUUID().toString().substring(0, 6);

		// Create and save URL entity

		ShortenedUrl url = new ShortenedUrl();

		url.setOriginalUrl(originalUrl);
		url.setShortUrl(shortUrl);
		url.setCustomAlias(customAlias);
		url.setExpiryTime(expiryTime);

		urlRepo.save(url);

		// Store in Redis Cache

		redisTemplate.opsForValue().set(shortUrl, originalUrl,
				expiryTime != null ? expiryTime.getSecond() - LocalDateTime.now().getSecond() : 365 * 24 * 60 * 60,
				TimeUnit.SECONDS);

		return BASE_URL + shortUrl;

	}

	// Retrieves the original url from the database or cache

	public String getOriginalURL(String shortUrl) {

		// checking the redis chache first

		String chachedUrl = redisTemplate.opsForValue().get(shortUrl);

		if (chachedUrl != null)
			return chachedUrl;

		// If not available fetching from Database

		Optional<ShortenedUrl> urlData = urlRepo.findByShortUrl(shortUrl);

		if (urlData.isPresent()) {

			ShortenedUrl url = urlData.get();

			if (url.getExpiryTime() != null && url.getExpiryTime().isBefore(LocalDateTime.now())) {
				throw new RuntimeException("URL has expired");
			}

			// Cache in Redis for future use

			redisTemplate.opsForValue().set(shortUrl, url.getOriginalUrl(), 24, TimeUnit.HOURS);

			return url.getOriginalUrl();
		}

		throw new RuntimeException("Short URL not found");

	}
	
	// Delete a shortened url from the database and the cache

	@Transactional
	public void deleteShortURL(String shortUrl) {
	    urlRepo.findByShortUrl(shortUrl).ifPresent(url -> {
	        // First, delete related analytics records
	    	analyticsRepo.deleteByShortUrlId(url.getId());

	        // Then delete from the URL repository
	        urlRepo.delete(url);

	        // Finally, delete from Redis
	        redisTemplate.delete(shortUrl);
	    });
	}


}
