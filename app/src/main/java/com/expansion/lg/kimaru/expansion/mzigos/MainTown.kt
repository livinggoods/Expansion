package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class MainTown {

    lateinit var id: String
    // Get Methods

    lateinit var townName: String
    lateinit var country: String
    lateinit var mappingId: String
    lateinit var lat: String
    lateinit var lon: String
    lateinit var subCountyId: String
    var dateAdded: Int? = null
    var addedBy: Int? = null

    constructor() {

    }

    constructor(id: String, townName: String, country: String, mappingId: String,
                lat: String, lon: String, subCountyId: String, dateAdded: Int?, addedBy: Int?) {
        this.id = id
        this.townName = townName
        this.country = country
        this.mappingId = mappingId
        this.lat = lat
        this.lon = lon
        this.subCountyId = subCountyId
        this.dateAdded = dateAdded
        this.addedBy = addedBy
    }

}
