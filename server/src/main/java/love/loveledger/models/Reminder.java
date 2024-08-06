package love.loveledger.models;

import java.time.LocalDate;

public class Reminder {
    private int reminderId;
    private String name;
    private String description;
    private LocalDate date;
    private int userId;
    private int relationshipId;
    private int notableDayId;

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        this.relationshipId = relationshipId;
    }

    public int getNotableDayId() {
        return notableDayId;
    }

    public void setNotableDayId(int notableDayId) {
        this.notableDayId = notableDayId;
    }
}
