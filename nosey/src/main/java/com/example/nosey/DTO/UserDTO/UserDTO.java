package com.example.nosey.DTO.UserDTO;

public class UserDTO {
    private Long userid;
    private String firstName;
    private String lastName;

    public UserDTO(Long userid, String firstName, String lastName) {
        this.userid = userid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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
