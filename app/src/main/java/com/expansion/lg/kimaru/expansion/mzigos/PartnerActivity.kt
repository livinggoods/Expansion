package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/28/17.
 */

/**
 *
 */

class PartnerActivity {
    lateinit var id: String
    lateinit var partnerId: String
    lateinit var country: String
    lateinit var county: String
    lateinit var subcounty: String
    lateinit var parish: String
    lateinit var village: String
    lateinit var communityUnit: String
    lateinit var mappingId: String
    lateinit var comment: String
    lateinit var activities: String
    var isDoingMhealth: Boolean = false
    var isDoingIccm: Boolean = false
    var isGivingFreeDrugs: Boolean = false
    var isGivingStipend: Boolean = false
    var isSynced: Boolean = false
    var dateAdded: Long? = null
    var addedBy: Long? = null

    var color = -1

    //empty constructor
    constructor() {}

    constructor(id: String, partnerId: String, country: String, county: String, subcounty: String,
                parish: String, village: String, communityUnit: String, mappingId: String,
                comment: String, doingMhealth: Boolean, doingIccm: Boolean,
                givingFreeDrugs: Boolean, givingStipend: Boolean,
                dateAdded: Long?, addedBy: Long?, activities: String, synced: Boolean) {
        this.id = id
        this.partnerId = partnerId
        this.country = country
        this.county = county
        this.subcounty = subcounty
        this.parish = parish
        this.village = village
        this.communityUnit = communityUnit
        this.mappingId = mappingId
        this.comment = comment
        this.isDoingMhealth = doingMhealth
        this.isDoingIccm = doingIccm
        this.isGivingFreeDrugs = givingFreeDrugs
        this.isGivingStipend = givingStipend
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.activities = activities
        this.isSynced = synced
    }
}
