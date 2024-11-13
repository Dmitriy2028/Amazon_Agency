package com.amazon.test_task.cache.config;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

// Configuration class for Redis caching, enabling caching support and setting up Redis cache manager properties.
@Configuration
@EnableCaching
@AllArgsConstructor
public class RedisCacheConfig {

    private final StringRedisTemplate redisTemplate; // Template to interact with Redis as a key-value store.

    // Configures a CacheManager bean with Redis-specific caching properties.
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // Sets the cache entry expiration to 5 minutes.
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json())); // Configures JSON serialization for cache values.

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig) // Applies default cache settings.
                .build(); // Builds and returns the RedisCacheManager instance.
    }

    // Checks the connection to Redis when the application context is fully initialized.
    @EventListener(ContextRefreshedEvent.class)
    public void checkRedisConnection() {
        try {
            redisTemplate.getConnectionFactory().getConnection().ping(); // Attempts to ping Redis to confirm connection.
            System.out.println("*********************** Successfully connected to Redis! ***********************");
        } catch (Exception e) {
            System.err.println("**************************************************** Failed to connect to Redis: " + e.getMessage());
        }
    }
}
