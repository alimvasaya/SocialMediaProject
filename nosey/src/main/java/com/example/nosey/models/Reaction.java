package com.example.nosey.models;
import jakarta.persistence.*;



@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;  // Like, Love, Haha, etc.

    // Constructors
    public Reaction() {}

    public Reaction(Post post, User user, ReactionType reactionType) {
        this.post = post;
        this.user = user;

        this.reactionType = reactionType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}