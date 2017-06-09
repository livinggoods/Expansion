package com.expansion.lg.kimaru.expansion.mzigos;

import android.test.ServiceTestCase;

/**
 * Created by kimaru on 6/9/17.
 */

public class Mobilization {
    String id, name, mappingId, country, comment, county, district, subCounty, parish;
    Integer addedBy;
    Long dateAdded;
    Boolean synced;

    public Mobilization() {
    }

    public Mobilization(String id, String name, String mappingId, String country, String comment,
                        String county, String district, String subCounty, String parish,
                        Integer addedBy, Long dateAdded, Boolean synced) {
        this.id = id;
        this.name = name;
        this.mappingId = mappingId;
        this.country = country;
        this.comment = comment;
        this.county = county;
        this.district = district;
        this.subCounty = subCounty;
        this.parish = parish;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
        this.synced = synced;
    }

// GET


    public String getCounty() {
        return county;
    }

    public String getDistrict() {
        return district;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public String getParish() {
        return parish;
    }

    public String getComment() {
        return comment;
    }

    public Boolean getSynced() {
        return synced;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMappingId() {
        return mappingId;
    }

    public String getCountry() {
        return country;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public Long getDateAdded() {
        return dateAdded;
    }


    // SET

    public void setId(String id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }
}
