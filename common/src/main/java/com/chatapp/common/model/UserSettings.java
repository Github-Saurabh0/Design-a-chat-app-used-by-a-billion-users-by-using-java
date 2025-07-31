package com.chatapp.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User settings model containing user preferences.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    private boolean enableReadReceipts;
    private boolean enableTypingIndicators;
    private boolean enablePushNotifications;
    private boolean enableEmailNotifications;
    private boolean enableSoundNotifications;
    private NotificationLevel notificationLevel;
    private PrivacyLevel privacyLevel;
    private ThemePreference themePreference;
    private String language;
    
    /**
     * Notification level enum representing how aggressively to notify the user.
     */
    public enum NotificationLevel {
        ALL,
        MENTIONS_ONLY,
        NONE
    }
    
    /**
     * Privacy level enum representing who can see user's information.
     */
    public enum PrivacyLevel {
        PUBLIC,        // Anyone can see profile and status
        CONTACTS_ONLY, // Only contacts can see profile and status
        PRIVATE        // Nobody can see profile or status
    }
    
    /**
     * Theme preference enum representing the user's preferred UI theme.
     */
    public enum ThemePreference {
        LIGHT,
        DARK,
        SYSTEM
    }
}