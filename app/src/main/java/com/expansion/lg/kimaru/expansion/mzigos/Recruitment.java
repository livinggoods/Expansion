package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class Recruitment {


    String id, name, district, subcounty, division, comment, lat, lon, country,
            county, subCountyId, regionId;
    Integer addedBy, synced, countyId, locationId;
    Long dateAdded;

    boolean isRead, isImportant;
    int color = -1;
    String picture =  "";

    CountyLocation countyLocation;
    KeCounty keCounty;

    SubCounty subCountyObj;

    public Recruitment(){

    }

    public Recruitment(String id, String name, String district, String subcounty,
                       String division, String lat, String lon, String comment, Integer addedBy,
                       Long dateAdded, Integer synced, String country, String county,
                       String subCountyId, Integer countyId, Integer locationId, String regionId) {
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
        this.id = id;
        this.country = country;
        this.county = county;
        this.subCountyId = subCountyId;
        this.countyId = countyId;
        this.locationId = locationId;
        this.regionId = regionId;
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
    public Long getDateAdded() {
        return dateAdded;
    }
    public Integer getSynced() {
        return synced;
    }

    public String getSubCountyId() {
        return subCountyId;
    }

    public void setSubCountyId(String subCountyId) {
        this.subCountyId = subCountyId;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    public String getPicture() {
        return picture;
    }
    public String getId() {
        return id;
    }
    public boolean isRead() {
        return isRead;
    }
    public boolean isImportant() {
        return isImportant;
    }
    public Integer getColor(){ return color;}

    public String getRegionId() {
        return regionId;
    }

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
    public void setDateAdded(Long dateAdded) {
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

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public void setIsImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public CountyLocation getCountyLocation() {
        return countyLocation;
    }

    public void setCountyLocation(CountyLocation countyLocation) {
        this.countyLocation = countyLocation;
    }

    public KeCounty getKeCounty() {
        return keCounty;
    }

    public SubCounty getSubCountyObj() {
        return subCountyObj;
    }

    public void setSubCountyObj(SubCounty subCountyObj) {
        this.subCountyObj = subCountyObj;
    }

    public void setKeCounty(KeCounty keCounty) {
        this.keCounty = keCounty;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
