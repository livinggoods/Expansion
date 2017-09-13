package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 9/13/17.
 */

public class TrainingTrainee {

    String id, registrationId, trainingId, country, chpCode;
    Integer classId, addedBy, branch, cohort;
    Long clientTime;

    public TrainingTrainee() {}

    public TrainingTrainee(String id, String registrationId, String trainingId, String country,
                           String chpCode, Integer classId, Integer addedBy, Integer branch,
                           Integer cohort, Long clientTime) {
        this.id = id;
        this.registrationId = registrationId;
        this.trainingId = trainingId;
        this.country = country;
        this.chpCode = chpCode;
        this.classId = classId;
        this.addedBy = addedBy;
        this.branch = branch;
        this.cohort = cohort;
        this.clientTime = clientTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getChpCode() {
        return chpCode;
    }

    public void setChpCode(String chpCode) {
        this.chpCode = chpCode;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public Integer getCohort() {
        return cohort;
    }

    public void setCohort(Integer cohort) {
        this.cohort = cohort;
    }

    public Long getClientTime() {
        return clientTime;
    }

    public void setClientTime(Long clientTime) {
        this.clientTime = clientTime;
    }
}
