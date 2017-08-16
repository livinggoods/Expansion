package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/28/17.
 */

/**
 *
 */

public class PartnerActivity {
    String partnerId, country, county, subcounty, parish, village, communityUnit, mappingId,
            comment, activities;
    boolean doingMhealth, doingIccm, givingFreeDrugs, givingStipend;
    Long dateAdded, addedBy;

    //empty constructor
    public PartnerActivity(){}

    public PartnerActivity(String partnerId, String country, String county, String subcounty,
                           String parish, String village, String communityUnit, String mappingId,
                           String comment, boolean doingMhealth, boolean doingIccm,
                           boolean givingFreeDrugs, boolean givingStipend,
                           Long dateAdded, Long addedBy, String activities) {
        this.partnerId = partnerId;
        this.country = country;
        this.county = county;
        this.subcounty = subcounty;
        this.parish = parish;
        this.village = village;
        this.communityUnit = communityUnit;
        this.mappingId = mappingId;
        this.comment = comment;
        this.doingMhealth = doingMhealth;
        this.doingIccm = doingIccm;
        this.givingFreeDrugs = givingFreeDrugs;
        this.givingStipend = givingStipend;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.activities = activities;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    public String getSubcounty() {
        return subcounty;
    }

    public String getParish() {
        return parish;
    }

    public String getVillage() {
        return village;
    }

    public String getCommunityUnit() {
        return communityUnit;
    }

    public String getMappingId() {
        return mappingId;
    }

    public String getComment() {
        return comment;
    }

    public boolean isDoingMhealth() {
        return doingMhealth;
    }

    public boolean isDoingIccm() {
        return doingIccm;
    }

    public boolean isGivingFreeDrugs() {
        return givingFreeDrugs;
    }

    public boolean isGivingStipend() {
        return givingStipend;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public Long getAddedBy() {
        return addedBy;
    }

    public String getActivities() {
        return activities;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setCommunityUnit(String communityUnit) {
        this.communityUnit = communityUnit;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDoingMhealth(boolean doingMhealth) {
        this.doingMhealth = doingMhealth;
    }

    public void setDoingIccm(boolean doingIccm) {
        this.doingIccm = doingIccm;
    }

    public void setGivingFreeDrugs(boolean givingFreeDrugs) {
        this.givingFreeDrugs = givingFreeDrugs;
    }

    public void setGivingStipend(boolean givingStipend) {
        this.givingStipend = givingStipend;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setAddedBy(Long addedBy) {
        this.addedBy = addedBy;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }
}
