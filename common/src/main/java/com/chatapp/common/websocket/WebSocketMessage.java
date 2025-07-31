package com.chatapp.common.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * WebSocket message model for real-time communication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private UUID id;
    private MessageType type;
    private String channel;
    private UUID senderId;
    private Object payload;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
    
    /**
     * WebSocket message type enum.
     */
    public enum MessageType {
        // Connection events
        CONNECT,
        DISCONNECT,
        HEARTBEAT,
        ERROR,
        
        // Chat events
        MESSAGE,
        MESSAGE_DELIVERED,
        MESSAGE_READ,
        MESSAGE_DELETED,
        MESSAGE_EDITED,
        
        // Typing events
        TYPING_START,
        TYPING_STOP,
        
        // Presence events
        PRESENCE_UPDATE,
        
        // Conversation events
        CONVERSATION_CREATED,
        CONVERSATION_UPDATED,
        CONVERSATION_DELETED,
        
        // User events
        USER_JOINED,
        USER_LEFT,
        USER_UPDATED,
        
        // Custom events
        CUSTOM
    }
}