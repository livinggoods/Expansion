package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/28/17.
 */

/**
 *
 */

public class Partners {
    String partnerID, partnerName, contactPerson, contactPersonPhone,
            parent, mappingId, country, comment;
    boolean synced, archived;
    Long dateAdded, addedBy;

    //empty constructor
    public Partners(){}

    public Partners(String partnerID, String partnerName, String contactPerson,
                    String contactPersonPhone, String parent, String mappingId,
                    String country, String comment, boolean synced, boolean archived,
                    Long dateAdded, Long addedBy) {
        this.partnerID = partnerID;
        this.partnerName = partnerName;
        this.contactPerson = contactPerson;
        this.contactPersonPhone = contactPersonPhone;
        this.parent = parent;
        this.mappingId = mappingId;
        this.country = country;
        this.comment = comment;
        this.synced = synced;
        this.archived = archived;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public String getParent() {
        return parent;
    }

    public String getMappingId() {
        return mappingId;
    }

    public String getCountry() {
        return country;
    }

    public String getComment() {
        return comment;
    }

    public boolean isSynced() {
        return synced;
    }

    public boolean isArchived() {
        return archived;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public Long getAddedBy() {
        return addedBy;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setAddedBy(Long addedBy) {
        this.addedBy = addedBy;
    }
}
