package com.chatapp.user.repository;

import com.chatapp.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Repository for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by username.
     *
     * @param username the username
     * @return the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     *
     * @param email the email
     * @return the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a username exists.
     *
     * @param username the username
     * @return true if exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email exists.
     *
     * @param email the email
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Find users by their IDs.
     *
     * @param ids the user IDs
     * @return the list of users
     */
    List<User> findByIdIn(Set<UUID> ids);

    /**
     * Search users by username or display name.
     *
     * @param query the search query
     * @param pageable the pagination information
     * @return the page of users
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:query% OR u.displayName LIKE %:query%")
    Page<User> searchUsers(@Param("query") String query, Pageable pageable);

    /**
     * Find users by username or display name, excluding blocked users.
     *
     * @param query the search query
     * @param blockedUserIds the IDs of blocked users
     * @param pageable the pagination information
     * @return the page of users
     */
    @Query("SELECT u FROM User u WHERE (u.username LIKE %:query% OR u.displayName LIKE %:query%) AND u.id NOT IN :blockedUserIds")
    Page<User> searchUsersExcludingBlocked(
            @Param("query") String query,
            @Param("blockedUserIds") Set<UUID> blockedUserIds,
            Pageable pageable);

    /**
     * Find contacts of a user.
     *
     * @param userId the user ID
     * @param pageable the pagination information
     * @return the page of users
     */
    @Query("SELECT u FROM User u JOIN u.contacts c WHERE c = :userId")
    Page<User> findContactsByUserId(@Param("userId") UUID userId, Pageable pageable);
}