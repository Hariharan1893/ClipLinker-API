package com.cliplinker.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimitingService {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private static final int MAX_REQUESTS = 10;
	private static final int TIME_WINDOW = 60;
	
	public boolean isAllowed(String ipAddress) {
		
		String key = "rate_limit:" + ipAddress;
		
		String requests = redisTemplate.opsForValue().get(key);

        if (requests == null) {
            redisTemplate.opsForValue().set(key, "1", TIME_WINDOW, TimeUnit.SECONDS);
            return true;
        } else if (Integer.parseInt(requests) < MAX_REQUESTS) {
            redisTemplate.opsForValue().increment(key);
            return true;
        } else {
            return false;
        }

	}
}
