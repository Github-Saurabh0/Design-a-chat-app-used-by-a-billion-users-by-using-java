package com.chatapp.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Message model representing a chat message.
 * This is a shared model used across services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private MessageType type;
    private String content;
    private List<Attachment> attachments;
    private Map<UUID, MessageStatus> statusByUser; // Maps user IDs to their read status
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
    private LocalDateTime updatedAt;
    private boolean edited;
    private boolean deleted;
    private UUID replyToMessageId; // For threaded replies
    private Map<String, Object> metadata; // For extensibility
    
    /**
     * Message type enum representing the type of message.
     */
    public enum MessageType {
        TEXT,
        IMAGE,
        VIDEO,
        AUDIO,
        FILE,
        LOCATION,
        CONTACT,
        SYSTEM, // For system messages like "User X joined the group"
        CUSTOM  // For custom message types
    }
    
    /**
     * Message status enum representing the current state of a message.
     */
    public enum MessageStatus {
        SENDING,
        SENT,
        DELIVERED,
        READ,
        FAILED
    }
    
    /**
     * Attachment model representing a file attached to a message.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {
        private UUID id;
        private String name;
        private String url;
        private String contentType;
        private long size;
        private String thumbnailUrl; // For images and videos
        private int width;  // For images and videos
        private int height; // For images and videos
        private int duration; // For audio and video in seconds
    }
}