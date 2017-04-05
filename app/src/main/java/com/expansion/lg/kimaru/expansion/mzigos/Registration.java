package com.expansion.lg.kimaru.expansion.mzigos;

import java.util.Date;

/**
 * Created by kimaru on 3/11/17.
 */

public class Registration {

    String recruitment, name, phone, gender, district, subcounty, division, village, mark, langs, education;
    String id, occupation, comment, picture;
    Integer readEnglish, brac, bracChp, community, addedBy, proceed, synced;
    Long dob, dateMoved, dateAdded;
    int color = -1;
    Boolean read = false;

    public Registration(){

    }

    public Registration(String id, String mName, String mPhone, String mGender, String mDistrict,
                        String mSubcounty, String mDivision, String mVillage, String mMark,
                        String mLangs, String mEducation, String mOccupation, String mComment,
                        Long mDob, Integer mReadEnglish, String mRecruitment,
                        Long mDateMoved, Integer mBrac, Integer mBracChp, Integer mCommunity,
                        Integer mAddedBy, Integer mProceed, Long mDateAdded, Integer mSynced) {
        this.name = mName;
        this.phone = mPhone;
        this.recruitment = mRecruitment;
        this.gender = mGender;
        this.district = mDistrict;
        this.subcounty = mSubcounty;
        this.division = mDivision;
        this.village = mVillage;
        this.mark = mMark;
        this.langs = mLangs;
        this.education = mEducation;
        this.occupation = mOccupation;
        this.comment = mComment;
        this.dob = mDob;
        this.readEnglish = mReadEnglish;
        this.dateMoved = mDateMoved;
        this.brac = mBrac;
        this.bracChp = mBracChp;
        this.community = mCommunity;
        this.addedBy = mAddedBy;
        this.proceed = mProceed;
        this.dateAdded = mDateAdded;
        this.synced = mSynced;
        this.id = id;
    }


    // Get Methods
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getGender() {
        return gender;
    }
    public String getRecruitment() {
        return recruitment;
    }
    public String getDistrict() {
        return district;
    }
    public String getSubcounty() {
        return subcounty;
    }
    public String getDivision() {
        return division;
    }
    public String getVillage() {
        return village;
    }
    public String getMark() {
        return mark;
    }
    public String getLangs() {
        return langs;
    }
    public String getEducation() {
        return education;
    }
    public String getOccupation() {
        return occupation;
    }
    public String getComment() {
        return comment;
    }
    public Long getDob() {
        return dob;
    }
    public Integer getReadEnglish() {
        return readEnglish;
    }
    public Long getDateMoved() {
        return dateMoved;
    }

    public Integer getBrac() {
        return brac;
    }

    public Integer getBracChp() {
        return bracChp;
    }

    public Integer getCommunity() {
        return community;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public Integer getProceed() {
        return proceed;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

 // I added these methods
    public Integer getSynced() {
        return synced;
    }
    public String getId() {
        return id;
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
    public String getMessage(){return "This is a great Message";}

    //Set Methods
    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setRecruitment(String recruitment) {
        this.recruitment = recruitment;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public void setLangs(String langs) {
        this.langs = langs;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public void setBrac(Integer brac) {
        this.brac = brac;
    }

    public void setBracChp(Integer bracChp) {
        this.bracChp = bracChp;
    }

    public void setCommunity(Integer community) {
        this.community = community;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateMoved(Long dateMoved) {
        this.dateMoved = dateMoved;
    }

    public void setProceed(Integer proceed) {
        this.proceed = proceed;
    }

    public void setReadEnglish(Integer readEnglish) {
        this.readEnglish = readEnglish;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }
    public void setPicture(String picture){ this.picture = picture;}

    public void setColor(int color) {
        this.color = color;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean hasPassed(){
        // age should be between 30 and 55
        Long currentDate =  new Date().getTime();
        Long epochYear = 31556926000L;
        Long age = this.dob - currentDate;
        if (30 * epochYear< age || age > 55 * epochYear){
            return false;
        }else if (this.readEnglish == 0) {
            return false;
        }else if (this.dateMoved < 2) {
            return false;
        }else if (brac == 1 && bracChp == 1){
            return false;
        }else if (Integer.valueOf(this.education) < 7 || Integer.valueOf(this.education) > 12){
            return false;
        }else {
            return true;
        }
    }

}