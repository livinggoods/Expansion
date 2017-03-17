package com.expansion.lg.kimaru.expansion.dbhelpers;

/**
 * Created by kimaru on 3/2/17.
 */

public class Exam {

    Integer Id, applicant, recruitment, math, english, personality, addedBy, dateAdded, synced;
    String comment;
    String picture="";
    int color = -1;
    Boolean read = false;

    public Exam(){

    }

    public Exam(Integer applicant, Integer math, Integer recruitment, Integer personality,
                Integer english, Integer addedBy, Integer dateAdded, Integer synced, String comment) {
        this.applicant = applicant;
        this.recruitment = recruitment;
        this.math = math;
        this.english = english;
        this.personality = personality;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
        this.synced = synced;
        this.comment = comment;
    }


    // Get Methods
    public Integer getApplicant() {
        return applicant;
    }
    public Integer getRecruitment() {
        return recruitment;
    }
    public Integer getMath() {
        return math;
    }
    public Integer getEnglish() {
        return english;
    }
    public Integer getAddedBy() {
        return addedBy;
    }
    public Integer getDateAdded() {
        return dateAdded;
    }
    public Integer getSynced() {
        return synced;
    }
    public Integer getPersonality() { return  personality; }

    public String getComment() {
        return comment;
    }


    public Integer getId() {
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

    public void setApplicant(Integer applicant) {
        this.applicant = applicant;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }

    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public void setEnglish(Integer english) {
        this.english = english;
    }

    public void setMath(Integer math) {
        this.math = math;
    }

    public void setPersonality(Integer personality) {
        this.personality = personality;
    }

    public void setRecruitment(Integer recruitment) {
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

    public void setId(Integer Id) {
        this.Id = Id;
    }
}
