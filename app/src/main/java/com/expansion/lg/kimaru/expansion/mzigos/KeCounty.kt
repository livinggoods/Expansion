package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class KeCounty {

    var id: Int? = null
    lateinit var countyName: String
    lateinit var country: String
    var lat: Double? = null
    var lon: Double? = null
    lateinit var contactPerson: String
    lateinit var countyCode: String
    lateinit var contactPersonPhone: String
    lateinit var mainTown: String
    lateinit var countySupport: String
    var isChvActivity: Boolean = false
        internal set
    lateinit var chvActivityLevel: String
    lateinit var countyPopulation: String
    var noOfVillages: Long? = null
    var mainTownPopulation: Long? = null
    var servicePopulation: Long? = null
    var populationDensity: Long? = null
    var transportCost: Int? = null
    lateinit var majorRoads: String
    lateinit var healtFacilities: String
    lateinit var privateClinicsInTown: String
    lateinit var privateClinicsInRadius: String
    lateinit var communityUnits: String
    lateinit var mainSupermarkets: String
    lateinit var mainBanks: String
    var anyMajorBusiness: Int? = null
    lateinit var comments: String
    var isRecommended: Boolean = false
    // Get Methods


    var isSynced: Boolean = false
    var dateAdded: Long? = null
    var addedBy: Int? = null
    var isLgPresent: Boolean = false

    //boolean isRead, isImportant;
    internal var color = -1
    internal var picture = ""

    constructor() {

    }

    constructor(id: Int?, countyName: String, countyCode: String, country: String, lat: Double?,
                lon: Double?, contactPerson: String, contactPersonPhone: String, mainTown: String,
                countySupport: String, chvActivityLevel: String, countyPopulation: String,
                noOfVillages: Long?, mainTownPopulation: Long?, servicePopulation: Long?,
                populationDensity: Long?, transportCost: Int?, majorRoads: String,
                healtFacilities: String, privateClinicsInTown: String, privateClinicsInRadius: String,
                communityUnits: String, mainSupermarkets: String, mainBanks: String,
                anyMajorBusiness: Int?, comments: String, recommended: Boolean,
                dateAdded: Long?, addedBy: Int?, lgPresent: Boolean?, synced: Boolean) {

        this.id = id
        this.countyName = countyName
        this.country = country
        this.lat = lat
        this.lon = lon
        this.contactPerson = contactPerson
        this.contactPersonPhone = contactPersonPhone
        this.mainTown = mainTown
        this.countySupport = countySupport
        this.chvActivityLevel = chvActivityLevel
        this.isChvActivity = chvActivityLevel !== "0"
        this.countyPopulation = countyPopulation
        this.noOfVillages = noOfVillages
        this.mainTownPopulation = mainTownPopulation
        this.servicePopulation = servicePopulation
        this.populationDensity = populationDensity
        this.transportCost = transportCost
        this.majorRoads = majorRoads
        this.healtFacilities = healtFacilities
        this.privateClinicsInTown = privateClinicsInTown
        this.privateClinicsInRadius = privateClinicsInRadius
        this.communityUnits = communityUnits
        this.mainSupermarkets = mainSupermarkets
        this.mainBanks = mainBanks
        this.anyMajorBusiness = anyMajorBusiness
        this.comments = comments
        this.isRecommended = recommended
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.countyCode = countyCode
        this.isLgPresent = lgPresent!!
        this.isSynced = synced
    }

    ///
    fun getPicture(): String {
        return ""
    }

    fun getColor(): Int {
        return 1
    }


    fun setColor(color: Int) {
        this.color = color
    }


    fun setPicture(picture: String) {
        this.picture = picture
    }
}
