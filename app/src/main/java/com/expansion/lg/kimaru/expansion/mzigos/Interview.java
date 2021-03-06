package com.expansion.lg.kimaru.expansion.mzigos;

import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;

/**
 * Created by kimaru on 3/2/17.
 */

public class Interview {

    private Integer motivation, community, mentality, selling, health, investment;
    private Integer interpersonal, commitment, total, addedBy, synced, selected, readAndInterpret,
            interviewerMotivationAssessment, interviewerAgeAssessment, interviewerResidenyAssessment,
            interviewerBracAssessment, interviewerAbilityToReadAssessment, interviewerQualifyAssessment;

    private String comment, id, applicant, recruitment, country;
    Long dateAdded;
    private boolean isRead, isImportant, canJoin;
    int color = -1;
    String picture =  "";

    public Interview(){

    }

    public Interview(String id, String applicant, String recruitment, Integer motivation, Integer community,
                     Integer mentality, Integer selling, Integer health, Integer investment,
                     Integer interpersonal, Integer commitment, Integer selected, Integer addedBy,
                     Long dateAdded, Integer synced, String comment, boolean canJoin, String country,
                     Integer readAndInterpret, Integer interviewerMotivationAssessment, Integer interviewerAgeAssessment, Integer interviewerResidenyAssessment,
                     Integer interviewerBracAssessment, Integer interviewerAbilityToReadAssessment, Integer interviewerQualifyAssessment) {
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
        this.country = country;
        this.id = id;
        this.canJoin = canJoin;
        this.interviewerMotivationAssessment=interviewerMotivationAssessment;
        this.interviewerAgeAssessment=interviewerAgeAssessment;
        this.interviewerResidenyAssessment=interviewerResidenyAssessment;
        this.interviewerBracAssessment=interviewerBracAssessment;
        this.interviewerAbilityToReadAssessment=interviewerAbilityToReadAssessment;
        this.interviewerQualifyAssessment=interviewerQualifyAssessment;
        this.readAndInterpret = readAndInterpret;
        this.total = (motivation + community + mentality + selling + health + investment + interpersonal + commitment);
    }


    // Get Methods


    public Integer getInterviewerMotivationAssessment() {
        return interviewerMotivationAssessment;
    }

    public Integer getInterviewerAgeAssessment() {
        return interviewerAgeAssessment;
    }

    public Integer getInterviewerResidenyAssessment() {
        return interviewerResidenyAssessment;
    }

    public Integer getInterviewerBracAssessment() {
        return interviewerBracAssessment;
    }

    public Integer getInterviewerAbilityToReadAssessment() {
        return interviewerAbilityToReadAssessment;
    }

    public Integer getInterviewerQualifyAssessment() {
        return interviewerQualifyAssessment;
    }

    public String getApplicant() {
        return applicant;
    }

    public String getRecruitment() {
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

    public Long getDateAdded() {
        return dateAdded;
    }

    public Integer getSynced() {
        return synced;
    }

    public Integer getTotal() {
        if (!(this.total==null)){
            return this.total;
        }else{
            return this.calculateTotal();
        }

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isCanJoin() {
        return canJoin;
    }

    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }

    public String getComment() {
        return comment;
    }

    public String getId() {
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

    private Integer calculateTotal(){
        Integer interviewTotal = 0;
        Integer motivation = this.motivation == null ? 0: this.motivation;
        Integer community = this.community == null ? 0: this.community;
        Integer mentality = this.mentality == null ? 0: this.mentality;
        Integer selling  = this.selling  == null ? 0: this.selling;
        Integer health  =this.health  == null ? 0: this.health;
        Integer investment = this.investment == null ? 0: this.investment;
        Integer interpersonal  =this.interpersonal  == null ? 0: this.interpersonal;
        Integer commitment = this.commitment == null ? 0: this.commitment;
        interviewTotal = motivation +community+mentality+selling+health+investment+interpersonal+commitment;
        return interviewTotal;
    }

    public Integer getReadAndInterpret() {
        return readAndInterpret;
    }

    //Set Methods


    public void setRecruitment(String recruitment) {
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(String id) {
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

    public boolean hasPassed(){
        if (this.commitment > 1 && getTotal() > 24 && canJoin ){
            return true;
        }else{
            return false;
        }
    }

    public void setReadAndInterpret(Integer readAndInterpret) {
        this.readAndInterpret = readAndInterpret;
    }

    public void setInterviewerMotivationAssessment(Integer interviewerMotivationAssessment) {
        this.interviewerMotivationAssessment = interviewerMotivationAssessment;
    }

    public void setInterviewerAgeAssessment(Integer interviewerAgeAssessment) {
        this.interviewerAgeAssessment = interviewerAgeAssessment;
    }

    public void setInterviewerResidenyAssessment(Integer interviewerResidenyAssessment) {
        this.interviewerResidenyAssessment = interviewerResidenyAssessment;
    }

    public void setInterviewerBracAssessment(Integer interviewerBracAssessment) {
        this.interviewerBracAssessment = interviewerBracAssessment;
    }

    public void setInterviewerAbilityToReadAssessment(Integer interviewerAbilityToReadAssessment) {
        this.interviewerAbilityToReadAssessment = interviewerAbilityToReadAssessment;
    }

    public void setInterviewerQualifyAssessment(Integer interviewerQualifyAssessment) {
        this.interviewerQualifyAssessment = interviewerQualifyAssessment;
    }
}
