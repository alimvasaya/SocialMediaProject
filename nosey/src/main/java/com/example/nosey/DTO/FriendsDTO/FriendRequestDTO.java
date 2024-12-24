package com.example.nosey.DTO.FriendsDTO;

import com.example.nosey.models.FriendRequestStatus;

public class FriendRequestDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private FriendRequestStatus status;

    public FriendRequestDTO(Long id, Long senderId, String senderName, FriendRequestStatus status) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.status = status;
    }
    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }
}
