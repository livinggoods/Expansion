package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 9/4/17.
 */

public class Training {
    String id, trainingName, country, county, subCounty, ward, district, parish, location,
            recruitment, comment;
    Integer createdBy, status;
    Double lat, lon;
    Long clientTime, dateCompleted, dateCommenced;

    int color = -1;


    public Training() {
    }

    public Training(String id, String trainingName, String country, String county,
                    String subCounty, String ward, String district, String parish, String location,
                    Integer createdBy, Integer status, Long clientTime, Double lat, Double lon,
                    String recruitment, String comment, Long dateCommenced , Long dateCompleted) {
        this.id = id;
        this.trainingName = trainingName;
        this.country = country;
        this.county = county;
        this.subCounty = subCounty;
        this.ward = ward;
        this.district = district;
        this.parish = parish;
        this.location = location;
        this.createdBy = createdBy;
        this.status = status;
        this.clientTime = clientTime;
        this.lat = lat;
        this.lon = lon;
        this.recruitment = recruitment;
        this.comment = comment;
        this.dateCommenced = dateCommenced;
        this.dateCompleted = dateCompleted;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecruitment() {
        return recruitment;
    }

    public void setRecruitment(String recruitment) {
        this.recruitment = recruitment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer created_by) {
        this.createdBy = created_by;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getClientTime() {
        return clientTime;
    }

    public void setClientTime(Long clientTime) {
        this.clientTime = clientTime;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Long getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Long dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Long getDateCommenced() {
        return dateCommenced;
    }

    public void setDateCommenced(Long dateCommenced) {
        this.dateCommenced = dateCommenced;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
