package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 *
 * Representation of the community Unit
 * subcounty, areaChiefName, ward, gps{}, noOfChVs, NumberOfVillages, distanceToBranch, transportCost
 * distanceTOMainRoad, noOfHouseholds, mohPoplationDensity, estimatedPopulationDensity, economicStatus,
 * distanceTONearestHealthFacility, actLevels, actPrice, privateFacilityForAct, mrdtLevels,  mrdtPrice,
 * privateFacilityForMrdt, presenceOfEstates, presenceOfFactories, presenceOfHostels, noOfDistibutors,
 * traderMarket, largeSupermarket, ngosFreeDrugs, ngosICCM, ngoMHealth, connectivity
 */



public class CommunityUnit {

    /*
    * Representation of the community Unit
     * ,
     * distanceTOMainRoad, noOfHouseholds, mohPoplationDensity, estimatedPopulationDensity, economicStatus,
     * distanceTONearestHealthFacility, actLevels, actPrice, privateFacilityForAct, mrdtLevels,  mrdtPrice,
     * privateFacilityForMrdt, presenceOfEstates, presenceOfFactories, presenceOfHostels, noOfDistibutors,
     * traderMarket, largeSupermarket, ngosFreeDrugs, ngosICCM, ngoMHealth, connectivity
     */


    // Some community units are split into 2 and they share some data


    String id, communityUnitName, mappingId, country, subCountyId, linkFacilityId, areaChiefName;
    String ward, comment;
    Double lat, lon;
    long dateAdded, addedBy, numberOfChvs, householdPerChv, numberOfVillages, distanceToBranch, transportCost;
    long distanceTOMainRoad, noOfHouseholds, mohPoplationDensity, estimatedPopulationDensity;
    long distanceTONearestHealthFacility, actLevels, actPrice, mrdtLevels,mrdtPrice, noOfDistibutors;
    String economicStatus, areaChiefPhone; //lower, middle or upper
    String privateFacilityForAct, privateFacilityForMrdt;
    String nameOfNgoDoingIccm, nameOfNgoDoingMhealth;
    Long presenceOfFactories, populationAsPerChief, chvsHouseholdsAsPerChief;

    boolean chvsTrained, presenceOfEstates, presenceOfHostels, traderMarket, largeSupermarket, ngosGivingFreeDrugs;
    boolean ngoDoingIccm, ngoDoingMhealth;

    boolean isRead, isImportant;
    int color = -1;
    String picture =  "";

    int noChvGokBasicTrained = 0;

    public CommunityUnit(){

    }

    public CommunityUnit(String id, String communityUnitName, String mappingId, Double lat, Double lon,
                         String country, String subCountyId, String linkFacilityId, String areaChiefName,
                         String areaChiefPhone, String ward, String economicStatus, String privateFacilityForAct,
                         String privateFacilityForMrdt,
                         String nameOfNgoDoingIccm, String nameOfNgoDoingMhealth, long dateAdded, long addedBy,
                         long numberOfChvs, long householdPerChv, long numberOfVillages,
                         long distanceToBranch, long transportCost, long distanceToMainRoad,
                         long noOfHouseholds, long mohPoplationDensity, long estimatedPopulationDensity,
                         long distanceTONearestHealthFacility, long actLevels, long actPrice,
                         long mrdtLevels, long mrdtPrice, long noOfDistibutors, boolean chvsTrained,
                         boolean presenceOfEstates, Long presenceOfFactories,
                         boolean presenceOfHostels, boolean traderMarket, boolean largeSupermarket,
                         boolean ngosGivingFreeDrugs, boolean ngoDoingIccm, boolean ngoDoingMhealth,
                         Long populationAsPerChief, Long chvsHouseholdsAsPerChief, String comment, int noChvGokBasicTrained) {
        this.id = id;
        this.communityUnitName = communityUnitName;
        this.mappingId = mappingId;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
        this.subCountyId = subCountyId;
        this.linkFacilityId = linkFacilityId;
        this.areaChiefName = areaChiefName;
        this.ward = ward;
        this.economicStatus = economicStatus;
        this.privateFacilityForAct = privateFacilityForAct;
        this.privateFacilityForMrdt = privateFacilityForMrdt;
        this.nameOfNgoDoingIccm = nameOfNgoDoingIccm;
        this.nameOfNgoDoingMhealth = nameOfNgoDoingMhealth;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.numberOfChvs = numberOfChvs;
        this.householdPerChv = householdPerChv;
        this.numberOfVillages = numberOfVillages;
        this.distanceToBranch = distanceToBranch;
        this.transportCost = transportCost;
        this.distanceTOMainRoad = distanceToMainRoad;
        this.noOfHouseholds = noOfHouseholds;
        this.mohPoplationDensity = mohPoplationDensity;
        this.estimatedPopulationDensity = estimatedPopulationDensity;
        this.distanceTONearestHealthFacility = distanceTONearestHealthFacility;
        this.actLevels = actLevels;
        this.actPrice = actPrice;
        this.mrdtLevels = mrdtLevels;
        this.mrdtPrice = mrdtPrice;
        this.noOfDistibutors = noOfDistibutors;
        this.chvsTrained = chvsTrained;
        this.presenceOfEstates = presenceOfEstates;
        this.presenceOfFactories = presenceOfFactories;
        this.presenceOfHostels = presenceOfHostels;
        this.traderMarket = traderMarket;
        this.largeSupermarket = largeSupermarket;
        this.ngosGivingFreeDrugs = ngosGivingFreeDrugs;
        this.ngoDoingIccm = ngoDoingIccm;
        this.ngoDoingMhealth = ngoDoingMhealth;
        this.areaChiefPhone = areaChiefPhone;
        this.populationAsPerChief = populationAsPerChief;
        this.chvsHouseholdsAsPerChief = chvsHouseholdsAsPerChief;
        this.comment = comment;
        this.noChvGokBasicTrained = noChvGokBasicTrained;

    }

    // Get Methods


    public int getNoChvGokBasicTrained() {
        return noChvGokBasicTrained;
    }

    public void setNoChvGokBasicTrained(int noChvGokBasicTrained) {
        this.noChvGokBasicTrained = noChvGokBasicTrained;
    }

    public String getComment() {
        return comment;
    }

    public Long getPopulationAsPerChief() {
        return populationAsPerChief;
    }

    public Long getChvsHouseholdsAsPerChief() {
        return chvsHouseholdsAsPerChief;
    }

    public String getAreaChiefPhone() {
        return areaChiefPhone;
    }

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
        return numberOfChvs;
    }

    public void setNumberOfChvs(long numberOfChvs) {
        this.numberOfChvs = numberOfChvs;
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

    public long getDistanceToBranch() {
        return distanceToBranch;
    }

    public String getAreaChiefName() {
        return areaChiefName;
    }

    public String getWard() {
        return ward;
    }

    public void setAreaChiefName(String areaChiefName) {
        this.areaChiefName = areaChiefName;
    }

    public boolean isLargeSupermarket() {
        return largeSupermarket;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public boolean isNgoDoingIccm() {
        return ngoDoingIccm;
    }

    public boolean isNgoDoingMhealth() {
        return ngoDoingMhealth;
    }

    public long getDistanceTOMainRoad() {
        return distanceTOMainRoad;
    }

    public long getNumberOfVillages() {
        return numberOfVillages;
    }

    public void setNumberOfVillages(long numberOfVillages) {
        this.numberOfVillages = numberOfVillages;
    }

    public boolean isNgosGivingFreeDrugs() {
        return ngosGivingFreeDrugs;
    }

    public long getMohPoplationDensity() {
        return mohPoplationDensity;
    }

    public long getActLevels() {
        return actLevels;
    }

    public boolean isPresenceOfEstates() {
        return presenceOfEstates;
    }

    public long getNoOfHouseholds() {
        return noOfHouseholds;
    }

    public long getTransportCost() {
        return transportCost;
    }

    public void setDistanceToBranch(long distanceToBranch) {
        this.distanceToBranch = distanceToBranch;
    }

    public Long presenceOfFactories() {
        return presenceOfFactories;
    }

    public long getActPrice() {
        return actPrice;
    }

    public boolean isPresenceOfHostels() {
        return presenceOfHostels;
    }

    public boolean isTraderMarket() {
        return traderMarket;
    }

    public long getDistanceTONearestHealthFacility() {
        return distanceTONearestHealthFacility;
    }

    public long getEstimatedPopulationDensity() {
        return estimatedPopulationDensity;
    }

    public long getMrdtLevels() {
        return mrdtLevels;
    }

    public long getMrdtPrice() {
        return mrdtPrice;
    }

    public long getNoOfDistibutors() {
        return noOfDistibutors;
    }

    public String getEconomicStatus() {
        return economicStatus;
    }

    public String getNameOfNgoDoingIccm() {
        return nameOfNgoDoingIccm;
    }

    public String getNameOfNgoDoingMhealth() {
        return nameOfNgoDoingMhealth;
    }

    public String getPrivateFacilityForAct() {
        return privateFacilityForAct;
    }

    public String getPrivateFacilityForMrdt() {
        return privateFacilityForMrdt;
    }

    public void setActLevels(long actLevels) {
        this.actLevels = actLevels;
    }

    public void setTransportCost(long transportCost) {
        this.transportCost = transportCost;
    }

    public void setDistanceTOMainRoad(long distanceTOMainRoad) {
        this.distanceTOMainRoad = distanceTOMainRoad;
    }

    public void setTraderMarket(boolean traderMarket) {
        this.traderMarket = traderMarket;
    }

    public void setActPrice(long actPrice) {
        this.actPrice = actPrice;
    }

    public void setDistanceTONearestHealthFacility(long distanceTONearestHealthFacility) {
        this.distanceTONearestHealthFacility = distanceTONearestHealthFacility;
    }

    public void setEconomicStatus(String economicStatus) {
        this.economicStatus = economicStatus;
    }

    public void setEstimatedPopulationDensity(long estimatedPopulationDensity) {
        this.estimatedPopulationDensity = estimatedPopulationDensity;
    }

    public void setLargeSupermarket(boolean largeSupermarket) {
        this.largeSupermarket = largeSupermarket;
    }

    public void setMohPoplationDensity(long mohPoplationDensity) {
        this.mohPoplationDensity = mohPoplationDensity;
    }

    public void setMrdtLevels(long mrdtLevels) {
        this.mrdtLevels = mrdtLevels;
    }

    public void setMrdtPrice(long mrdtPrice) {
        this.mrdtPrice = mrdtPrice;
    }

    public void setNoOfHouseholds(long noOfHouseholds) {
        this.noOfHouseholds = noOfHouseholds;
    }

    public void setNameOfNgoDoingIccm(String nameOfNgoDoingIccm) {
        this.nameOfNgoDoingIccm = nameOfNgoDoingIccm;
    }

    public void setNameOfNgoDoingMhealth(String nameOfNgoDoingMhealth) {
        this.nameOfNgoDoingMhealth = nameOfNgoDoingMhealth;
    }

    public void setNgoDoingIccm(boolean ngoDoingIccm) {
        this.ngoDoingIccm = ngoDoingIccm;
    }

    public void setNgoDoingMhealth(boolean ngoDoingMhealth) {
        this.ngoDoingMhealth = ngoDoingMhealth;
    }

    public void setNgosGivingFreeDrugs(boolean ngosGivingFreeDrugs) {
        this.ngosGivingFreeDrugs = ngosGivingFreeDrugs;
    }

    public void setPrivateFacilityForMrdt(String privateFacilityForMrdt) {
        this.privateFacilityForMrdt = privateFacilityForMrdt;
    }

    public void setNoOfDistibutors(long noOfDistibutors) {
        this.noOfDistibutors = noOfDistibutors;
    }

    public void setPresenceOfEstates(boolean presenceOfEstates) {
        this.presenceOfEstates = presenceOfEstates;
    }
    public boolean isPresenceOfFactories(){
        return this.presenceOfFactories.compareTo(0L) > 0;
    }

    public void setPresenceOfFactories(Long presenceOfFactories) {
        this.presenceOfFactories = presenceOfFactories;
    }

    public void setPresenceOfHostels(boolean presenceOfHostels) {
        this.presenceOfHostels = presenceOfHostels;
    }

    public void setAreaChiefPhone(String areaChiefPhone) {
        this.areaChiefPhone = areaChiefPhone;
    }

    public void setPrivateFacilityForAct(String privateFacilityForAct) {
        this.privateFacilityForAct = privateFacilityForAct;
    }

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

    public void setPopulationAsPerChief(Long populationAsPerChief) {
        this.populationAsPerChief = populationAsPerChief;
    }

    public void setChvsHouseholdsAsPerChief(Long chvsHouseholdsAsPerChief) {
        this.chvsHouseholdsAsPerChief = chvsHouseholdsAsPerChief;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
