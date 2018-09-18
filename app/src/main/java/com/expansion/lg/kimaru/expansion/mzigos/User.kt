package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class User {

    // Get Methods

    var id: Int? = null
    lateinit var email: String
    lateinit var  username: String
    lateinit var password: String
    lateinit var name: String
    lateinit var country: String

    constructor() {

    }

    constructor(id: Int?, email: String, username: String, password: String, name: String, country: String) {
        this.id = id
        this.email = email
        this.username = username
        this.password = password
        this.name = name
        this.country = country
    }
}
