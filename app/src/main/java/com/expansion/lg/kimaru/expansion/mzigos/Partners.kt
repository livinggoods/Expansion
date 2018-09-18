package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/28/17.
 */

/**
 *
 */

class Partners {
    lateinit var partnerID: String
    lateinit var partnerName: String
    lateinit var contactPerson: String
    lateinit var contactPersonPhone: String
    lateinit var parent: String
    lateinit var mappingId: String
    lateinit var country: String
    lateinit var comment: String
    var isSynced: Boolean = false
    var isArchived: Boolean = false
    var dateAdded: Long? = null
    var addedBy: Long? = null

    var color = -1

    //empty constructor
    constructor() {}

    constructor(partnerID: String, partnerName: String, contactPerson: String,
                contactPersonPhone: String, parent: String, mappingId: String,
                country: String, comment: String, synced: Boolean, archived: Boolean,
                dateAdded: Long?, addedBy: Long?) {
        this.partnerID = partnerID
        this.partnerName = partnerName
        this.contactPerson = contactPerson
        this.contactPersonPhone = contactPersonPhone
        this.parent = parent
        this.mappingId = mappingId
        this.country = country
        this.comment = comment
        this.isSynced = synced
        this.isArchived = archived
        this.dateAdded = dateAdded
        this.addedBy = addedBy
    }
}
