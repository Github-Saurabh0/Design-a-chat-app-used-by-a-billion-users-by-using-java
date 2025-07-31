package com.chatapp.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Configuration for rate limiting in the API Gateway.
 * This class defines the rate limiting configuration to prevent abuse of the API.
 */
@Configuration
public class RateLimiterConfig {

    /**
     * Define a key resolver for rate limiting based on the request path.
     * This will limit requests based on the path, regardless of the user.
     *
     * @return the key resolver
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * Define a key resolver for rate limiting based on the user ID.
     * This will limit requests based on the user ID extracted from the JWT token.
     *
     * @return the key resolver
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
            if (userId == null) {
                return Mono.just("anonymous");
            }
            return Mono.just(userId);
        };
    }

    /**
     * Define a key resolver for rate limiting based on the client IP address.
     * This will limit requests based on the client IP address.
     *
     * @return the key resolver
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            return Mono.just(ip);
        };
    }

    /**
     * Define a Redis rate limiter for general API endpoints.
     * This will limit requests to 10 per second with a burst capacity of 20.
     *
     * @return the Redis rate limiter
     */
    @Bean
    public RedisRateLimiter generalApiRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    /**
     * Define a Redis rate limiter for authentication endpoints.
     * This will limit requests to 3 per second with a burst capacity of 5.
     *
     * @return the Redis rate limiter
     */
    @Bean
    public RedisRateLimiter authRateLimiter() {
        return new RedisRateLimiter(3, 5);
    }

    /**
     * Define a Redis rate limiter for media upload endpoints.
     * This will limit requests to 2 per second with a burst capacity of 5.
     *
     * @return the Redis rate limiter
     */
    @Bean
    public RedisRateLimiter mediaUploadRateLimiter() {
        return new RedisRateLimiter(2, 5);
    }
}