package com.chatapp.user.entity;

import com.chatapp.common.model.User.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * User entity representing a user in the database.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(name = "user_contacts", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "contact_id")
    private Set<UUID> contacts = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_blocked", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "blocked_user_id")
    private Set<UUID> blockedUsers = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "settings_id")
    private UserSettings settings;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    /**
     * Convert the entity to a DTO.
     *
     * @return the user DTO
     */
    public com.chatapp.common.model.User toDto() {
        return com.chatapp.common.model.User.builder()
                .id(id)
                .username(username)
                .email(email)
                .displayName(displayName)
                .profilePictureUrl(profilePictureUrl)
                .status(status)
                .lastSeen(lastSeen)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .contacts(contacts)
                .blockedUsers(blockedUsers)
                .settings(settings != null ? settings.toDto() : null)
                .build();
    }
}