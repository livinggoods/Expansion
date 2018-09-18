package com.expansion.lg.kimaru.expansion.mzigos

import java.util.Date

/**
 * Created by kimaru on 3/11/17.
 */

class Registration {

    lateinit var recruitment: String
    // Get Methods
    //Set Methods
    lateinit var name: String
    lateinit var phone: String
    lateinit var gender: String
    lateinit var district: String
    lateinit var subcounty: String
    lateinit var division: String
    lateinit var village: String
    lateinit var mark: String
    lateinit var langs: String
    lateinit var education: String
    lateinit var id: String
    lateinit var occupation: String
    lateinit var comment: String
    lateinit internal var picture: String
    lateinit var country: String
    lateinit var chewName: String
    lateinit var chewNumber: String
    lateinit var ward: String
    lateinit var cuName: String
    lateinit var linkFacility: String
    lateinit var otherTrainings: String
    lateinit var referralName: String
    lateinit var referralTitle: String
    lateinit var referralPhone: String
    lateinit var parish: String
    lateinit var chewUuid: String
    lateinit var maritalStatus: String
    lateinit var subCountyId: String
    lateinit var parishId: String
    lateinit var villageId: String
    var readEnglish: Int? = null
    var brac: Int? = null
    var bracChp: Int? = null
    var community: Int? = null
    var addedBy: Int? = null
    var proceed: Int? = null
    // I added these methods
    var synced: Int? = null
    var dob: Long? = null
    var dateMoved: Long? = null
    var dateAdded: Long? = null
    var noOfHouseholds: Long? = null
    var recruitmentTransportCost: Long? = null
    var transportCostToBranch: Long? = null
    var isChv: Boolean = false
    var isGokTrained: Boolean = false
    var isVht: Boolean = false
    var isAccounts: Boolean = false
    internal var color = -1
    internal var read: Boolean? = false
    val isImportant: Boolean
        get() = true
    val isRead: Boolean
        get() = read!!
    val message: String
        get() = "This is a great Message"

    val age: Long?
        get() {
            val currentDate = Date().time
            val epochYear = 31556926000L
            val yearEpoch = currentDate - this.dob!!
            return yearEpoch / epochYear
        }

    constructor() {

    }

    constructor(id: String, mName: String, mPhone: String, mGender: String, mDistrict: String,
                mSubcounty: String, mDivision: String, mVillage: String, mMark: String,
                mLangs: String, mEducation: String, mOccupation: String, mComment: String,
                mDob: Long?, mReadEnglish: Int?, mRecruitment: String, country: String,
                mDateMoved: Long?, mBrac: Int?, mBracChp: Int?, mCommunity: Int?,
                mAddedBy: Int?, mProceed: Int?, mDateAdded: Long?, mSynced: Int?,
                chewName: String, chewNumber: String, ward: String, cuName: String,
                linkFacility: String, noOfHouseholds: Long, isChv: Boolean, gokTrained: Boolean,
                otherTrainings: String, referralName: String, referralTitle: String,
                referralPhone: String, vht: Boolean, accounts: Boolean, parish: String,
                recruitmentTransportCost: Long?, transportCostToBranch: Long?, chewUuid: String,
                maritalStatus: String, subCountyId: String, parishId: String, villageId: String) {
        this.name = mName
        this.phone = mPhone
        this.recruitment = mRecruitment
        this.gender = mGender
        this.district = mDistrict
        this.subcounty = mSubcounty
        this.subCountyId = subCountyId
        this.division = mDivision
        this.village = mVillage
        this.country = country
        this.mark = mMark
        this.langs = mLangs
        this.education = mEducation
        this.occupation = mOccupation
        this.comment = mComment
        this.dob = mDob
        this.readEnglish = mReadEnglish
        this.dateMoved = mDateMoved
        this.brac = mBrac
        this.bracChp = mBracChp
        this.community = mCommunity
        this.addedBy = mAddedBy
        this.proceed = mProceed
        this.dateAdded = mDateAdded
        this.synced = mSynced
        this.id = id
        this.chewName = chewName
        this.chewNumber = chewNumber
        this.ward = ward
        this.cuName = cuName
        this.linkFacility = linkFacility
        this.noOfHouseholds = noOfHouseholds
        this.isChv = isChv
        this.isGokTrained = gokTrained
        this.otherTrainings = otherTrainings
        this.referralName = referralName
        this.referralTitle = referralTitle
        this.referralPhone = referralPhone
        this.isVht = vht
        this.isAccounts = accounts
        this.parish = parish
        this.recruitmentTransportCost = recruitmentTransportCost
        this.transportCostToBranch = transportCostToBranch
        this.chewUuid = chewUuid
        this.maritalStatus = maritalStatus
        this.subCountyId = subCountyId
        this.villageId = villageId
        this.parishId = parishId
    }

    fun getPicture(): String {
        return ""
    }

    fun getColor(): Int {
        return 1
    }

    fun setPicture(picture: String) {
        this.picture = picture
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun setRead(read: Boolean) {
        this.read = read
    }

    fun getRead(): Boolean? {
        return read
    }

    fun setRead(read: Boolean?) {
        this.read = read
    }

    //    public boolean hasPassed(){
    //        Integer age = getAge().intValue();
    //        if ( age < 30 || age > 55){
    //            return false;
    //        }else if (this.readEnglish.equals(0)) {
    //            return false;
    //        }else if (this.dateMoved < 2) {
    //            return false;
    //        }else if (brac.equals(1) && bracChp.equals(1)){
    //            return false;
    //        }else if (this.country.equalsIgnoreCase("UG")){
    //            // if the education is less than P7
    //            if (Integer.valueOf(this.education) < 2 || Integer.valueOf(this.education) > 7 ){
    //                return false;
    //            }else {
    //                return true;
    //            }
    //
    //        }else {
    //            if (Integer.valueOf(this.education) < 10 || Integer.valueOf(this.education) > 14 ){
    //                return false;
    //            }else {
    //                return true;
    //            }
    //        }
    //    }

    fun hasPassed(): Boolean {
        val age = age!!.toInt()
        return if (age < 30 || age > 55) {
            false
        } else if (this.readEnglish == 0) {
            false
        } else if (this.dateMoved!! < 2) {
            false
        } else if (this.bracChp == 1) {
            false
        } else if (this.country.equals("UG", ignoreCase = true)) {
            // if the education is less than P7
            !(Integer.valueOf(this.education) < 2 || Integer.valueOf(this.education) > 7)
        } else {
            !(Integer.valueOf(this.education) < 10 || Integer.valueOf(this.education) > 14)
        }
    }

}