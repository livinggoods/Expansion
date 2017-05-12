package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 5/12/17.
 */

public class ChewReferral {
    String uuid, name, phone, title, country, recruitmentId;

    public ChewReferral(){}

    public ChewReferral(String uuid, String name, String phone, String title, String country,
                        String recruitmentId){
        this.uuid = uuid;
        this.name = name;
        this.phone = phone;
        this.title = title;
        this.country = country;
        this.recruitmentId = recruitmentId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
}
