package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class SubCounty {

    String id, subCountyName, country, mappingId, lat, lon, contactPerson, contactPersonPhone;
    Integer dateAdded, addedBy;

    public SubCounty(){

    }

    public SubCounty(String id, String subCountyName, String country, String mappingId,
                     String contactPerson, String contactPersonPhone,
                     String lat, String lon, Integer dateAdded, Integer addedBy) {
        this.id = id;
        this.subCountyName = subCountyName;
        this.country = country;
        this.mappingId = mappingId;
        this.lat = lat;
        this.lon = lon;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.contactPerson = contactPerson;
        this.contactPersonPhone = contactPersonPhone;
    }

    // Get Methods

    public String getSubCountyName() {
        return subCountyName;
    }

    public void setSubCountyName(String subCountyName) {
        this.subCountyName = subCountyName;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getDateAdded() {
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

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public String getContactPerson() {
        return contactPerson;
    }
}
