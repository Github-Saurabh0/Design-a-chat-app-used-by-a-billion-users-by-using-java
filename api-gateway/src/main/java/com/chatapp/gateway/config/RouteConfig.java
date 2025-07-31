package com.chatapp.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for API Gateway routes.
 * This class defines the routes for the API Gateway to route requests to the appropriate microservices.
 */
@Configuration
public class RouteConfig {

    /**
     * Configure routes for the API Gateway.
     *
     * @param builder the route locator builder
     * @return the route locator
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f
                                .rewritePath("/api/users/(?<segment>.*)", "/api/v1/users/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user-service")))
                        .uri("lb://user-service"))
                
                // Chat Service Routes
                .route("chat-service", r -> r.path("/api/chats/**")
                        .filters(f -> f
                                .rewritePath("/api/chats/(?<segment>.*)", "/api/v1/chats/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("chatServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/chat-service")))
                        .uri("lb://chat-service"))
                
                // Presence Service Routes
                .route("presence-service", r -> r.path("/api/presence/**")
                        .filters(f -> f
                                .rewritePath("/api/presence/(?<segment>.*)", "/api/v1/presence/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("presenceServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/presence-service")))
                        .uri("lb://presence-service"))
                
                // Notification Service Routes
                .route("notification-service", r -> r.path("/api/notifications/**")
                        .filters(f -> f
                                .rewritePath("/api/notifications/(?<segment>.*)", "/api/v1/notifications/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("notificationServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/notification-service")))
                        .uri("lb://notification-service"))
                
                // Media Service Routes
                .route("media-service", r -> r.path("/api/media/**")
                        .filters(f -> f
                                .rewritePath("/api/media/(?<segment>.*)", "/api/v1/media/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("mediaServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/media-service")))
                        .uri("lb://media-service"))
                
                // WebSocket Routes
                .route("websocket-route", r -> r.path("/ws/**")
                        .uri("lb://chat-service"))
                
                .build();
    }
}