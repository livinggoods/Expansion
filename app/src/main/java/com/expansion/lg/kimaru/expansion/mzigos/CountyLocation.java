package com.expansion.lg.kimaru.expansion.mzigos;

import android.support.annotation.Nullable;

/**
 * Created by kimaru on 3/28/17.
 */

public class CountyLocation {
    String adminName;
    String name;
    int archived;
    String code;
    Integer id;
    String lat;
    String country;
    String lon;
    String meta;
    int parent;
    String polygon;

    //empty constructor
    public CountyLocation(){}

    public CountyLocation(String adminName, String name, String country, int archived, String code, @Nullable  Integer id, String lat,
                          String lon, String meta, int parent, String polygon){
        this.adminName = adminName;
        this.name =  name;
        this.archived = archived;
        this.code = code;
        this.id = id;
        this.lat = lat;
        this.country = country;
        this.lon = lon;
        this.meta = meta;
        this.parent = parent;
        this.polygon = polygon;
    }

    // Set


    public void setName(String name) {
        this.name = name;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //Get

    public int getArchived() {
        return archived;
    }

    public Integer getId() {
        return id;
    }

    public int getParent() {
        return parent;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getCode() {
        return code;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getMeta() {
        return meta;
    }

    public String getPolygon() {
        return polygon;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }
}
