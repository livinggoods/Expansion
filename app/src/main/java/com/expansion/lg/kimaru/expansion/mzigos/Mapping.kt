package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class Mapping {

    lateinit var id: String
    lateinit var mappingName: String
    lateinit var subCounty: String
    lateinit var country: String
    lateinit var contactPerson: String
    lateinit var contactPersonPhone: String
    lateinit var comment: String
    lateinit var county: String
    lateinit var district: String
    // Get Methods


    lateinit var mappingRegion: String
    var addedBy: Int? = null
    var dateAdded: Long? = null
    var isSynced: Boolean = false

    internal var isRead = false
    internal var isImportant = true
    internal var color = -1
    internal var picture = ""

    constructor() {

    }

    constructor(id: String, mappingName: String, country: String, county: String, dateAdded: Long?,
                addedBy: Int?, contactPerson: String, contactPersonPhone: String,
                synced: Boolean, comment: String, district: String, subCounty: String,
                mappingRegion: String) {
        this.id = id
        this.mappingName = mappingName
        this.country = country
        this.dateAdded = dateAdded
        this.addedBy = addedBy
        this.contactPerson = contactPerson
        this.contactPersonPhone = contactPersonPhone
        this.isSynced = synced
        this.county = county
        this.comment = comment
        this.district = district
        this.subCounty = subCounty
        this.mappingRegion = mappingRegion
    }

    //
    fun getPicture(): String {
        return ""
    }

    fun getColor(): Int {
        return 1
    }

    fun isImportant(): Boolean {
        return true
    }

    fun isRead(): Boolean {
        return isRead
    }


    fun setIsRead(isRead: Boolean) {
        this.isRead = isRead
    }

    fun setIsImportant(isImportant: Boolean) {
        this.isImportant = isImportant
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun setImportant(important: Boolean) {
        isImportant = important
    }

    fun setPicture(picture: String) {
        this.picture = picture
    }

    fun setRead(read: Boolean) {
        isRead = read
    }
}
