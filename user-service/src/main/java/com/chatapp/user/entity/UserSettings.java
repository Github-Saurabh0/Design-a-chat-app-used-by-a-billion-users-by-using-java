package com.chatapp.user.entity;

import com.chatapp.common.model.UserSettings.NotificationLevel;
import com.chatapp.common.model.UserSettings.PrivacyLevel;
import com.chatapp.common.model.UserSettings.ThemePreference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * User settings entity representing user preferences in the database.
 */
@Entity
@Table(name = "user_settings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "enable_read_receipts", nullable = false)
    private boolean enableReadReceipts = true;

    @Column(name = "enable_typing_indicators", nullable = false)
    private boolean enableTypingIndicators = true;

    @Column(name = "enable_push_notifications", nullable = false)
    private boolean enablePushNotifications = true;

    @Column(name = "enable_email_notifications", nullable = false)
    private boolean enableEmailNotifications = true;

    @Column(name = "enable_sound_notifications", nullable = false)
    private boolean enableSoundNotifications = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_level", nullable = false)
    private NotificationLevel notificationLevel = NotificationLevel.ALL;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_level", nullable = false)
    private PrivacyLevel privacyLevel = PrivacyLevel.CONTACTS_ONLY;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_preference", nullable = false)
    private ThemePreference themePreference = ThemePreference.SYSTEM;

    @Column(nullable = false)
    private String language = "en";

    /**
     * Convert the entity to a DTO.
     *
     * @return the user settings DTO
     */
    public com.chatapp.common.model.UserSettings toDto() {
        return com.chatapp.common.model.UserSettings.builder()
                .enableReadReceipts(enableReadReceipts)
                .enableTypingIndicators(enableTypingIndicators)
                .enablePushNotifications(enablePushNotifications)
                .enableEmailNotifications(enableEmailNotifications)
                .enableSoundNotifications(enableSoundNotifications)
                .notificationLevel(notificationLevel)
                .privacyLevel(privacyLevel)
                .themePreference(themePreference)
                .language(language)
                .build();
    }
}