package com.cliplinker.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cliplinker.model.ClickAnalytics;
import com.cliplinker.model.ShortenedUrl;
import com.cliplinker.repository.ClickAnalyticsRepository;
import com.cliplinker.repository.ShortenedUrlRepository;

@Service
public class ClickAnalyticsService {

	@Autowired
	private ClickAnalyticsRepository analyticsRepo;
	
	@Autowired
	private ShortenedUrlRepository urlRepo;
	
	
	// logs a visit to the shortened url
	
	@Transactional
	public void trackClick(String shortUrl, String ipAddress) {
		
		Optional<ShortenedUrl> urlData = urlRepo.findByShortUrl(shortUrl);
		
		if (urlData.isPresent()) {
            ShortenedUrl url = urlData.get();
            url.setClickCount(url.getClickCount() + 1);
            urlRepo.save(url);

            ClickAnalytics analytics = new ClickAnalytics();
            analytics.setShortUrl(url);
            analytics.setIpAddress(ipAddress);
            analytics.setTimestamp(LocalDateTime.now());
            analyticsRepo.save(analytics);
        }
	}
}
