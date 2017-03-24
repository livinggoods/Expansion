package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class LinkFacility {

    String id, facilityName, mappingId, lat, lon, country, subCountyId;
    Integer dateAdded, addedBy;

    public LinkFacility(){

    }

    public LinkFacility(String id, String facilityName, String country, String mappingId,
                        String lat, String lon, String subCountyId, Integer dateAdded, Integer addedBy) {
        this.id = id;
        this.facilityName = facilityName;
        this.mappingId = mappingId;
        this.lat = lat;
        this.lon = lon;
        this.subCountyId = subCountyId;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
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

    public void setSubCountyId(String subCountyId) {
        this.subCountyId = subCountyId;
    }

    public String getSubCountyId() {
        return subCountyId;
    }

}
