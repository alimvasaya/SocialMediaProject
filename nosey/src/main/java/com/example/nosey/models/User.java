package com.example.nosey.models;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserProfile profile;


    public User() {}


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Constructor with id, email, and password
    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
    public void setProfile(UserProfile profile){
        this.profile = profile;
    }

    public UserProfile getProfile() {
        return profile;
    }
}