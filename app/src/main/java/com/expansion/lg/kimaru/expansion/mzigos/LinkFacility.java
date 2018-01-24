package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class LinkFacility {

    String id, facilityName, mappingId, country, subCountyId, mflCode, county, parish;
    Integer addedBy;
    Double lat, lon;
    Long actLevels, mrdtLevels, dateAdded;

    String picture="";
    int color = -1;
    Boolean read = false;


    public LinkFacility(){

    }

    public LinkFacility(String id, String facilityName, String country, String mappingId, Double lat,
                        Double lon, String subCountyId, Long dateAdded, Integer addedBy,
                        long actLevels, long mrdtLevels, String mflCode, String county, String parish) {
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
        this.mflCode = mflCode;
        this.county = county;
        this.parish = parish;

    }

    // Get Methods


    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getMflCode() {
        return mflCode;
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

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
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

    public void setMflCode(String mflCode) {
        this.mflCode = mflCode;
    }

    public void setActLevels(Long actLevels) {
        this.actLevels = actLevels;
    }

    public void setMrdtLevels(Long mrdtLevels) {
        this.mrdtLevels = mrdtLevels;
    }
}
