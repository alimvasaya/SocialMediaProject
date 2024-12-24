package com.example.nosey.models;

import jakarta.persistence.*;

@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    // Constructors, Getters, and Setters
    public FriendRequest() {}

    public FriendRequest(User sender, User receiver, FriendRequestStatus status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public FriendRequestStatus getStatus() { return status; }
    public void setStatus(FriendRequestStatus status) { this.status = status; }
}
