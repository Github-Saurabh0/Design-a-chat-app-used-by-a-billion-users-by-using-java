package com.chatapp.user.service;

import com.chatapp.common.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing user-related events to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String USER_EVENTS_TOPIC = "user-events";
    private static final String USER_STATUS_TOPIC = "user-status";

    /**
     * Publish user registered event.
     *
     * @param user the registered user
     */
    public void publishUserRegistered(User user) {
        try {
            UserEvent event = UserEvent.builder()
                    .eventType("USER_REGISTERED")
                    .userId(user.getId())
                    .user(user)
                    .timestamp(System.currentTimeMillis())
                    .build();

            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(USER_EVENTS_TOPIC, user.getId().toString(), eventJson);
            
            log.info("Published USER_REGISTERED event for user: {}", user.getId());
        } catch (JsonProcessingException e) {
            log.error("Failed to publish USER_REGISTERED event for user: {}", user.getId(), e);
        }
    }

    /**
     * Publish user updated event.
     *
     * @param user the updated user
     */
    public void publishUserUpdated(User user) {
        try {
            UserEvent event = UserEvent.builder()
                    .eventType("USER_UPDATED")
                    .userId(user.getId())
                    .user(user)
                    .timestamp(System.currentTimeMillis())
                    .build();

            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(USER_EVENTS_TOPIC, user.getId().toString(), eventJson);
            
            log.info("Published USER_UPDATED event for user: {}", user.getId());
        } catch (JsonProcessingException e) {
            log.error("Failed to publish USER_UPDATED event for user: {}", user.getId(), e);
        }
    }

    /**
     * Publish user status updated event.
     *
     * @param user the user with updated status
     */
    public void publishUserStatusUpdated(User user) {
        try {
            UserStatusEvent event = UserStatusEvent.builder()
                    .userId(user.getId())
                    .status(user.getStatus())
                    .lastSeen(user.getLastSeen())
                    .timestamp(System.currentTimeMillis())
                    .build();

            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(USER_STATUS_TOPIC, user.getId().toString(), eventJson);
            
            log.info("Published USER_STATUS_UPDATED event for user: {} with status: {}", user.getId(), user.getStatus());
        } catch (JsonProcessingException e) {
            log.error("Failed to publish USER_STATUS_UPDATED event for user: {}", user.getId(), e);
        }
    }

    /**
     * Publish user deleted event.
     *
     * @param user the deleted user
     */
    public void publishUserDeleted(User user) {
        try {
            UserEvent event = UserEvent.builder()
                    .eventType("USER_DELETED")
                    .userId(user.getId())
                    .user(user)
                    .timestamp(System.currentTimeMillis())
                    .build();

            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(USER_EVENTS_TOPIC, user.getId().toString(), eventJson);
            
            log.info("Published USER_DELETED event for user: {}", user.getId());
        } catch (JsonProcessingException e) {
            log.error("Failed to publish USER_DELETED event for user: {}", user.getId(), e);
        }
    }

    /**
     * User event model.
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UserEvent {
        private String eventType;
        private java.util.UUID userId;
        private User user;
        private long timestamp;
    }

    /**
     * User status event model.
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UserStatusEvent {
        private java.util.UUID userId;
        private User.UserStatus status;
        private java.time.LocalDateTime lastSeen;
        private long timestamp;
    }
}