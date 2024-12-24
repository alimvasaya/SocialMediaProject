package com.example.nosey.models;

import jakarta.persistence.*;


@Entity
@Table(name = "followers")
public class Follow {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "userId")
    private User follower;  // The user who is following

    @ManyToOne
    @JoinColumn(name = "following_id", referencedColumnName = "userId")
    private User following;  // The user being followed

    // Constructors, Getters, and Setters
    public Follow(Long id, User follower, User following) {
        this.id = id;
        this.follower = follower;
        this.following = following;
    }

    public Follow() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }
}