package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class Mapping {

    String id, mappingName, subCounty, country, contactPerson, contactPersonPhone,
            comment, county, district, mappingRegion;
    Integer addedBy;
    Long dateAdded;
    int synced = 0;

    boolean isRead = false;
    boolean isImportant = true;
    int color = -1;
    String picture =  "";

    public Mapping(){

    }

    public Mapping(String id, String mappingName, String country, String county, Long dateAdded,
                   Integer addedBy, String contactPerson, String contactPersonPhone,
                   int synced, String comment, String district, String subCounty,
                   String mappingRegion) {
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
        this.district = district;
        this.subCounty = subCounty;
        this.mappingRegion = mappingRegion;
    }


    // Get Methods


    public String getMappingRegion() {
        return mappingRegion;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

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

    public String getDistrict() {
        return district;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Long dateAdded) {
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

    public int getSynced() {
        return synced;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSynced(int synced) {
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

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setMappingRegion(String mappingRegion) {
        this.mappingRegion = mappingRegion;
    }
}
