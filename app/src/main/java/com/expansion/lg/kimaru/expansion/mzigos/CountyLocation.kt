package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/28/17.
 */

class CountyLocation {
    lateinit var adminName: String
    // Set


    lateinit var name: String
    //Get

    var archived: Int = 0
    lateinit var code: String
    var id: Int? = null
    lateinit var lat: String
    lateinit var country: String
    lateinit var lon: String
    lateinit var meta: String
    var parent: Int = 0
    lateinit var polygon: String

    //empty constructor
    constructor() {}

    constructor(adminName: String, name: String, country: String, archived: Int, code: String, id: Int?, lat: String,
                lon: String, meta: String, parent: Int, polygon: String) {
        this.adminName = adminName
        this.name = name
        this.archived = archived
        this.code = code
        this.id = id
        this.lat = lat
        this.country = country
        this.lon = lon
        this.meta = meta
        this.parent = parent
        this.polygon = polygon
    }
}
