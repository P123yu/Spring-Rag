package com.example.spring_rag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    // 1. Handle Specific Spring AI / OpenAI Errors
//    @ExceptionHandler(OpenAiHttpException.class)
//    public ResponseEntity<String> handleOpenAiException(OpenAiHttpException ex) {
//        // Check for 429 (Too Many Requests)
//        if (ex.getStatusCode().value() == 429) {
//            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
//                    .body("Server is currently busy (Rate Limit Exceeded). Please try again after 30 seconds.");
//        }
//
//        // Handle other OpenAI errors (401, 500, etc.)
//        return ResponseEntity.status(ex.getStatusCode().value())
//                .body("AI Provider Error: " + ex.getMessage());
//    }

    // 2. Handle Generic Web Client Errors (Fallback)
    @ExceptionHandler(WebClientResponseException.TooManyRequests.class)
    public ResponseEntity<String> handleRateLimitException(WebClientResponseException.TooManyRequests ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Server is currently busy. Please try again shortly.");
    }

    // 3. General Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        // Log the error here in a real app
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}