package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 9/13/17.
 */

class TrainingRole {
    var id: Int? = null
    lateinit var roleName: String
    var isArchived: Boolean = false
    var isReadOnly: Boolean = false
    lateinit var country: String
    var clientTime: Long? = null
    var createdBy: Int? = null

    constructor() {}

    constructor(id: Int?, roleName: String, archived: Boolean, readOnly: Boolean,
                country: String, clientTime: Long?, createdBy: Int?) {
        this.id = id
        this.roleName = roleName
        this.isArchived = archived
        this.isReadOnly = readOnly
        this.country = country
        this.clientTime = clientTime
        this.createdBy = createdBy
    }
}
