package com.expansion.lg.kimaru.expansion.dbhelpers;

/**
 * Created by kimaru on 3/2/17.
 */

public class Interview {

    Integer id, applicant, recruitment, motivation, community, mentality, selling, health, investment;
    Integer interpersonal, commitment, total, selected, addedBy, dateAdded, synced;
    String comment;
    boolean isRead, isImportant;
    int color = -1;
    String picture =  "";

    public Interview(){

    }

    public Interview(Integer applicant, Integer recruitment, Integer motivation, Integer community,
                     Integer mentality, Integer selling, Integer health, Integer investment,
                     Integer interpersonal, Integer commitment, Integer selected, Integer addedBy,
                     Integer dateAdded, Integer synced, String comment) {
        this.applicant = applicant;
        this.recruitment = recruitment;
        this.motivation = motivation;
        this.community = community;
        this.mentality = mentality;
        this.selling = selling;
        this.health = health;
        this.investment = investment;
        this.interpersonal = interpersonal;
        this.commitment = commitment;
        this.selected = selected;
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

    public Integer getMotivation() {
        return motivation;
    }

    public Integer getCommunity() {
        return community;
    }

    public Integer getMentality() {
        return mentality;
    }

    public Integer getSelling() {
        return selling;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getInvestment() {
        return investment;
    }

    public Integer getInterpersonal() {
        return interpersonal;
    }

    public Integer getCommitment() {
        return commitment;
    }

    public Integer getSelected() {
        return selected;
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

    public Integer getTotal() {
        return total;
    }
    public String getComment() {
        return comment;
    }

    public Integer getId() {
        return id;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public boolean isRead() {
        return isRead;
    }

    public int getColor() {
        return color;
    }

    public String getPicture() {
        return picture;
    }


    //Set Methods


    public void setRecruitment(Integer recruitment) {
        this.recruitment = recruitment;
    }

    public void setCommitment(Integer commitment) {
        this.commitment = commitment;
    }

    public void setCommunity(Integer community) {
        this.community = community;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setInterpersonal(Integer interpersonal) {
        this.interpersonal = interpersonal;
    }

    public void setInvestment(Integer investment) {
        this.investment = investment;
    }

    public void setMentality(Integer mentality) {
        this.mentality = mentality;
    }

    public void setMotivation(Integer motivation) {
        this.motivation = motivation;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public void setSelling(Integer selling) {
        this.selling = selling;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
