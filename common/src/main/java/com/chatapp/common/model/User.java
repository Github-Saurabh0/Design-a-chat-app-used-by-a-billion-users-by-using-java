package com.chatapp.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * User model representing a chat application user.
 * This is a shared model used across services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String email;
    private String displayName;
    private String profilePictureUrl;
    private UserStatus status;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<UUID> contacts;
    private Set<UUID> blockedUsers;
    private UserSettings settings;
    
    /**
     * User status enum representing the current state of a user.
     */
    public enum UserStatus {
        ONLINE,
        OFFLINE,
        AWAY,
        DO_NOT_DISTURB,
        INVISIBLE
    }
}