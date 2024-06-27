package love.loveledger.models;

import java.time.LocalDate;

public class Relationship {
    private int relationshipId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean official;
    private String[] labels;
    private int userId;
    private int loveInterestId;

    public int getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        this.relationshipId = relationshipId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public String[] getLabels() {
        return labels;
    }

    public String getLabelsString() {
        return String.join(";", labels);
    }

    public void setLabels(String labels) {
        this.labels = labels.split(";");
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLoveInterestId() {
        return loveInterestId;
    }

    public void setLoveInterestId(int loveInterestId) {
        this.loveInterestId = loveInterestId;
    }
}
