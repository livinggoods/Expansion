package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 9/13/17.
 */

class TrainingClass {
    var id: Int = 0
    var createdBy: Int = 0
    lateinit var trainingId: String
    lateinit var className: String
    lateinit var country: String
    var clientTime: Long? = null
    var isArchived: Boolean = false

    constructor() {}

    constructor(id: Int, createdBy: Int, trainingId: String, className: String,
                country: String, clientTime: Long?, archived: Boolean) {
        this.id = id
        this.createdBy = createdBy
        this.trainingId = trainingId
        this.className = className
        this.country = country
        this.clientTime = clientTime
        this.isArchived = archived
    }
}
