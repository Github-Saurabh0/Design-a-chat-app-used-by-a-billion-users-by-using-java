package com.chatapp.gateway.controller;

import com.chatapp.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fallback controller for circuit breaker fallbacks.
 * This controller provides fallback responses when a service is unavailable.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Fallback for the user service.
     *
     * @return a fallback response
     */
    @GetMapping("/user-service")
    public ResponseEntity<ApiResponse<Void>> userServiceFallback() {
        ApiResponse<Void> response = ApiResponse.error(
                "User service is currently unavailable. Please try again later.",
                "SERVICE_UNAVAILABLE"
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Fallback for the chat service.
     *
     * @return a fallback response
     */
    @GetMapping("/chat-service")
    public ResponseEntity<ApiResponse<Void>> chatServiceFallback() {
        ApiResponse<Void> response = ApiResponse.error(
                "Chat service is currently unavailable. Please try again later.",
                "SERVICE_UNAVAILABLE"
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Fallback for the presence service.
     *
     * @return a fallback response
     */
    @GetMapping("/presence-service")
    public ResponseEntity<ApiResponse<Void>> presenceServiceFallback() {
        ApiResponse<Void> response = ApiResponse.error(
                "Presence service is currently unavailable. Please try again later.",
                "SERVICE_UNAVAILABLE"
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Fallback for the notification service.
     *
     * @return a fallback response
     */
    @GetMapping("/notification-service")
    public ResponseEntity<ApiResponse<Void>> notificationServiceFallback() {
        ApiResponse<Void> response = ApiResponse.error(
                "Notification service is currently unavailable. Please try again later.",
                "SERVICE_UNAVAILABLE"
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Fallback for the media service.
     *
     * @return a fallback response
     */
    @GetMapping("/media-service")
    public ResponseEntity<ApiResponse<Void>> mediaServiceFallback() {
        ApiResponse<Void> response = ApiResponse.error(
                "Media service is currently unavailable. Please try again later.",
                "SERVICE_UNAVAILABLE"
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}