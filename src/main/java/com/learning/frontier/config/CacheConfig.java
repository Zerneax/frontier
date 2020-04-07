package com.learning.frontier.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.asList(
                new CaffeineCache("account", caffeineCacheBuilder(100, 1000, 10).build()),
                new CaffeineCache("token", caffeineCacheBuilder(100, 1000, 5).build()),
                new CaffeineCache("autorisation", caffeineCacheBuilder(100, 1000, 10).build())
        ));

        return simpleCacheManager;
    }


    Caffeine< Object, Object > caffeineCacheBuilder(int capacity, int maxSize, int duration) {
        return Caffeine.newBuilder()
                .initialCapacity(capacity)
                .maximumSize(maxSize)
                .expireAfterAccess(duration, TimeUnit.MINUTES)
                .weakKeys()
                .recordStats();
    }
}
