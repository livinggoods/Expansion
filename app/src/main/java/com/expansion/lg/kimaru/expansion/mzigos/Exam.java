package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class Exam {

    Double math, english, personality;
    Integer addedBy, synced;
    Long dateAdded;
    String comment, Id, applicant, recruitment, country;
    String picture="";
    int color = -1;
    Boolean read = false;

    public Exam(){

    }

    public Exam(String id, String applicant, Double math, String recruitment, Double personality,
                Double english, Integer addedBy, Long dateAdded, Integer synced, String comment,
                String country) {
        this.applicant = applicant;
        this.recruitment = recruitment;
        this.math = math;
        this.english = english;
        this.personality = personality;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
        this.synced = synced;
        this.comment = comment;
        this.Id = id;
        this.country = country;
    }


    // Get Methods
    public String getApplicant() {
        return applicant;
    }
    public String getRecruitment() {
        return recruitment;
    }
    public Double getMath() {
        return math;
    }
    public Double getEnglish() {
        return english;
    }
    public Integer getAddedBy() {
        return addedBy;
    }
    public Long getDateAdded() {
        return dateAdded;
    }
    public Integer getSynced() {
        return synced;
    }
    public Double getPersonality() { return  personality; }

    public String getComment() {
        return comment;
    }

    public String getCountry() {
        return country;
    }

    public String getId() {
        return Id;
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
        return read;
    }
    //Set Methods

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public void setEnglish(Double english) {
        this.english = english;
    }

    public void setMath(Double math) {
        this.math = math;
    }

    public void setPersonality(Double personality) {
        this.personality = personality;
    }

    public void setRecruitment(String recruitment) {
        this.recruitment = recruitment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPicture(String picture){ this.picture = picture;}

    public void setColor(int color) {
        this.color = color;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public boolean hasPassed(){

        if (this.math == 0 || this.english == 0 || this.personality ==0){
            return false;
        }else{
            if (this.country == "KE"){
                if ((this.math + this.english + this.personality) < 20){
                    return false;
                }else{
                    return true;
                }
            }else {
                if ((this.math + this.english + this.personality) < 30){
                    return false;
                }else{
                    return true;
                }
            }

        }
    }
}
