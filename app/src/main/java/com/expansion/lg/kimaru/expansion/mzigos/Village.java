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



public class Village {



    String id, villageName, mappingId;
    Double lat, lon;
    String country, district, county, subCountyId, parish, communityUnit, ward, linkFacilityId,
            areaChiefName, areaChiefPhone, economicStatus, nameOfNgoDoingMhealth, privateFacilityForAct,
            nameOfNgoDoingIccm, privateFacilityForMrdt, comment, distributorsInTheArea;

    Boolean presenceOfHostels, presenceOfEstates, presenceOfDistributors, chvsTrained,
            traderMarket, largeSupermarket, ngosGivingFreeDrugs, ngoDoingIccm, ngoDoingMhealth;

    Long dateAdded, distanceToBranch, transportCost, distanceToNearestHealthFacility, actPrice,
            actLevels, mrdtLevels, mrdtPrice, distanceToMainRoad, noOfHouseholds,
            mohPoplationDensity, estimatedPopulationDensity;
    Integer addedBy, numberOfFactories, mtnSignalStrength, safaricomSignalStrength,
            orangeSignalStrength, airtelSignalStrength;

    boolean isRead, isImportant, synced, bracOperating;
    int color = -1;
    String picture =  "";



    public Village(){

    }

    public Village(String id, String villageName, String mappingId, Double lat, Double lon,
                   String country, String district, String county, String subCountyId, String parish,
                   String communityUnit, String ward, String linkFacilityId,
                   String areaChiefName, String areaChiefPhone, Long distanceToBranch, Long transportCost,
                   Long distanceToMainRoad, Long noOfHouseholds, Long mohPoplationDensity,
                   Long estimatedPopulationDensity, String economicStatus, Long distanceToNearestHealthFacility,
                   Long actLevels, Long actPrice, Long mrdtLevels, Long mrdtPrice, Boolean presenceOfHostels,
                   Boolean presenceOfEstates, Integer numberOfFactories, Boolean presenceOfDistributors,
                   String distributorsInTheArea, Boolean traderMarket, Boolean largeSupermarket, Boolean ngosGivingFreeDrugs,
                   Boolean ngoDoingIccm, Boolean ngoDoingMhealth, String nameOfNgoDoingIccm,
                   String nameOfNgoDoingMhealth, String privateFacilityForAct, String privateFacilityForMrdt,
                   Long dateAdded, Integer addedBy, String comment, Boolean chvsTrained, boolean synced,
                   boolean bracOperating, Integer mtnSignalStrength, Integer safaricomSignalStrength,
                   Integer orangeSignalStrength, Integer airtelSignalStrength){

        this.id = id;
        this.villageName = villageName;
        this.mappingId = mappingId;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
        this.district = district;
        this.county = county;
        this.subCountyId = subCountyId;
        this.parish = parish;
        this.communityUnit = communityUnit;
        this.ward = ward;
        this.linkFacilityId = linkFacilityId;
        this.areaChiefName = areaChiefName;
        this.areaChiefPhone = areaChiefPhone;
        this.distanceToBranch = distanceToBranch;
        this.transportCost = transportCost;
        this.distanceToMainRoad = distanceToMainRoad;
        this.noOfHouseholds = noOfHouseholds;
        this.mohPoplationDensity = mohPoplationDensity;
        this.estimatedPopulationDensity = estimatedPopulationDensity;
        this.economicStatus = economicStatus;
        this.distanceToNearestHealthFacility = distanceToNearestHealthFacility;
        this.actLevels = actLevels;
        this.actPrice = actPrice;
        this.mrdtLevels = mrdtLevels;
        this.mrdtPrice = mrdtPrice;
        this.presenceOfHostels = presenceOfHostels;
        this.presenceOfEstates = presenceOfEstates;
        this.numberOfFactories = numberOfFactories;
        this.presenceOfDistributors = presenceOfDistributors;
        this.distributorsInTheArea = distributorsInTheArea;
        this.traderMarket = traderMarket;
        this.largeSupermarket = largeSupermarket;
        this.ngosGivingFreeDrugs = ngosGivingFreeDrugs;
        this.ngoDoingIccm = ngoDoingIccm;
        this.ngoDoingMhealth = ngoDoingMhealth;
        this.nameOfNgoDoingIccm = nameOfNgoDoingIccm;
        this.nameOfNgoDoingMhealth = nameOfNgoDoingMhealth;
        this.privateFacilityForAct = privateFacilityForAct;
        this.privateFacilityForMrdt = privateFacilityForMrdt;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.comment = comment;
        this.chvsTrained = chvsTrained;
        this.synced = synced;
        this.bracOperating = bracOperating;
        this.mtnSignalStrength = mtnSignalStrength;
        this.safaricomSignalStrength = safaricomSignalStrength;
        this.orangeSignalStrength = orangeSignalStrength;
        this.airtelSignalStrength = airtelSignalStrength;
    }

    //SET METHODS
    public void setId(String id) {
        this.id = id;
    }
    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
    public void setMappingId(String mappingId){this.mappingId = mappingId;}

    public void setAirtelSignalStrength(Integer airtelSignalStrength) {
        this.airtelSignalStrength = airtelSignalStrength;
    }

    public void setSafaricomSignalStrength(Integer safaricomSignalStrength) {
        this.safaricomSignalStrength = safaricomSignalStrength;
    }

    public void setOrangeSignalStrength(Integer orangeSignalStrength) {
        this.orangeSignalStrength = orangeSignalStrength;
    }

    public void setMtnSignalStrength(Integer mtnSignalStrength) {
        this.mtnSignalStrength = mtnSignalStrength;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public void setBracOperating(boolean bracOperating) {
        this.bracOperating = bracOperating;
    }

    public void setDistributorsInTheArea(String distributorsInTheArea) {
        this.distributorsInTheArea = distributorsInTheArea;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAreaChiefPhone(String areaChiefPhone) {
        this.areaChiefPhone = areaChiefPhone;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public void setActLevels(Long actLevels) {
        this.actLevels = actLevels;
    }

    public void setActPrice(Long actPrice) {
        this.actPrice = actPrice;
    }

    public void setAreaChiefName(String areaChiefName) {
        this.areaChiefName = areaChiefName;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setChvsTrained(Boolean chvsTrained) {
        this.chvsTrained = chvsTrained;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTransportCost(Long transportCost) {
        this.transportCost = transportCost;
    }

    public void setTraderMarket(Boolean traderMarket) {
        this.traderMarket = traderMarket;
    }

    public void setSubCountyId(String subCountyId) {
        this.subCountyId = subCountyId;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setPrivateFacilityForMrdt(String privateFacilityForMrdt) {
        this.privateFacilityForMrdt = privateFacilityForMrdt;
    }

    public void setPrivateFacilityForAct(String privateFacilityForAct) {
        this.privateFacilityForAct = privateFacilityForAct;
    }

    public void setPresenceOfHostels(Boolean presenceOfHostels) {
        this.presenceOfHostels = presenceOfHostels;
    }

    public void setNumberOfFactories(Integer numberOfFactories) {
        this.numberOfFactories = numberOfFactories;
    }

    public void setPresenceOfEstates(Boolean presenceOfEstates) {
        this.presenceOfEstates = presenceOfEstates;
    }

    public void setPresenceOfDistributors(Boolean presenceOfDistributors) {
        this.presenceOfDistributors = presenceOfDistributors;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setNoOfHouseholds(Long noOfHouseholds) {
        this.noOfHouseholds = noOfHouseholds;
    }

    public void setNgosGivingFreeDrugs(Boolean ngosGivingFreeDrugs) {
        this.ngosGivingFreeDrugs = ngosGivingFreeDrugs;
    }

    public void setNgoDoingMhealth(Boolean ngoDoingMhealth) {
        this.ngoDoingMhealth = ngoDoingMhealth;
    }

    public void setNgoDoingIccm(Boolean ngoDoingIccm) {
        this.ngoDoingIccm = ngoDoingIccm;
    }

    public void setNameOfNgoDoingMhealth(String nameOfNgoDoingMhealth) {
        this.nameOfNgoDoingMhealth = nameOfNgoDoingMhealth;
    }

    public void setNameOfNgoDoingIccm(String nameOfNgoDoingIccm) {
        this.nameOfNgoDoingIccm = nameOfNgoDoingIccm;
    }

    public void setMrdtPrice(Long mrdtPrice) {
        this.mrdtPrice = mrdtPrice;
    }

    public void setMrdtLevels(Long mrdtLevels) {
        this.mrdtLevels = mrdtLevels;
    }

    public void setMohPoplationDensity(Long mohPoplationDensity) {
        this.mohPoplationDensity = mohPoplationDensity;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setLinkFacilityId(String linkFacilityId) {
        this.linkFacilityId = linkFacilityId;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLargeSupermarket(Boolean largeSupermarket) {
        this.largeSupermarket = largeSupermarket;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setEstimatedPopulationDensity(Long estimatedPopulationDensity) {
        this.estimatedPopulationDensity = estimatedPopulationDensity;
    }

    public void setEconomicStatus(String economicStatus) {
        this.economicStatus = economicStatus;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setDistanceToNearestHealthFacility(Long distanceToNearestHealthFacility) {
        this.distanceToNearestHealthFacility = distanceToNearestHealthFacility;
    }

    public void setDistanceToMainRoad(Long distanceToMainRoad) {
        this.distanceToMainRoad = distanceToMainRoad;
    }

    public void setDistanceToBranch(Long distanceToBranch) {
        this.distanceToBranch = distanceToBranch;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setCommunityUnit(String communityUnit) {
        this.communityUnit = communityUnit;
    }

    //GET METHODS

    public Boolean getPresenceOfHostels() {
        return presenceOfHostels;
    }

    public int getColor() {
        return color;
    }

    public Boolean getChvsTrained() {
        return chvsTrained;
    }

    public Double getLat() {
        return lat;
    }

    public Boolean getPresenceOfEstates() {
        return presenceOfEstates;
    }

    public Double getLon() {
        return lon;
    }

    public String getAreaChiefName() {
        return areaChiefName;
    }

    public String getAreaChiefPhone() {
        return areaChiefPhone;
    }

    public String getComment() {
        return comment;
    }

    public Integer getNumberOfFactories() {
        return numberOfFactories;
    }

    public String getCommunityUnit() {
        return communityUnit;
    }

    public String getWard() {
        return ward;
    }

    public String getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    public String getDistrict() {
        return district;
    }

    public Boolean getLargeSupermarket() {
        return largeSupermarket;
    }

    public Boolean getNgosGivingFreeDrugs() {
        return ngosGivingFreeDrugs;
    }

    public Boolean getNgoDoingIccm() {
        return ngoDoingIccm;
    }

    public Boolean getPresenceOfDistributors() {
        return presenceOfDistributors;
    }

    public String getEconomicStatus() {
        return economicStatus;
    }

    public String getId() {
        return id;
    }

    public Boolean getNgoDoingMhealth() {
        return ngoDoingMhealth;
    }

    public String getLinkFacilityId() {
        return linkFacilityId;
    }

    public Boolean getTraderMarket() {
        return traderMarket;
    }

    public String getMappingId() {
        return mappingId;
    }

    public String getNameOfNgoDoingIccm() {
        return nameOfNgoDoingIccm;
    }

    public String getNameOfNgoDoingMhealth() {
        return nameOfNgoDoingMhealth;
    }

    public String getParish() {
        return parish;
    }

    public String getPrivateFacilityForAct() {
        return privateFacilityForAct;
    }

    public String getPrivateFacilityForMrdt() {
        return privateFacilityForMrdt;
    }

    public String getSubCountyId() {
        return subCountyId;
    }

    public String getVillageName() {
        return villageName;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public boolean isRead() {
        return isRead;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public Long getActLevels() {
        return actLevels;
    }

    public Long getActPrice() {
        return actPrice;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public Long getDistanceToBranch() {
        return distanceToBranch;
    }

    public String getPicture() {
        return picture;
    }

    public Long getDistanceToMainRoad() {
        return distanceToMainRoad;
    }

    public Long getDistanceToNearestHealthFacility() {
        return distanceToNearestHealthFacility;
    }

    public Long getEstimatedPopulationDensity() {
        return estimatedPopulationDensity;
    }

    public Long getMohPoplationDensity() {
        return mohPoplationDensity;
    }

    public Long getMrdtLevels() {
        return mrdtLevels;
    }

    public Long getMrdtPrice() {
        return mrdtPrice;
    }

    public Long getTransportCost() {
        return transportCost;
    }

    public Long getNoOfHouseholds() {
        return noOfHouseholds;
    }

    public boolean isSynced() {
        return synced;
    }

    public String getDistributorsInTheArea() {
        return distributorsInTheArea;
    }

    public boolean isBracOperating() {
        return bracOperating;
    }

    public Integer getAirtelSignalStrength() {
        return airtelSignalStrength;
    }

    public Integer getMtnSignalStrength() {
        return mtnSignalStrength;
    }

    public Integer getOrangeSignalStrength() {
        return orangeSignalStrength;
    }

    public Integer getSafaricomSignalStrength() {
        return safaricomSignalStrength;
    }
}
