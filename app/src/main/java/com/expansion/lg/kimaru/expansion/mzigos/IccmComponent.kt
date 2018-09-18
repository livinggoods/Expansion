package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 8/18/17.
 */

class IccmComponent {
    ///set

    var id: Int? = null
    lateinit var componentName: String
    lateinit  var comment: String
    lateinit  var status: String
    lateinit   var dateAdded: String
    var addedBy: Long? = null
    var clientTime: Long? = null
    var isArchived: Boolean = false

    constructor() {}

    constructor(id: Int?, componentName: String, comment: String, status: String,
                dateAdded: String, addedBy: Long?, clientTime: Long?, archived: Boolean) {
        this.id = id
        this.componentName = componentName
        this.comment = comment
        this.status = status
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.clientTime = clientTime
        this.isArchived = archived
    }
}
