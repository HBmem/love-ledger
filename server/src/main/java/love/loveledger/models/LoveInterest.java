package love.loveledger.models;

import java.time.LocalDate;

public class LoveInterest {
    private int loveInterestId;
    private String nickname;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private String[] hobbies;
    private String[] likes;
    private String[] dislikes;
    private int userId;

    public int getLoveInterestId() {
        return loveInterestId;
    }

    public void setLoveInterestId(int loveInterestId) {
        this.loveInterestId = loveInterestId;
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

    public String[] getHobbies() {
        return hobbies;
    }

    public String getHobbiesString() {
        return String.join(";", hobbies);
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies.split(";");
    }

    public String[] getLikes() {
        return likes;
    }

    public String getLikesString() {
        return String.join(";", likes);
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public void setLikes(String likes) {
        this.likes = likes.split(";");
    }

    public String[] getDislikes() {
        return dislikes;
    }

    public String getDislikesString() {
        return String.join(";", dislikes);
    }

    public void setDislikes(String[] dislikes) {
        this.dislikes = dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes.split(";");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
