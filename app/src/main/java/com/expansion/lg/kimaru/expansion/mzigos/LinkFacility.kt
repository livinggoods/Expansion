package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class LinkFacility {

    lateinit  var id: String
    lateinit  var facilityName: String
    lateinit  var mappingId: String
    lateinit  var country: String
    lateinit  var subCountyId: String
    lateinit   var mflCode: String
    lateinit  var county: String
    // Get Methods


    lateinit  var parish: String
    var addedBy: Int? = null
    var lat: Double? = null
    var lon: Double? = null
    internal var actLevels: Long? = null
    internal var mrdtLevels: Long? = null
    var dateAdded: Long? = null

    var picture = ""
    var color = -1
    var isRead: Boolean? = false


    constructor() {

    }

    constructor(id: String, facilityName: String, country: String, mappingId: String, lat: Double?,
                lon: Double?, subCountyId: String, dateAdded: Long?, addedBy: Int?,
                actLevels: Long, mrdtLevels: Long, mflCode: String, county: String, parish: String) {
        this.id = id
        this.facilityName = facilityName
        this.mappingId = mappingId
        this.lat = lat
        this.lon = lon
        this.subCountyId = subCountyId
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.mrdtLevels = mrdtLevels
        this.actLevels = actLevels
        this.country = country
        this.mflCode = mflCode
        this.county = county
        this.parish = parish

    }

    fun getActLevels(): Long {
        return actLevels!!
    }

    fun getMrdtLevels(): Long {
        return mrdtLevels!!
    }

    fun setActLevels(actLevels: Long) {
        this.actLevels = actLevels
    }

    fun setMrdtLevels(mrdtLevels: Long) {
        this.mrdtLevels = mrdtLevels
    }

    fun setActLevels(actLevels: Long?) {
        this.actLevels = actLevels
    }

    fun setMrdtLevels(mrdtLevels: Long?) {
        this.mrdtLevels = mrdtLevels
    }
}
