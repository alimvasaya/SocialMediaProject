package com.example.nosey.DTO.CommentDTO;

public class CommentRequestDTO {
    private Long postId;
    private String content;

    // Getters and Setters

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}