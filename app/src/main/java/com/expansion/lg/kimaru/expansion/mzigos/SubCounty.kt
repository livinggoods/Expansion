package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class SubCounty {

    lateinit var id: String
    // Get Methods

    lateinit var subCountyName: String
    lateinit var countyID: String
    lateinit var country: String
    lateinit var mappingId: String
    lateinit var lat: String
    lateinit var lon: String
    lateinit var contactPerson: String
    lateinit var contactPersonPhone: String
    lateinit var mainTown: String
    lateinit var countySupport: String
    lateinit var subcountySupport: String
    var isChvActivity: Boolean = false
        internal set
    lateinit var chvActivityLevel: String
    lateinit var countyPopulation: String
    lateinit var subCountyPopulation: String
    lateinit var noOfVillages: String
    lateinit var mainTownPopulation: String
    lateinit var servicePopulation: String
    lateinit var populationDensity: String
    lateinit var transportCost: String
    lateinit var majorRoads: String
    lateinit var healtFacilities: String
    lateinit var privateClinicsInTown: String
    lateinit var privateClinicsInRadius: String
    lateinit var communityUnits: String
    lateinit var mainSupermarkets: String
    lateinit var mainBanks: String
    lateinit var anyMajorBusiness: String
    lateinit var comments: String
    var isRecommended: Boolean = false
    var dateAdded: Long? = null
    var addedBy: Int? = null

    //boolean isRead, isImportant;
    internal var color = -1
    internal var picture = ""

    constructor() {

    }

    constructor(id: String, subCountyName: String, countyID: String, country: String,
                mappingId: String, lat: String, lon: String, contactPerson: String,
                contactPersonPhone: String, mainTown: String, countySupport: String,
                subcountySupport: String, chvActivityLevel: String,
                countyPopulation: String, subCountyPopulation: String, noOfVillages: String,
                mainTownPopulation: String, servicePopulation: String, populationDensity: String,
                transportCost: String, majorRoads: String, healtFacilities: String,
                privateClinicsInTown: String, privateClinicsInRadius: String, communityUnits: String,
                mainSupermarkets: String, mainBanks: String, anyMajorBusiness: String,
                comments: String, recommended: Boolean, dateAdded: Long?, addedBy: Int?) {

        this.id = id
        this.subCountyName = subCountyName
        this.countyID = countyID
        this.country = country
        this.mappingId = mappingId
        this.lat = lat
        this.lon = lon
        this.contactPerson = contactPerson
        this.contactPersonPhone = contactPersonPhone
        this.mainTown = mainTown
        this.countySupport = countySupport
        this.subcountySupport = subcountySupport
        this.chvActivityLevel = chvActivityLevel
        this.isChvActivity = chvActivityLevel !== "0"
        this.countyPopulation = countyPopulation
        this.subCountyPopulation = subCountyPopulation
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
