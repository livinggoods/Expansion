package com.expansion.lg.kimaru.expansion.mzigos

import android.test.ServiceTestCase

/**
 * Created by kimaru on 6/9/17.
 */

class Mobilization {
    // SET

    lateinit var id: String
    lateinit var name: String
    lateinit var mappingId: String
    lateinit var country: String
    lateinit var comment: String
    // GET


    lateinit var county: String
    lateinit var district: String
    lateinit var subCounty: String
    lateinit var parish: String
    var addedBy: Int? = null
    var dateAdded: Long? = null
    var synced: Boolean? = null

    constructor() {}

    constructor(id: String, name: String, mappingId: String, country: String, comment: String,
                county: String, district: String, subCounty: String, parish: String,
                addedBy: Int?, dateAdded: Long?, synced: Boolean?) {
        this.id = id
        this.name = name
        this.mappingId = mappingId
        this.country = country
        this.comment = comment
        this.county = county
        this.district = district
        this.subCounty = subCounty
        this.parish = parish
        this.addedBy = addedBy
        this.dateAdded = dateAdded
        this.synced = synced
    }
}
