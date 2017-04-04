package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/28/17.
 */

public class Partners {
    String partnerID;
    String partnerName;
    boolean doingIccm;
    String iccmComponent;
    boolean doingMHealth;
    String comment;
    Long dateAdded;
    Long addedBy;

    //empty constructor
    public Partners(){}

    public Partners (String partnerID, String partnerName, String iccmComponent,
                     boolean doingIccm, boolean doingMHealth, String comment, Long dateAdded, Long addedBy){
        this.partnerID = partnerID;
        this.doingIccm = doingIccm;
        this.partnerName = partnerName;
        this.iccmComponent = iccmComponent;
        this.doingMHealth = doingMHealth;
        this.comment = comment;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDoingIccm(boolean doingIccm) {
        this.doingIccm = doingIccm;
    }

    public void setDoingMHealth(boolean doingMHealth) {
        this.doingMHealth = doingMHealth;
    }

    public void setIccmComponent(String iccmComponent) {
        this.iccmComponent = iccmComponent;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public String getComment() {
        return comment;
    }

    public boolean isDoingIccm() {
        return doingIccm;
    }

    public boolean isDoingMHealth() {
        return doingMHealth;
    }

    public String getIccmComponent() {
        return iccmComponent;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public Long getAddedBy() {
        return addedBy;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setAddedBy(Long addedBy) {
        this.addedBy = addedBy;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }
}
