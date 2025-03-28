package com.cliplinker.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cliplinker.model.ShortenedUrl;
import com.cliplinker.repository.ShortenedUrlRepository;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
	private final ShortenedUrlRepository urlRepository;

    public AnalyticsController(ShortenedUrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    /**
     * Get click count and other analytics
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getAnalytics(@PathVariable String shortUrl) {
        Optional<ShortenedUrl> urlData = urlRepository.findByShortUrl(shortUrl);

        if (urlData.isPresent()) {
            ShortenedUrl url = urlData.get();
            return ResponseEntity.ok(Map.of(
                "shortUrl", shortUrl,
                "originalUrl", url.getOriginalUrl() != null ? url.getOriginalUrl() : "N/A",
                "clickCount", url.getClickCount(),
                "expiryTime", url.getExpiryTime() != null ? url.getExpiryTime().toString() : "No expiry"
            ));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Short URL not found"));
    }	
}
