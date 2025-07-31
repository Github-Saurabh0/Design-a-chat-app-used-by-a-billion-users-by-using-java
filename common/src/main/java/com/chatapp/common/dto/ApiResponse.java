package com.chatapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response format for consistent responses across services.
 *
 * @param <T> the type of data in the response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    
    /**
     * Create a successful response with data.
     *
     * @param data the response data
     * @param message the success message
     * @param <T> the type of data
     * @return a successful ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * Create a successful response with data and a default message.
     *
     * @param data the response data
     * @param <T> the type of data
     * @return a successful ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }
    
    /**
     * Create a successful response with a message and no data.
     *
     * @param message the success message
     * @param <T> the type of data
     * @return a successful ApiResponse
     */
    public static <T> ApiResponse<T> success(String message) {
        return success(null, message);
    }
    
    /**
     * Create an error response.
     *
     * @param message the error message
     * @param errorCode the error code
     * @param <T> the type of data
     * @return an error ApiResponse
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
    
    /**
     * Create an error response with a default error code.
     *
     * @param message the error message
     * @param <T> the type of data
     * @return an error ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(message, "UNKNOWN_ERROR");
    }
}