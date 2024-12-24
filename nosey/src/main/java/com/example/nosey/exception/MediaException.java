package com.example.nosey.exception;

public class MediaException {

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class MediaUploadException extends RuntimeException {
        public MediaUploadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
