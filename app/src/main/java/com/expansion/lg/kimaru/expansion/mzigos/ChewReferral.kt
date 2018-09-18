package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 5/12/17.
 */

class ChewReferral {
    lateinit var id: String
    lateinit var name: String
    lateinit var phone: String
    lateinit var title: String
    lateinit var country: String
    lateinit var recruitmentId: String
    //Some mapping Details that we shall use, esp for UG
    lateinit var county: String
    lateinit var district: String
    lateinit var subCounty: String
    lateinit var communityUnit: String
    lateinit var village: String
    lateinit var mapping: String
    lateinit  var lat: String
    lateinit  var lon: String
    lateinit  var mobilization: String
    var synced: Int? = null
    var color = -1

    constructor() {}

    constructor(id: String, name: String, phone: String, title: String, country: String,
                recruitmentId: String, synced: Int?, county: String, district: String,
                subCounty: String, communityUnit: String, village: String, mapping: String,
                lat: String, lon: String, mobilization: String) {
        this.id = id
        this.name = name
        this.phone = phone
        this.title = title
        this.country = country
        this.recruitmentId = recruitmentId
        this.synced = synced
        this.county = county
        this.district = district
        this.subCounty = subCounty
        this.communityUnit = communityUnit
        this.village = village
        this.mapping = mapping
        this.lat = lat
        this.lon = lon
        this.mobilization = mobilization
    }
}
