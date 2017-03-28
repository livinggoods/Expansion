package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class Mapping {

    String id, mappingName, country, contactPerson, contactPersonPhone, comment, county;
    Integer dateAdded, addedBy;
    boolean synced;

    boolean isRead = false;
    boolean isImportant = true;
    int color = -1;
    String picture =  "";

    public Mapping(){

    }

    public Mapping(String id, String mappingName, String country, String county, Integer dateAdded,
                   Integer addedBy, String contactPerson, String contactPersonPhone,
                   boolean synced, String comment) {
        this.id = id;
        this.mappingName = mappingName;
        this.country = country;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.contactPerson = contactPerson;
        this.contactPersonPhone = contactPersonPhone;
        this.synced = synced;
        this.county = county;
        this.comment = comment;
    }


    // Get Methods

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public Integer getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public boolean isSynced() {
        return synced;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    //
    public String getPicture() {
        return "";
    }
    public int getColor() {
        return 1;
    }
    public boolean isImportant() {
        return true;
    }
    public boolean isRead() {
        return isRead;
    }


    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public void setIsImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
