package com.hbmem.LoveLedger.model;

import java.time.LocalDate;

public class Relationship {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private RelationshipStatus relationshipStatus;
    private int importanceLevel;
    private boolean official;
    private int UserId;
    private int loveInterestId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public RelationshipStatus getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(RelationshipStatus relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public int getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(int importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getLoveInterestId() {
        return loveInterestId;
    }

    public void setLoveInterestId(int loveInterestId) {
        this.loveInterestId = loveInterestId;
    }
}
