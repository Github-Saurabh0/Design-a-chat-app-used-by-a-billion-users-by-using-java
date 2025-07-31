package com.chatapp.user.service;

import com.chatapp.common.exception.ChatAppException;
import com.chatapp.common.model.User;
import com.chatapp.common.response.PageResponse;
import com.chatapp.user.dto.*;
import com.chatapp.user.entity.UserSettings;
import com.chatapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for user management operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;

    /**
     * Register a new user.
     *
     * @param request the registration request
     * @return the created user
     */
    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        log.info("Registering new user with username: {}", request.getUsername());

        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ChatAppException.ConflictException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ChatAppException.ConflictException("Email already exists");
        }

        // Create default user settings
        UserSettings defaultSettings = UserSettings.builder()
                .enableReadReceipts(true)
                .enableTypingIndicators(true)
                .enablePushNotifications(true)
                .enableEmailNotifications(true)
                .enableSoundNotifications(true)
                .build();

        // Create user entity
        com.chatapp.user.entity.User user = com.chatapp.user.entity.User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .status(com.chatapp.common.model.User.UserStatus.ONLINE)
                .settings(defaultSettings)
                .build();

        user.getRoles().add("USER");
        user = userRepository.save(user);

        // Publish user registration event
        userEventPublisher.publishUserRegistered(user.toDto());

        log.info("User registered successfully with ID: {}", user.getId());
        return user.toDto();
    }

    /**
     * Get user by ID.
     *
     * @param userId the user ID
     * @return the user
     */
    @Cacheable(value = "users", key = "#userId")
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(com.chatapp.user.entity.User::toDto)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));
    }

    /**
     * Get user by username.
     *
     * @param username the username
     * @return the user
     */
    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(com.chatapp.user.entity.User::toDto)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));
    }

    /**
     * Get user by email.
     *
     * @param email the email
     * @return the user
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(com.chatapp.user.entity.User::toDto)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));
    }

    /**
     * Update user profile.
     *
     * @param userId the user ID
     * @param request the update request
     * @return the updated user
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public User updateUser(UUID userId, UserUpdateRequest request) {
        log.info("Updating user profile for ID: {}", userId);

        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));

        // Update fields if provided
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ChatAppException.ConflictException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (request.getNewPassword() != null) {
            if (request.getCurrentPassword() == null || 
                !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new ChatAppException.BadRequestException("Current password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        user = userRepository.save(user);

        // Publish user updated event
        userEventPublisher.publishUserUpdated(user.toDto());

        log.info("User profile updated successfully for ID: {}", userId);
        return user.toDto();
    }

    /**
     * Update user settings.
     *
     * @param userId the user ID
     * @param request the settings update request
     * @return the updated user
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public User updateUserSettings(UUID userId, UserSettingsUpdateRequest request) {
        log.info("Updating user settings for ID: {}", userId);

        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));

        UserSettings settings = user.getSettings();
        if (settings == null) {
            settings = new UserSettings();
            user.setSettings(settings);
        }

        // Update settings if provided
        if (request.getEnableReadReceipts() != null) {
            settings.setEnableReadReceipts(request.getEnableReadReceipts());
        }
        if (request.getEnableTypingIndicators() != null) {
            settings.setEnableTypingIndicators(request.getEnableTypingIndicators());
        }
        if (request.getEnablePushNotifications() != null) {
            settings.setEnablePushNotifications(request.getEnablePushNotifications());
        }
        if (request.getEnableEmailNotifications() != null) {
            settings.setEnableEmailNotifications(request.getEnableEmailNotifications());
        }
        if (request.getEnableSoundNotifications() != null) {
            settings.setEnableSoundNotifications(request.getEnableSoundNotifications());
        }
        if (request.getNotificationLevel() != null) {
            settings.setNotificationLevel(request.getNotificationLevel());
        }
        if (request.getPrivacyLevel() != null) {
            settings.setPrivacyLevel(request.getPrivacyLevel());
        }
        if (request.getThemePreference() != null) {
            settings.setThemePreference(request.getThemePreference());
        }
        if (request.getLanguage() != null) {
            settings.setLanguage(request.getLanguage());
        }

        user = userRepository.save(user);

        log.info("User settings updated successfully for ID: {}", userId);
        return user.toDto();
    }

    /**
     * Search users.
     *
     * @param query the search query
     * @param currentUserId the current user ID
     * @param pageable the pagination information
     * @return the page of users
     */
    public PageResponse<User> searchUsers(String query, UUID currentUserId, Pageable pageable) {
        com.chatapp.user.entity.User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));

        Page<com.chatapp.user.entity.User> userPage;
        if (currentUser.getBlockedUsers().isEmpty()) {
            userPage = userRepository.searchUsers(query, pageable);
        } else {
            userPage = userRepository.searchUsersExcludingBlocked(query, currentUser.getBlockedUsers(), pageable);
        }

        return PageResponse.from(userPage.map(com.chatapp.user.entity.User::toDto));
    }

    /**
     * Get user contacts.
     *
     * @param userId the user ID
     * @param pageable the pagination information
     * @return the page of contacts
     */
    public PageResponse<User> getUserContacts(UUID userId, Pageable pageable) {
        Page<com.chatapp.user.entity.User> contactsPage = userRepository.findContactsByUserId(userId, pageable);
        return PageResponse.from(contactsPage.map(com.chatapp.user.entity.User::toDto));
    }

    /**
     * Add contact.
     *
     * @param userId the user ID
     * @param contactId the contact ID
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void addContact(UUID userId, UUID contactId) {
        if (userId.equals(contactId)) {
            throw new ChatAppException.BadRequestException("Cannot add yourself as contact");
        }

        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));
        
        if (!userRepository.existsById(contactId)) {
            throw new ChatAppException.ResourceNotFoundException("Contact user not found");
        }

        user.getContacts().add(contactId);
        userRepository.save(user);

        log.info("Contact {} added for user {}", contactId, userId);
    }

    /**
     * Remove contact.
     *
     * @param userId the user ID
     * @param contactId the contact ID
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void removeContact(UUID userId, UUID contactId) {
        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));

        user.getContacts().remove(contactId);
        userRepository.save(user);

        log.info("Contact {} removed for user {}", contactId, userId);
    }

    /**
     * Block user.
     *
     * @param userId the user ID
     * @param blockedUserId the blocked user ID
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void blockUser(UUID userId, UUID blockedUserId) {
        if (userId.equals(blockedUserId)) {
            throw new ChatAppException.BadRequestException("Cannot block yourself");
        }

        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));
        
        if (!userRepository.existsById(blockedUserId)) {
            throw new ChatAppException.ResourceNotFoundException("User to block not found");
        }

        user.getBlockedUsers().add(blockedUserId);
        user.getContacts().remove(blockedUserId); // Remove from contacts if exists
        userRepository.save(user);

        log.info("User {} blocked by user {}", blockedUserId, userId);
    }

    /**
     * Unblock user.
     *
     * @param userId the user ID
     * @param blockedUserId the blocked user ID
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void unblockUser(UUID userId, UUID blockedUserId) {
        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));

        user.getBlockedUsers().remove(blockedUserId);
        userRepository.save(user);

        log.info("User {} unblocked by user {}", blockedUserId, userId);
    }

    /**
     * Update user status.
     *
     * @param userId the user ID
     * @param status the new status
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void updateUserStatus(UUID userId, com.chatapp.common.model.User.UserStatus status) {
        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));

        user.setStatus(status);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);

        // Publish status update event
        userEventPublisher.publishUserStatusUpdated(user.toDto());

        log.info("User status updated to {} for ID: {}", status, userId);
    }

    /**
     * Get users by IDs.
     *
     * @param userIds the user IDs
     * @return the list of users
     */
    public List<User> getUsersByIds(Set<UUID> userIds) {
        return userRepository.findByIdIn(userIds)
                .stream()
                .map(com.chatapp.user.entity.User::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete user account.
     *
     * @param userId the user ID
     */
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(UUID userId) {
        com.chatapp.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatAppException.ResourceNotFoundException("User not found"));

        // Publish user deletion event before deleting
        userEventPublisher.publishUserDeleted(user.toDto());

        userRepository.delete(user);
        log.info("User account deleted for ID: {}", userId);
    }
}