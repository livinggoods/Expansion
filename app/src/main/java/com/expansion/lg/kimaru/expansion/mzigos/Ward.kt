package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 7/21/17.
 */

class Ward {
    //GET


    //SET

    lateinit var id: String
    lateinit var name: String
    lateinit var subCounty: String
    var archived: Int? = null
    var county: Int? = null

    constructor() {}

    constructor(id: String, name: String, subCounty: String, archived: Int?, county: Int?) {
        this.id = id
        this.name = name
        this.subCounty = subCounty
        this.archived = archived
        this.county = county
    }
}
