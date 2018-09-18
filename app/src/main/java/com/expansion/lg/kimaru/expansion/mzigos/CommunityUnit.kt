package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 *
 * Representation of the community Unit
 * subcounty, areaChiefName, ward, gps{}, noOfChVs, NumberOfVillages, distanceToBranch, transportCost
 * distanceTOMainRoad, noOfHouseholds, mohPoplationDensity, estimatedPopulationDensity, economicStatus,
 * distanceTONearestHealthFacility, actLevels, actPrice, privateFacilityForAct, mrdtLevels,  mrdtPrice,
 * privateFacilityForMrdt, presenceOfEstates, presenceOfFactories, presenceOfHostels, noOfDistibutors,
 * traderMarket, largeSupermarket, ngosFreeDrugs, ngosICCM, ngoMHealth, connectivity
 */


class CommunityUnit {

    /*
    * Representation of the community Unit
     * ,
     * distanceTOMainRoad, noOfHouseholds, mohPoplationDensity, estimatedPopulationDensity, economicStatus,
     * distanceTONearestHealthFacility, actLevels, actPrice, privateFacilityForAct, mrdtLevels,  mrdtPrice,
     * privateFacilityForMrdt, presenceOfEstates, presenceOfFactories, presenceOfHostels, noOfDistibutors,
     * traderMarket, largeSupermarket, ngosFreeDrugs, ngosICCM, ngoMHealth, connectivity
     */


    // Some community units are split into 2 and they share some data


    lateinit var id: String
    lateinit var communityUnitName: String
    lateinit var mappingId: String
    lateinit var country: String
    lateinit var subCountyId: String
    lateinit var linkFacilityId: String
    lateinit var areaChiefName: String
    lateinit var ward: String
    // Get Methods


    lateinit var comment: String
    var lat: Double? = null
    var lon: Double? = null
    var dateAdded: Long = 0
    var addedBy: Long = 0
    ////
    var numberOfChvs: Long = 0
    var householdPerChv: Long = 0
    var numberOfVillages: Long = 0
    var distanceToBranch: Long = 0
    var transportCost: Long = 0
    var distanceTOMainRoad: Long = 0
    var noOfHouseholds: Long = 0
    var mohPoplationDensity: Long = 0
    var estimatedPopulationDensity: Long = 0
    var distanceTONearestHealthFacility: Long = 0
    var actLevels: Long = 0
    var actPrice: Long = 0
    var mrdtLevels: Long = 0
    var mrdtPrice: Long = 0
    var noOfDistibutors: Long = 0
    lateinit var economicStatus: String
    lateinit var areaChiefPhone: String //lower, middle or upper
    lateinit var privateFacilityForAct: String
    lateinit var privateFacilityForMrdt: String
    lateinit var nameOfNgoDoingIccm: String
    lateinit var nameOfNgoDoingMhealth: String
    internal var presenceOfFactories: Long? = null
    var populationAsPerChief: Long? = null
    var chvsHouseholdsAsPerChief: Long? = null

    var isChvsTrained: Boolean = false
    var isPresenceOfEstates: Boolean = false
    var isPresenceOfHostels: Boolean = false
    var isTraderMarket: Boolean = false
    var isLargeSupermarket: Boolean = false
    var isNgosGivingFreeDrugs: Boolean = false
    var isNgoDoingIccm: Boolean = false
    var isNgoDoingMhealth: Boolean = false

    internal var isRead: Boolean = false
    internal var isImportant: Boolean = false
    internal var color = -1
    internal var picture = ""
    val isPresenceOfFactories: Boolean
        get() = this.presenceOfFactories!!.compareTo(0L) > 0


    constructor() {

    }

    constructor(id: String, communityUnitName: String, mappingId: String, lat: Double?, lon: Double?,
                country: String, subCountyId: String, linkFacilityId: String, areaChiefName: String,
                areaChiefPhone: String, ward: String, economicStatus: String, privateFacilityForAct: String,
                privateFacilityForMrdt: String,
                nameOfNgoDoingIccm: String, nameOfNgoDoingMhealth: String, dateAdded: Long, addedBy: Long,
                numberOfChvs: Long, householdPerChv: Long, numberOfVillages: Long,
                distanceToBranch: Long, transportCost: Long, distanceToMainRoad: Long,
                noOfHouseholds: Long, mohPoplationDensity: Long, estimatedPopulationDensity: Long,
                distanceTONearestHealthFacility: Long, actLevels: Long, actPrice: Long,
                mrdtLevels: Long, mrdtPrice: Long, noOfDistibutors: Long, chvsTrained: Boolean,
                presenceOfEstates: Boolean, presenceOfFactories: Long?,
                presenceOfHostels: Boolean, traderMarket: Boolean, largeSupermarket: Boolean,
                ngosGivingFreeDrugs: Boolean, ngoDoingIccm: Boolean, ngoDoingMhealth: Boolean,
                populationAsPerChief: Long?, chvsHouseholdsAsPerChief: Long?, comment: String) {
        this.id = id
        this.communityUnitName = communityUnitName
        this.mappingId = mappingId
        this.lat = lat
        this.lon = lon
        this.country = country
        this.subCountyId = subCountyId
        this.linkFacilityId = linkFacilityId
        this.areaChiefName = areaChiefName
        this.ward = ward
        this.economicStatus = economicStatus
        this.privateFacilityForAct = privateFacilityForAct
        this.privateFacilityForMrdt = privateFacilityForMrdt
        this.nameOfNgoDoingIccm = nameOfNgoDoingIccm
        this.nameOfNgoDoingMhealth = nameOfNgoDoingMhealth
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.numberOfChvs = numberOfChvs
        this.householdPerChv = householdPerChv
        this.numberOfVillages = numberOfVillages
        this.distanceToBranch = distanceToBranch
        this.transportCost = transportCost
        this.distanceTOMainRoad = distanceToMainRoad
        this.noOfHouseholds = noOfHouseholds
        this.mohPoplationDensity = mohPoplationDensity
        this.estimatedPopulationDensity = estimatedPopulationDensity
        this.distanceTONearestHealthFacility = distanceTONearestHealthFacility
        this.actLevels = actLevels
        this.actPrice = actPrice
        this.mrdtLevels = mrdtLevels
        this.mrdtPrice = mrdtPrice
        this.noOfDistibutors = noOfDistibutors
        this.isChvsTrained = chvsTrained
        this.isPresenceOfEstates = presenceOfEstates
        this.presenceOfFactories = presenceOfFactories
        this.isPresenceOfHostels = presenceOfHostels
        this.isTraderMarket = traderMarket
        this.isLargeSupermarket = largeSupermarket
        this.isNgosGivingFreeDrugs = ngosGivingFreeDrugs
        this.isNgoDoingIccm = ngoDoingIccm
        this.isNgoDoingMhealth = ngoDoingMhealth
        this.areaChiefPhone = areaChiefPhone
        this.populationAsPerChief = populationAsPerChief
        this.chvsHouseholdsAsPerChief = chvsHouseholdsAsPerChief
        this.comment = comment

    }

    fun presenceOfFactories(): Long? {
        return presenceOfFactories
    }

    fun setPresenceOfFactories(presenceOfFactories: Long?) {
        this.presenceOfFactories = presenceOfFactories
    }

    fun getPicture(): String {
        return ""
    }

    fun getColor(): Int {
        return 1
    }

    fun isImportant(): Boolean {
        return true
    }

    fun isRead(): Boolean {
        return isRead
    }


    fun setIsRead(isRead: Boolean) {
        this.isRead = isRead
    }

    fun setIsImportant(isImportant: Boolean) {
        this.isImportant = isImportant
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun setImportant(important: Boolean) {
        isImportant = important
    }

    fun setPicture(picture: String) {
        this.picture = picture
    }

    fun setRead(read: Boolean) {
        isRead = read
    }
}
