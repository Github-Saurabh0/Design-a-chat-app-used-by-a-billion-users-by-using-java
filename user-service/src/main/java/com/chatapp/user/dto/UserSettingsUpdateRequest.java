package com.chatapp.user.dto;

import com.chatapp.common.model.UserSettings.NotificationLevel;
import com.chatapp.common.model.UserSettings.PrivacyLevel;
import com.chatapp.common.model.UserSettings.ThemePreference;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user settings update requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsUpdateRequest {

    private Boolean enableReadReceipts;

    private Boolean enableTypingIndicators;

    private Boolean enablePushNotifications;

    private Boolean enableEmailNotifications;

    private Boolean enableSoundNotifications;

    private NotificationLevel notificationLevel;

    private PrivacyLevel privacyLevel;

    private ThemePreference themePreference;

    @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "Language must be in format 'en' or 'en-US'")
    private String language;
}