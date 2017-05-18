package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 5/12/17.
 */

public class ChewReferral {
    String id, name, phone, title, country, recruitmentId;
    //Some mapping Details that we shall use, esp for UG
    String county, district, subCounty, communityUnit, village, mapping, lat, lon, mobilization;
    Integer synced;
    int color = -1;

    public ChewReferral(){}

    public ChewReferral(String id, String name, String phone, String title, String country,
                        String recruitmentId, Integer synced, String county, String district,
                        String subCounty, String communityUnit, String village, String mapping,
                        String lat, String lon, String mobilization){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.title = title;
        this.country = country;
        this.recruitmentId = recruitmentId;
        this.synced = synced;
        this.county = county;
        this.district = district;
        this.subCounty = subCounty;
        this.communityUnit = communityUnit;
        this.village = village;
        this.mapping = mapping;
        this.lat = lat;
        this.lon = lon;
        this.mobilization = mobilization;
    }

    public String getCommunityUnit() {
        return communityUnit;
    }

    public String getCounty() {
        return county;
    }

    public String getDistrict() {
        return district;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getMapping() {
        return mapping;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public String getVillage() {
        return village;
    }

    public String getMobilization() {
        return mobilization;
    }

    public Integer getSynced() {
        return synced;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecruitmentId() {
        return recruitmentId;
    }

    public void setRecruitmentId(String recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setCommunityUnit(String communityUnit) {
        this.communityUnit = communityUnit;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setMobilization(String mobilization) {
        this.mobilization = mobilization;
    }
}
