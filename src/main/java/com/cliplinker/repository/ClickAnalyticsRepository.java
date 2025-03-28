package com.cliplinker.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cliplinker.model.ClickAnalytics;

@Repository
public interface ClickAnalyticsRepository extends JpaRepository<ClickAnalytics, UUID> {
	@Modifying
	@Query("DELETE FROM ClickAnalytics c WHERE c.shortUrl.id = :shortUrlId")
	void deleteByShortUrlId(@Param("shortUrlId") UUID shortUrlId);


}
