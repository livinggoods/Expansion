package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 9/13/17.
 */

public class TrainingClass {
    int id, createdBy;
    String trainingId, className, country;
    Long clientTime;
    boolean archived;

    public TrainingClass() {}

    public TrainingClass(int id, int createdBy, String trainingId, String className,
                         String country, Long clientTime, boolean archived) {
        this.id = id;
        this.createdBy = createdBy;
        this.trainingId = trainingId;
        this.className = className;
        this.country = country;
        this.clientTime = clientTime;
        this.archived = archived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getClientTime() {
        return clientTime;
    }

    public void setClientTime(Long clientTime) {
        this.clientTime = clientTime;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
