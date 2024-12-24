package com.example.nosey.DTO.PostDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;

public class PostResponseDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String title;
    private String content;
    private String mediaUrl;
    private LocalDateTime createdAt;

    public PostResponseDTO(Long id, Long userId, String firstName, String lastName, String title, String content, String mediaUrl, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.createdAt = createdAt;
    }



    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}