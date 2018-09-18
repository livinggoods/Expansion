package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 9/4/17.
 */

class Training {
    lateinit var id: String
    lateinit var trainingName: String
    lateinit var country: String
    lateinit var county: String
    lateinit var subCounty: String
    lateinit var ward: String
    lateinit var district: String
    lateinit var parish: String
    lateinit var location: String
    lateinit var recruitment: String
    lateinit var comment: String
    var createdBy: Int? = null
    var status: Int? = null
    var lat: Double? = null
    var lon: Double? = null
    var clientTime: Long? = null
    var dateCompleted: Long? = null
    var dateCommenced: Long? = null

    var color = -1


    constructor() {}

    constructor(id: String, trainingName: String, country: String, county: String,
                subCounty: String, ward: String, district: String, parish: String, location: String,
                createdBy: Int?, status: Int?, clientTime: Long?, lat: Double?, lon: Double?,
                recruitment: String, comment: String, dateCommenced: Long?, dateCompleted: Long?) {
        this.id = id
        this.trainingName = trainingName
        this.country = country
        this.county = county
        this.subCounty = subCounty
        this.ward = ward
        this.district = district
        this.parish = parish
        this.location = location
        this.createdBy = createdBy
        this.status = status
        this.clientTime = clientTime
        this.lat = lat
        this.lon = lon
        this.recruitment = recruitment
        this.comment = comment
        this.dateCommenced = dateCommenced
        this.dateCompleted = dateCompleted

    }
}
