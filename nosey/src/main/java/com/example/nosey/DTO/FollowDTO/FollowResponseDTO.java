package com.example.nosey.DTO.FollowDTO;

public class FollowResponseDTO {
    private String message;

    public FollowResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}