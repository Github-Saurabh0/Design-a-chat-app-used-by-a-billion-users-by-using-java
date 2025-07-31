package com.chatapp.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Notification model representing a notification to be sent to a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private UUID id;
    private UUID userId;
    private NotificationType type;
    private String title;
    private String body;
    private String imageUrl;
    private UUID referenceId; // ID of the related entity (message, conversation, etc.)
    private ReferenceType referenceType;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private LocalDateTime expiresAt;
    private Map<String, Object> data; // Additional data for the notification
    
    /**
     * Notification type enum representing the type of notification.
     */
    public enum NotificationType {
        MESSAGE,
        FRIEND_REQUEST,
        FRIEND_ACCEPTED,
        GROUP_INVITATION,
        GROUP_JOINED,
        MENTION,
        SYSTEM,
        CUSTOM
    }
    
    /**
     * Reference type enum representing the type of entity referenced by the notification.
     */
    public enum ReferenceType {
        MESSAGE,
        CONVERSATION,
        USER,
        SYSTEM,
        CUSTOM
    }
}