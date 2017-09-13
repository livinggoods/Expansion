package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 9/13/17.
 */

public class TrainingRole {
    Integer id;
    String roleName;
    boolean archived;
    boolean readOnly;
    String country;
    Long clientTime;
    Integer createdBy;

    public TrainingRole() {}

    public TrainingRole(Integer id, String roleName, boolean archived, boolean readOnly,
                        String country, Long clientTime, Integer createdBy) {
        this.id = id;
        this.roleName = roleName;
        this.archived = archived;
        this.readOnly = readOnly;
        this.country = country;
        this.clientTime = clientTime;
        this.createdBy = createdBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
}
