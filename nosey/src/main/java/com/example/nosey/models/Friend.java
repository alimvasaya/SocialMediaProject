package com.example.nosey.models;

import jakarta.persistence.*;

@Entity
public
class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User friend;

    // Constructors, Getters, and Setters
    public Friend() {}

    public Friend(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getFriend() { return friend; }
    public void setFriend(User friend) { this.friend = friend; }
}
