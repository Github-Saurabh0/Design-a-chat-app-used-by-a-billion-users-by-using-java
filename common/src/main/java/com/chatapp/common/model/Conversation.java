package com.chatapp.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Conversation model representing a chat conversation.
 * This can be a one-to-one chat or a group chat.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    private UUID id;
    private ConversationType type;
    private String name; // For group chats
    private String description; // For group chats
    private String avatarUrl; // For group chats
    private Set<UUID> participants;
    private Map<UUID, ParticipantRole> participantRoles; // Maps user IDs to their roles
    private UUID createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID lastMessageId;
    private LocalDateTime lastActivityAt;
    private boolean encrypted;
    private Map<String, Object> metadata; // For extensibility
    
    /**
     * Conversation type enum representing the type of conversation.
     */
    public enum ConversationType {
        DIRECT,  // One-to-one chat
        GROUP,   // Group chat
        CHANNEL  // Broadcast channel
    }
    
    /**
     * Participant role enum representing the role of a participant in a conversation.
     */
    public enum ParticipantRole {
        OWNER,
        ADMIN,
        MODERATOR,
        MEMBER,
        GUEST
    }
}