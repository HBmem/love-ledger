package com.hbmem.LoveLedger.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class LoveInterest {
    private int id;
    private String nickname;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private List<String> likes;
    private List<String> dislikes;
    private String photoUrl;
    private int user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<String> getLikes() {
        return likes;
    }

    public boolean hasLike(String like) {
        if (likes == null) {
            return false;
        }
        return likes.contains(like);
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public boolean hasDislike(String dislike) {
        if (dislikes == null) {
            return false;
        }
        return dislikes.contains(dislike);
    }

    public void setDislikes(List<String> dislikes) {
        this.dislikes = dislikes;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
