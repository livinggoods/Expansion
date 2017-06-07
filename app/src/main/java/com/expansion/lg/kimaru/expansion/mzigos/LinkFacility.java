package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class LinkFacility {

    String id, facilityName, mappingId, lat, lon, country, subCountyId;
    Integer addedBy;
    Long actLevels, mrdtLevels, dateAdded;

    String picture="";
    int color = -1;
    Boolean read = false;


    public LinkFacility(){

    }

    public LinkFacility(String id, String facilityName, String country, String mappingId, String lat,
                        String lon, String subCountyId, Long dateAdded, Integer addedBy,
                        long actLevels, long mrdtLevels) {
        this.id = id;
        this.facilityName = facilityName;
        this.mappingId = mappingId;
        this.lat = lat;
        this.lon = lon;
        this.subCountyId = subCountyId;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.mrdtLevels = mrdtLevels;
        this.actLevels = actLevels;
        this.country = country;

    }

    // Get Methods

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setSubCountyId(String subCountyId) {
        this.subCountyId = subCountyId;
    }

    public String getSubCountyId() {
        return subCountyId;
    }

    public long getActLevels() {
        return actLevels;
    }

    public long getMrdtLevels() {
        return mrdtLevels;
    }

    public void setActLevels(long actLevels) {
        this.actLevels = actLevels;
    }

    public void setMrdtLevels(long mrdtLevels) {
        this.mrdtLevels = mrdtLevels;
    }

    public Boolean isRead() {
        return read;
    }

    public int getColor() {
        return color;
    }

    public String getPicture() {
        return picture;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
