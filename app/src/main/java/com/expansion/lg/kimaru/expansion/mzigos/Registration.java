package com.expansion.lg.kimaru.expansion.mzigos;

import java.util.Date;

/**
 * Created by kimaru on 3/11/17.
 */

public class Registration {

    String recruitment, name, phone, gender, district, subcounty, division, village, mark, langs, education;
    String id, occupation, comment, picture, country, chewName, chewNumber, ward, cuName, linkFacility;
    String otherTrainings, referralName, referralTitle, referralPhone, parish;
    Integer readEnglish, brac, bracChp, community, addedBy, proceed, synced;
    Long dob, dateMoved, dateAdded, noOfHouseholds, recruitmentTransportCost, transportCostToBranch;
    boolean chv, gokTrained, vht, accounts;
    int color = -1;
    Boolean read = false;

    public Registration(){

    }

    public Registration(String id, String mName, String mPhone, String mGender, String mDistrict,
                        String mSubcounty, String mDivision, String mVillage, String mMark,
                        String mLangs, String mEducation, String mOccupation, String mComment,
                        Long mDob, Integer mReadEnglish, String mRecruitment, String country,
                        Long mDateMoved, Integer mBrac, Integer mBracChp, Integer mCommunity,
                        Integer mAddedBy, Integer mProceed, Long mDateAdded, Integer mSynced,
                        String chewName, String chewNumber, String ward, String cuName,
                        String linkFacility, long noOfHouseholds, boolean isChv, boolean gokTrained,
                        String otherTrainings, String referralName, String referralTitle,
                        String referralPhone, boolean vht, boolean accounts, String parish,
                        Long recruitmentTransportCost, Long transportCostToBranch) {
        this.name = mName;
        this.phone = mPhone;
        this.recruitment = mRecruitment;
        this.gender = mGender;
        this.district = mDistrict;
        this.subcounty = mSubcounty;
        this.division = mDivision;
        this.village = mVillage;
        this.country = country;
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
        this.chewName = chewName;
        this.chewNumber = chewNumber;
        this.ward = ward;
        this.cuName = cuName;
        this.linkFacility = linkFacility;
        this.noOfHouseholds = noOfHouseholds;
        this.chv = isChv;
        this.gokTrained = gokTrained;
        this.otherTrainings = otherTrainings;
        this.referralName = referralName;
        this.referralTitle = referralTitle;
        this.referralPhone = referralPhone;
        this.vht = vht;
        this.accounts = accounts;
        this.parish = parish;
        this.recruitmentTransportCost = recruitmentTransportCost;
        this.transportCostToBranch = transportCostToBranch;
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
    public String getChewName() {
        return chewName;
    }
    public String getChewNumber() {
        return chewNumber;
    }
    public String getWard() {
        return ward;
    }
    public String getCuName() {
        return cuName;
    }
    public String getLinkFacility() {
        return linkFacility;
    }
    public boolean isChv() {
        return chv;
    }
    public boolean isGokTrained() {
        return gokTrained;
    }
    public Long getNoOfHouseholds() {
        return noOfHouseholds;
    }
    public String getOtherTrainings() {
        return otherTrainings;
    }

    public String getReferralName() {
        return referralName;
    }

    public String getReferralTitle() {
        return referralTitle;
    }

    public String getParish() {
        return parish;
    }

    public String getReferralPhone() {
        return referralPhone;
    }

    public boolean isAccounts() {
        return accounts;
    }

    public boolean isVht() {
        return vht;
    }

    public void setAccounts(boolean accounts) {
        this.accounts = accounts;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public void setReferralName(String referralName) {
        this.referralName = referralName;
    }

    public void setReferralPhone(String referralPhone) {
        this.referralPhone = referralPhone;
    }

    public void setReferralTitle(String referralTitle) {
        this.referralTitle = referralTitle;
    }

    public void setVht(boolean vht) {
        this.vht = vht;
    }

    public Long getRecruitmentTransportCost() {
        return recruitmentTransportCost;
    }

    public Long getTransportCostToBranch() {
        return transportCostToBranch;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    public Boolean getRead() {
        return read;
    }

    public void setChewName(String chewName) {
        this.chewName = chewName;
    }

    public void setChewNumber(String chewNumber) {
        this.chewNumber = chewNumber;
    }

    public void setChv(boolean chv) {
        this.chv = chv;
    }

    public void setCuName(String cuName) {
        this.cuName = cuName;
    }

    public void setGokTrained(boolean gokTrained) {
        this.gokTrained = gokTrained;
    }

    public void setLinkFacility(String linkFacility) {
        this.linkFacility = linkFacility;
    }

    public void setNoOfHouseholds(Long noOfHouseholds) {
        this.noOfHouseholds = noOfHouseholds;
    }

    public void setOtherTrainings(String otherTrainings) {
        this.otherTrainings = otherTrainings;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public Long getAge(){
        Long currentDate =  new Date().getTime();
        Long epochYear = 31556926000L;
        Long yearEpoch = currentDate - this.dob;
        Long ageYear = yearEpoch / epochYear;
        return ageYear;
    }

    public void setRecruitmentTransportCost(Long recruitmentTransportCost) {
        this.recruitmentTransportCost = recruitmentTransportCost;
    }

    public void setTransportCostToBranch(Long transportCostToBranch) {
        this.transportCostToBranch = transportCostToBranch;
    }

    public boolean hasPassed(){
        Long age = this.getAge();
        if ( 30 < age || age > 55){
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