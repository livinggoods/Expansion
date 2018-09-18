package com.expansion.lg.kimaru.expansion.mzigos

/**
 * Created by kimaru on 3/2/17.
 */

class Exam {

    var math: Double? = null
    var english: Double? = null
    var personality: Double? = null
    var addedBy: Int? = null
    var synced: Int? = null
    var dateAdded: Long? = null
    lateinit  var comment: String
    lateinit  var id: String
    // Get Methods
    //Set Methods

    lateinit   var applicant: String
    lateinit   var recruitment: String
    lateinit   var country: String
    internal var picture = ""
    internal var color = -1
    internal var read: Boolean? = false
    val isImportant: Boolean
        get() = true
    var isRead: Boolean
        get() = read!!
        set(read) {
            this.read = read
        }

    constructor() {

    }

    constructor(id: String, applicant: String, math: Double?, recruitment: String, personality: Double?,
                english: Double?, addedBy: Int?, dateAdded: Long?, synced: Int?, comment: String,
                country: String) {
        this.applicant = applicant
        this.recruitment = recruitment
        this.math = math
        this.english = english
        this.personality = personality
        this.addedBy = addedBy
        this.dateAdded = dateAdded
        this.synced = synced
        this.comment = comment
        this.id = id
        this.country = country
    }


    fun getPicture(): String {
        return ""
    }

    fun getColor(): Int {
        return 1
    }

    fun setPicture(picture: String) {
        this.picture = picture
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun hasPassed(): Boolean {

        return if (this.math!! == 0.0 || this.english!! == 0.0 || this.personality!! == 0.0) {
            false
        } else {
            if (this.country.equals("KE", ignoreCase = true)) {
                this.math!! + this.english!! + this.personality!! >= 10
            } else {
                this.math!! + this.english!! + this.personality!! >= 30
            }

        }
    }
}
