package love.loveledger.models;

import java.time.LocalDate;

public class NotableDay {
    private int notableDayId;
    private String name;
    private String description;
    private int day;
    private int month;
    private int year;

    public int getNotableDayId() {
        return notableDayId;
    }

    public void setNotableDayId(int notableDayId) {
        this.notableDayId = notableDayId;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
