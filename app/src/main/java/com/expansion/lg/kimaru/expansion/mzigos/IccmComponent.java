package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 8/18/17.
 */

public class IccmComponent {
    Integer id;
    String componentName, comment, status, dateAdded;
    Long addedBy, clientTime;
    boolean archived;

    public IccmComponent() {}

    public IccmComponent(Integer id, String componentName, String comment, String status,
                         String dateAdded, Long addedBy, Long clientTime, boolean archived) {
        this.id = id;
        this.componentName = componentName;
        this.comment = comment;
        this.status = status;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.clientTime = clientTime;
        this.archived = archived;
    }

    public Integer getId() {
        return id;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public Long getAddedBy() {
        return addedBy;
    }

    public Long getClientTime() {
        return clientTime;
    }

    public boolean isArchived() {
        return archived;
    }

    ///set

    public void setId(Integer id) {
        this.id = id;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setAddedBy(Long addedBy) {
        this.addedBy = addedBy;
    }

    public void setClientTime(Long clientTime) {
        this.clientTime = clientTime;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
