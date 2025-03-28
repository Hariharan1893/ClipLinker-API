package com.cliplinker.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cliplinker.model.ShortenedUrl;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, UUID> {

	Optional<ShortenedUrl> findByShortUrl(String shortUrl);

	Optional<ShortenedUrl> findByCustomAlias(String customAlias);
}
