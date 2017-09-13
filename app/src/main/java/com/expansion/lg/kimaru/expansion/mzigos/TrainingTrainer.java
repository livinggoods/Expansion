package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 9/13/17.
 */

public class TrainingTrainer {
    Integer id, trainerId, createdby, trainingRoleId;
    String trainingId, classId, country;
    Long clientTime;
    boolean archived;

    public TrainingTrainer() {}

    public TrainingTrainer(Integer id, Integer trainerId, Integer createdby,
                           Integer trainingRoleId, String trainingId, String classId,
                           String country, Long clientTime, boolean archived) {
        this.id = id;
        this.trainerId = trainerId;
        this.createdby = createdby;
        this.trainingRoleId = trainingRoleId;
        this.trainingId = trainingId;
        this.classId = classId;
        this.country = country;
        this.clientTime = clientTime;
        this.archived = archived;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public Integer getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Integer createdby) {
        this.createdby = createdby;
    }

    public Integer getTrainingRoleId() {
        return trainingRoleId;
    }

    public void setTrainingRoleId(Integer trainingRoleId) {
        this.trainingRoleId = trainingRoleId;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
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
