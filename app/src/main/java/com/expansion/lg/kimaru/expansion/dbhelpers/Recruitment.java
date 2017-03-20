package com.expansion.lg.kimaru.expansion.dbhelpers;

/**
 * Created by kimaru on 3/2/17.
 */

public class Recruitment {

    String name, district, subcounty, division, comment, lat, lon;
    Integer id, addedBy, dateAdded, synced;
    boolean isRead, isImportant;
    int color = -1;
    String picture =  "";

    public Recruitment(){

    }

    public Recruitment(String name, String district, String subcounty, String division, String lat, String lon,
                       String comment, Integer addedBy, Integer dateAdded, Integer synced) {
        this.name = name;
        this.district = district;
        this.subcounty = subcounty;
        this.division = division;
        this.comment = comment;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
        this.synced = synced;
        this.lat = lat;
        this.lon = lon;
    }


    // Get Methods
    public String getName() {
        return name;
    }
    public String getDistrict() {
        return district;
    }
    public String getSubcounty() {
        return subcounty;
    }
    public String getLat() {
        return lat;
    }
    public String getLon() {
        return lon;
    }
    public String getDivision() {
        return division;
    }
    public String getComment() {
        return comment;
    }
    public Integer getAddedBy() {
        return addedBy;
    }
    public Integer getDateAdded() {
        return dateAdded;
    }
    public Integer getSynced() {
        return synced;
    }

    public String getPicture() {
        return picture;
    }
    public Integer getId() {
        return id;
    }
    public boolean isRead() {
        return isRead;
    }
    public boolean isImportant() {
        return isImportant;
    }
    public Integer getColor(){ return color;}

    //Set Methods
    public void setName(String name) {
        this.name = name;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setDivision(String division) {
        this.division = division;
    }
    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }
    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }
    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }
    public void setSynced(Integer synced) {
        this.synced = synced;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }


    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public void setIsImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }
    public void setId(Integer id) {
        this.id = id;
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
