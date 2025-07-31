package com.chatapp.common.exception;

import lombok.Getter;

/**
 * Base exception class for all application-specific exceptions.
 */
@Getter
public class ChatAppException extends RuntimeException {
    private final String errorCode;
    private final int statusCode;

    public ChatAppException(String message, String errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public ChatAppException(String message, String errorCode, int statusCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    /**
     * Resource not found exception.
     */
    public static class ResourceNotFoundException extends ChatAppException {
        public ResourceNotFoundException(String message) {
            super(message, "RESOURCE_NOT_FOUND", 404);
        }
    }

    /**
     * Unauthorized access exception.
     */
    public static class UnauthorizedException extends ChatAppException {
        public UnauthorizedException(String message) {
            super(message, "UNAUTHORIZED", 401);
        }
    }

    /**
     * Forbidden access exception.
     */
    public static class ForbiddenException extends ChatAppException {
        public ForbiddenException(String message) {
            super(message, "FORBIDDEN", 403);
        }
    }

    /**
     * Bad request exception for invalid input.
     */
    public static class BadRequestException extends ChatAppException {
        public BadRequestException(String message) {
            super(message, "BAD_REQUEST", 400);
        }
    }

    /**
     * Conflict exception for resource conflicts.
     */
    public static class ConflictException extends ChatAppException {
        public ConflictException(String message) {
            super(message, "CONFLICT", 409);
        }
    }

    /**
     * Service unavailable exception.
     */
    public static class ServiceUnavailableException extends ChatAppException {
        public ServiceUnavailableException(String message) {
            super(message, "SERVICE_UNAVAILABLE", 503);
        }
    }

    /**
     * Internal server error exception.
     */
    public static class InternalServerErrorException extends ChatAppException {
        public InternalServerErrorException(String message) {
            super(message, "INTERNAL_SERVER_ERROR", 500);
        }

        public InternalServerErrorException(String message, Throwable cause) {
            super(message, "INTERNAL_SERVER_ERROR", 500, cause);
        }
    }
}