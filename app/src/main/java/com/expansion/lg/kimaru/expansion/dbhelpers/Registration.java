package com.expansion.lg.kimaru.expansion.dbhelpers;

/**
 * Created by kimaru on 3/11/17.
 */

public class Registration {

    String name, phone,gender, district, subcounty, division, village, mark, langs, education;
    String occupation, comment, picture;
    Integer Id, dob, readEnglish, recruitment, dateMoved, brac, bracChp, community, addedBy, proceed, dateAdded, synced;
    int color = -1;
    Boolean read = false;

    public Registration(){

    }

    public Registration(String mName, String mPhone, String mGender, String mDistrict,
                        String mSubcounty, String mDivision, String mVillage, String mMark,
                        String mLangs, String mEducation, String mOccupation, String mComment,
                        Integer mDob, Integer mReadEnglish, Integer mRecruitment,
                        Integer mDateMoved, Integer mBrac, Integer mBracChp, Integer mCommunity,
                        Integer mAddedBy, Integer mProceed, Integer mDateAdded, Integer mSynced) {
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
    public Integer getRecruitment() {
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
    public Integer getDob() {
        return dob;
    }
    public Integer getReadEnglish() {
        return readEnglish;
    }
    public Integer getDateMoved() {
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

    public Integer getDateAdded() {
        return dateAdded;
    }

 // I added these methods
    public Integer getSynced() {
        return synced;
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
    public void setRecruitment(Integer recruitment) {
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

    public void setDob(Integer dob) {
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

    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateMoved(Integer dateMoved) {
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

    public void setId(Integer Id) {
        this.Id = Id;
    }

}