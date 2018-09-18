package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 5/23/17.
 */

class Parish {
    // get methods
    lateinit var id: String
    lateinit var name: String
    lateinit var contactPerson: String
    lateinit var contactPersonPhone: String
    lateinit var parent: String
    lateinit var mapping: String
    lateinit var country: String
    lateinit var comment: String
    var dateAdded: Long? = null
    var addedBy: Int? = null
    var synced: Int? = null
    var color = -1

    constructor() {}

    constructor(id: String, name: String, contactPerson: String, contactPersonPhone: String, parent: String, mapping: String, dateAdded: Long?,
                addedBy: Int?, synced: Int?, country: String, comment: String) {
        this.id = id
        this.name = name
        this.contactPerson = contactPerson
        this.contactPersonPhone = contactPersonPhone
        this.parent = parent
        this.mapping = mapping
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.synced = synced
        this.country = country
        this.comment = comment
    }
}
