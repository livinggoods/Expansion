package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 9/13/17.
 */

class TrainingTrainer {
    var id: Int? = null
    var trainerId: Int? = null
    var createdby: Int? = null
    var trainingRoleId: Int? = null
    lateinit var trainingId: String
    lateinit var classId: String
    lateinit var country: String
    var clientTime: Long? = null
    var isArchived: Boolean = false

    constructor() {}

    constructor(id: Int?, trainerId: Int?, createdby: Int?,
                trainingRoleId: Int?, trainingId: String, classId: String,
                country: String, clientTime: Long?, archived: Boolean) {
        this.id = id
        this.trainerId = trainerId
        this.createdby = createdby
        this.trainingRoleId = trainingRoleId
        this.trainingId = trainingId
        this.classId = classId
        this.country = country
        this.clientTime = clientTime
        this.isArchived = archived
    }
}
