package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 5/12/17.
 */

public class ChewReferral {
    String id, name, phone, title, country, recruitmentId;
    Integer synced;
    int color = -1;

    public ChewReferral(){}

    public ChewReferral(String id, String name, String phone, String title, String country,
                        String recruitmentId, Integer synced){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.title = title;
        this.country = country;
        this.recruitmentId = recruitmentId;
        this.synced = synced;
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
}
