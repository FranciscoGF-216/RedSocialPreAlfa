package com.example.uaqychat.models;

public class User {

    private String id;
    private String email;
    private String username;
    private String phone;
    private String imageProfile;
    private String imageCover;
    private Long timesTamp;

    public User(){

    }

    public User(String id, String email, String username, String phone, Long timesTamp,String imageProfile,String imageCover) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.timesTamp = timesTamp;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(Long timesTamp) {
        this.timesTamp = timesTamp;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }
}
