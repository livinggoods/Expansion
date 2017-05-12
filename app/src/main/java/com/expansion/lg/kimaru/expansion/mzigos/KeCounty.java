package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class KeCounty {

    String id;
    String countyName;
    String country;
    String lat;
    String lon;
    String contactPerson;
    String countyCode;
    String contactPersonPhone;
    String mainTown;
    String countySupport;
    boolean chvActivity;
    String chvActivityLevel;
    String countyPopulation;
    String noOfVillages;
    String mainTownPopulation;
    String servicePopulation;
    String populationDensity;
    String transportCost;
    String majorRoads;
    String healtFacilities;
    String privateClinicsInTown;
    String privateClinicsInRadius;
    String communityUnits;
    String mainSupermarkets;
    String mainBanks;
    String anyMajorBusiness;
    String comments;
    boolean recommended;
    Integer dateAdded;
    Integer addedBy;

    //boolean isRead, isImportant;
    int color = -1;
    String picture =  "";

    public KeCounty(){

    }

    public KeCounty(String id, String countyName, String countyCode, String country, String lat, String lon, String contactPerson,
                    String contactPersonPhone, String mainTown, String countySupport,
                    String chvActivityLevel, String countyPopulation, String noOfVillages,
                    String mainTownPopulation, String servicePopulation, String populationDensity,
                    String transportCost, String majorRoads, String healtFacilities,
                    String privateClinicsInTown, String privateClinicsInRadius, String communityUnits,
                    String mainSupermarkets, String mainBanks, String anyMajorBusiness,
                    String comments, boolean recommended, Integer dateAdded, Integer addedBy) {

        this.id = id;
        this.countyName = countyName;
        this.country = country;
        this.lat = lat;
        this.lon = lon;
        this.contactPerson = contactPerson;
        this.contactPersonPhone = contactPersonPhone;
        this.mainTown = mainTown;
        this.countySupport = countySupport;
        this.chvActivityLevel = chvActivityLevel;
        this.chvActivity = chvActivityLevel != "0";
        this.countyPopulation = countyPopulation;
        this.noOfVillages = noOfVillages;
        this.mainTownPopulation = mainTownPopulation;
        this.servicePopulation = servicePopulation;
        this.populationDensity = populationDensity;
        this.transportCost = transportCost;
        this.majorRoads = majorRoads;
        this.healtFacilities = healtFacilities;
        this.privateClinicsInTown = privateClinicsInTown;
        this.privateClinicsInRadius = privateClinicsInRadius;
        this.communityUnits = communityUnits;
        this.mainSupermarkets = mainSupermarkets;
        this.mainBanks = mainBanks;
        this.anyMajorBusiness = anyMajorBusiness;
        this.comments = comments;
        this.recommended = recommended;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.countyCode = countyCode;
    }

    // Get Methods


    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyName() {
        return countyName;
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

    public String getMainBanks() {
        return mainBanks;
    }

    public String getAnyMajorBusiness() {
        return anyMajorBusiness;
    }

    public boolean isChvActivity() {
        return chvActivity;
    }

    public String getChvActivityLevel() {
        return chvActivityLevel;
    }

    public void setChvActivityLevel(String chvActivityLevel) {
        this.chvActivityLevel = chvActivityLevel;
    }

    public String getComments() {
        return comments;
    }

    public String getCommunityUnits() {
        return communityUnits;
    }

    public String getCountyPopulation() {
        return countyPopulation;
    }

    public String getCountySupport() {
        return countySupport;
    }

    public String getHealtFacilities() {
        return healtFacilities;
    }

    public String getMainSupermarkets() {
        return mainSupermarkets;
    }

    public String getMainTown() {
        return mainTown;
    }

    public String getMajorRoads() {
        return majorRoads;
    }

    public String getMainTownPopulation() {
        return mainTownPopulation;
    }

    public String getNoOfVillages() {
        return noOfVillages;
    }


    public String getPopulationDensity() {
        return populationDensity;
    }

    public String getPrivateClinicsInRadius() {
        return privateClinicsInRadius;
    }

    public String getPrivateClinicsInTown() {
        return privateClinicsInTown;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public String getServicePopulation() {
        return servicePopulation;
    }

    public String getTransportCost() {
        return transportCost;
    }


    public void setAnyMajorBusiness(String anyMajorBusiness) {
        this.anyMajorBusiness = anyMajorBusiness;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setCommunityUnits(String communityUnits) {
        this.communityUnits = communityUnits;
    }


    public void setPrivateClinicsInRadius(String privateClinicsInRadius) {
        this.privateClinicsInRadius = privateClinicsInRadius;
    }

    public void setCountyPopulation(String countyPopulation) {
        this.countyPopulation = countyPopulation;
    }

    public void setCountySupport(String countySupport) {
        this.countySupport = countySupport;
    }

    public void setMainTown(String mainTown) {
        this.mainTown = mainTown;
    }

    public void setHealtFacilities(String healtFacilities) {
        this.healtFacilities = healtFacilities;
    }

    public void setMainBanks(String mainBanks) {
        this.mainBanks = mainBanks;
    }

    public void setMainSupermarkets(String mainSupermarkets) {
        this.mainSupermarkets = mainSupermarkets;
    }

    public void setMainTownPopulation(String mainTownPopulation) {
        this.mainTownPopulation = mainTownPopulation;
    }

    public void setMajorRoads(String majorRoads) {
        this.majorRoads = majorRoads;
    }

    public void setNoOfVillages(String noOfVillages) {
        this.noOfVillages = noOfVillages;
    }

    public void setPopulationDensity(String populationDensity) {
        this.populationDensity = populationDensity;
    }

    public void setPrivateClinicsInTown(String privateClinicsInTown) {
        this.privateClinicsInTown = privateClinicsInTown;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public void setServicePopulation(String servicePopulation) {
        this.servicePopulation = servicePopulation;
    }

    public void setTransportCost(String transportCost) {
        this.transportCost = transportCost;
    }

    ///
    public String getPicture() {
        return "";
    }
    public int getColor() {
        return 1;
    }


     public void setColor(int color) {
        this.color = color;
    }


    public void setPicture(String picture) {
        this.picture = picture;
    }
}
