package com.example.nosey.DTO.FriendsDTO;

public class FriendDTO {
    private Long friendId;
    private String friendName;

    // Constructor
    public FriendDTO(Long friendId, Long friendName) {
        this.friendId = friendId;
        this.friendName = String.valueOf(friendName);
    }

    // Getters and Setters
    public Long getFriendId() { return friendId; }
    public void setFriendId(Long friendId) { this.friendId = friendId; }

    public String getFriendName() { return friendName; }
    public void setFriendName(String friendName) { this.friendName = friendName; }
}
