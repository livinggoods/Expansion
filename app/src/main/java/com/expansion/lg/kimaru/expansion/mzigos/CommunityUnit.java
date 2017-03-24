package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 *
 * Representation
 */

public class CommunityUnit {

    String id, communityUnitName, mappingId, lat, lon, country, subCountyId, linkFacilityId;
    long dateAdded, addedBy, numberOfChvs, householdPerChv;
    boolean chvsTrained;

    public CommunityUnit(){

    }

    public CommunityUnit(String id, String communityUnitName, String mappingId, String lat,
                       String lon, String subCountyId, String country, String linkFacilityId,
                       long numberOfChvs, long householdPerChv, long dateAdded, long addedBy) {
        this.id = id;
        this.communityUnitName = communityUnitName;
        this.mappingId = mappingId;
        this.lat = lat;
        this.lon = lon;
        this.subCountyId = subCountyId;
        this.linkFacilityId = linkFacilityId;
        this.numberOfChvs = numberOfChvs;
        this.householdPerChv = householdPerChv;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
    }

    // Get Methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCommunityUnitName() {
        return communityUnitName;
    }

    public void setCommunityUnitName(String communityUnitName) {
        this.communityUnitName = communityUnitName;
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
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getLinkFacilityId() {
        return linkFacilityId;
    }

    public void setLinkFacilityId(String linkFacilityId) {
        this.linkFacilityId = linkFacilityId;
    }



    public long getAddedBy() {
        return addedBy;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setAddedBy(long addedBy) {
        this.addedBy = addedBy;
    }


    ////
    public long getNumberOfChvs() {
        return dateAdded;
    }

    public void setNumberOfChvs(long addedBy) {
        this.addedBy = addedBy;
    }

    public long getHouseholdPerChv() {
        return householdPerChv;
    }

    public void setHouseholdPerChv(long householdPerChv) {
        this.householdPerChv = householdPerChv;
    }

    public boolean isChvsTrained() {
        return chvsTrained;
    }

    public void setChvsTrained(boolean chvsTrained) {
        this.chvsTrained = chvsTrained;
    }

    public void setSubCountyId(String subCountyId) {
        this.subCountyId = subCountyId;
    }

    public String getSubCountyId() {
        return subCountyId;
    }

}
