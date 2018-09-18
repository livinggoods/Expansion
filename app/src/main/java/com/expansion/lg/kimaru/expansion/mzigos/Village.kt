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


class Village {


    //SET METHODS
    lateinit var id: String
    lateinit    var villageName: String
    lateinit     var mappingId: String
         var lat: Double? = null
    var lon: Double? = null
    lateinit    var country: String
    lateinit     var district: String
    lateinit     var county: String
    lateinit    var subCountyId: String
    lateinit    var parish: String
    lateinit     var communityUnit: String
    lateinit   var ward: String
    lateinit    var linkFacilityId: String
    lateinit   var areaChiefName: String
    lateinit    var areaChiefPhone: String
    lateinit    var economicStatus: String
    lateinit   var nameOfNgoDoingMhealth: String
    lateinit    var privateFacilityForAct: String
    lateinit    var nameOfNgoDoingIccm: String
    lateinit     var privateFacilityForMrdt: String
    lateinit   var comment: String
    lateinit    var distributorsInTheArea: String

    //GET METHODS

    var presenceOfHostels: Boolean? = null
    var presenceOfEstates: Boolean? = null
    var presenceOfDistributors: Boolean? = null
    var chvsTrained: Boolean? = null
    var actStock: Boolean? = null
    var traderMarket: Boolean? = null
    var largeSupermarket: Boolean? = null
    var ngosGivingFreeDrugs: Boolean? = null
    var ngoDoingIccm: Boolean? = null
    var ngoDoingMhealth: Boolean? = null

    var dateAdded: Long? = null
    var distanceToBranch: Long? = null
    var transportCost: Long? = null
    var distanceToNearestHealthFacility: Long? = null
    var actPrice: Long? = null
    var actLevels: Long? = null
    var mrdtLevels: Long? = null
    var mrdtPrice: Long? = null
    var distanceToMainRoad: Long? = null
    var noOfHouseholds: Long? = null
    var mohPoplationDensity: Long? = null
    var estimatedPopulationDensity: Long? = null
    var addedBy: Int? = null
    var numberOfFactories: Int? = null
    var mtnSignalStrength: Int? = null
    var safaricomSignalStrength: Int? = null
    var orangeSignalStrength: Int? = null
    var airtelSignalStrength: Int? = null

    var isRead: Boolean = false
    var isImportant: Boolean = false
    var isSynced: Boolean = false
    var isBracOperating: Boolean = false
    var color = -1
    var picture = ""


    constructor() {

    }

    constructor(id: String, villageName: String, mappingId: String, lat: Double?, lon: Double?,
                country: String, district: String, county: String, subCountyId: String, parish: String,
                communityUnit: String, ward: String, linkFacilityId: String,
                areaChiefName: String, areaChiefPhone: String, distanceToBranch: Long?, transportCost: Long?,
                distanceToMainRoad: Long?, noOfHouseholds: Long?, mohPoplationDensity: Long?,
                estimatedPopulationDensity: Long?, economicStatus: String, distanceToNearestHealthFacility: Long?,
                actLevels: Long?, actPrice: Long?, mrdtLevels: Long?, mrdtPrice: Long?, presenceOfHostels: Boolean?,
                presenceOfEstates: Boolean?, numberOfFactories: Int?, presenceOfDistributors: Boolean?,
                distributorsInTheArea: String, traderMarket: Boolean?, largeSupermarket: Boolean?, ngosGivingFreeDrugs: Boolean?,
                ngoDoingIccm: Boolean?, ngoDoingMhealth: Boolean?, nameOfNgoDoingIccm: String,
                nameOfNgoDoingMhealth: String, privateFacilityForAct: String, privateFacilityForMrdt: String,
                dateAdded: Long?, addedBy: Int?, comment: String, chvsTrained: Boolean?, synced: Boolean,
                bracOperating: Boolean, mtnSignalStrength: Int?, safaricomSignalStrength: Int?,
                orangeSignalStrength: Int?, airtelSignalStrength: Int?, actStock: Boolean?) {

        this.id = id
        this.villageName = villageName
        this.mappingId = mappingId
        this.lat = lat
        this.lon = lon
        this.country = country
        this.district = district
        this.county = county
        this.subCountyId = subCountyId
        this.parish = parish
        this.communityUnit = communityUnit
        this.ward = ward
        this.linkFacilityId = linkFacilityId
        this.areaChiefName = areaChiefName
        this.areaChiefPhone = areaChiefPhone
        this.distanceToBranch = distanceToBranch
        this.transportCost = transportCost
        this.distanceToMainRoad = distanceToMainRoad
        this.noOfHouseholds = noOfHouseholds
        this.mohPoplationDensity = mohPoplationDensity
        this.estimatedPopulationDensity = estimatedPopulationDensity
        this.economicStatus = economicStatus
        this.distanceToNearestHealthFacility = distanceToNearestHealthFacility
        this.actLevels = actLevels
        this.actPrice = actPrice
        this.mrdtLevels = mrdtLevels
        this.mrdtPrice = mrdtPrice
        this.presenceOfHostels = presenceOfHostels
        this.presenceOfEstates = presenceOfEstates
        this.numberOfFactories = numberOfFactories
        this.presenceOfDistributors = presenceOfDistributors
        this.distributorsInTheArea = distributorsInTheArea
        this.traderMarket = traderMarket
        this.largeSupermarket = largeSupermarket
        this.ngosGivingFreeDrugs = ngosGivingFreeDrugs
        this.ngoDoingIccm = ngoDoingIccm
        this.ngoDoingMhealth = ngoDoingMhealth
        this.nameOfNgoDoingIccm = nameOfNgoDoingIccm
        this.nameOfNgoDoingMhealth = nameOfNgoDoingMhealth
        this.privateFacilityForAct = privateFacilityForAct
        this.privateFacilityForMrdt = privateFacilityForMrdt
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.comment = comment
        this.chvsTrained = chvsTrained
        this.isSynced = synced
        this.isBracOperating = bracOperating
        this.mtnSignalStrength = mtnSignalStrength
        this.safaricomSignalStrength = safaricomSignalStrength
        this.orangeSignalStrength = orangeSignalStrength
        this.airtelSignalStrength = airtelSignalStrength
        this.actStock = actStock
    }
}
