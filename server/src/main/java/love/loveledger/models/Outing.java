package love.loveledger.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Outing {
    private int outingId;
    private String name;
    private OutingType outingType;
    private String description;
    private String location;
    private String outcome;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int relationshipId;

    public int getOutingId() {
        return outingId;
    }

    public void setOutingId(int outingId) {
        this.outingId = outingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OutingType getOutingType() {
        return outingType;
    }

    public void setOutingType(OutingType outingType) {
        this.outingType = outingType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        this.relationshipId = relationshipId;
    }
}
