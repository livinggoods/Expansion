package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class Recruitment {


    lateinit var id: String
    // Get Methods
    //Set Methods
    lateinit var name: String
    lateinit var district: String
    lateinit var subcounty: String
    lateinit var division: String
    lateinit var comment: String
    lateinit var lat: String
    lateinit var lon: String
    lateinit var country: String
    lateinit var county: String
    lateinit var subCountyId: String
    lateinit var regionId: String
    var addedBy: Int? = null
    var synced: Int? = null
    var countyId: Int? = null
    var locationId: Int? = null
    var dateAdded: Long? = null

    internal var isRead: Boolean = false
    internal var isImportant: Boolean = false
    internal var color = -1
    var picture = ""

    lateinit var countyLocation: CountyLocation
    lateinit var keCounty: KeCounty

    lateinit var subCountyObj: SubCounty

    constructor() {

    }

    constructor(id: String, name: String, district: String, subcounty: String,
                division: String, lat: String, lon: String, comment: String, addedBy: Int?,
                dateAdded: Long?, synced: Int?, country: String, county: String,
                subCountyId: String, countyId: Int?, locationId: Int?, regionId: String) {
        this.name = name
        this.district = district
        this.subcounty = subcounty
        this.division = division
        this.comment = comment
        this.addedBy = addedBy
        this.dateAdded = dateAdded
        this.synced = synced
        this.lat = lat
        this.lon = lon
        this.id = id
        this.country = country
        this.county = county
        this.subCountyId = subCountyId
        this.countyId = countyId
        this.locationId = locationId
        this.regionId = regionId
    }

    fun isRead(): Boolean {
        return isRead
    }

    fun isImportant(): Boolean {
        return isImportant
    }

    fun getColor(): Int? {
        return color
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

    fun setRead(read: Boolean) {
        isRead = read
    }
}
