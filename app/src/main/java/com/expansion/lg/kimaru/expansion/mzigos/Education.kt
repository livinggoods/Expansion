package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class Education {

    // Get Methods

    var id: Int? = null
    var hierachy: Int? = null
    lateinit var levelName: String
    lateinit var levelType: String
    lateinit var country: String

    constructor() {

    }

    constructor(id: Int?, levelName: String, levelType: String, hierachy: Int?, country: String) {
        this.id = id
        this.hierachy = hierachy
        this.levelName = levelName
        this.levelType = levelType
        this.country = country
    }
}
