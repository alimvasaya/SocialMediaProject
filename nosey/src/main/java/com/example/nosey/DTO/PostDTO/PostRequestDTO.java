package com.example.nosey.DTO.PostDTO;

import org.springframework.web.multipart.MultipartFile;

public class PostRequestDTO {
    private Long userId;
    private String title;
    private String content;
    private MultipartFile media;


    public PostRequestDTO(Long userId, String title, String content, MultipartFile media) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.media = media;
    }

    // Getters and Setters
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

    public MultipartFile getMedia() {
        return media;
    }

    public void setMedia(MultipartFile media) {
        this.media = media;
    }
}