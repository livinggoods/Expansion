package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 9/13/17.
 */

class TrainingTrainee {

    lateinit var id: String
    lateinit var registrationId: String
    lateinit var trainingId: String
    lateinit var country: String
    lateinit var chpCode: String
    var classId: Int? = null
    var addedBy: Int? = null
    var branch: Int? = null
    var cohort: Int? = null
    var clientTime: Long? = null

    constructor() {}

    constructor(id: String, registrationId: String, trainingId: String, country: String,
                chpCode: String, classId: Int?, addedBy: Int?, branch: Int?,
                cohort: Int?, clientTime: Long?) {
        this.id = id
        this.registrationId = registrationId
        this.trainingId = trainingId
        this.country = country
        this.chpCode = chpCode
        this.classId = classId
        this.addedBy = addedBy
        this.branch = branch
        this.cohort = cohort
        this.clientTime = clientTime
    }
}
