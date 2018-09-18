package com.expansion.lg.kimaru.expansion.mzigos

import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable

/**
 * Created by kimaru on 3/2/17.
 */

class Interview {

    var motivation: Int? = null
    var community: Int? = null
    var mentality: Int? = null
    var selling: Int? = null
    var health: Int? = null
    var investment: Int? = null
    var interpersonal: Int? = null
    var commitment: Int? = null
    internal var total: Int? = null
    var addedBy: Int? = null
    var synced: Int? = null
    var selected: Int? = null
    var readAndInterpret: Int? = null
    // Get Methods


    var interviewerMotivationAssessment: Int? = null
    var interviewerAgeAssessment: Int? = null
    var interviewerResidenyAssessment: Int? = null
    var interviewerBracAssessment: Int? = null
    var interviewerAbilityToReadAssessment: Int? = null
    var interviewerQualifyAssessment: Int? = null
    lateinit var comment: String
    lateinit var id: String
    lateinit var applicant: String
    //Set Methods


    lateinit var recruitment: String
    lateinit var country: String
    var dateAdded: Long? = null
    var isRead: Boolean = false
    var isImportant: Boolean = false
    var isCanJoin: Boolean = false
    var color = -1
    var picture = ""

    constructor() {

    }

    constructor(id: String, applicant: String, recruitment: String, motivation: Int?, community: Int?,
                mentality: Int?, selling: Int?, health: Int?, investment: Int?,
                interpersonal: Int?, commitment: Int?, selected: Int?, addedBy: Int?,
                dateAdded: Long?, synced: Int?, comment: String, canJoin: Boolean, country: String,
                readAndInterpret: Int?, interviewerMotivationAssessment: Int?, interviewerAgeAssessment: Int?, interviewerResidenyAssessment: Int?,
                interviewerBracAssessment: Int?, interviewerAbilityToReadAssessment: Int?, interviewerQualifyAssessment: Int?) {
        this.applicant = applicant
        this.recruitment = recruitment
        this.motivation = motivation
        this.community = community
        this.mentality = mentality
        this.selling = selling
        this.health = health
        this.investment = investment
        this.interpersonal = interpersonal
        this.commitment = commitment
        this.selected = selected
        this.addedBy = addedBy
        this.dateAdded = dateAdded
        this.synced = synced
        this.comment = comment
        this.country = country
        this.id = id
        this.isCanJoin = canJoin
        this.interviewerMotivationAssessment = interviewerMotivationAssessment
        this.interviewerAgeAssessment = interviewerAgeAssessment
        this.interviewerResidenyAssessment = interviewerResidenyAssessment
        this.interviewerBracAssessment = interviewerBracAssessment
        this.interviewerAbilityToReadAssessment = interviewerAbilityToReadAssessment
        this.interviewerQualifyAssessment = interviewerQualifyAssessment
        this.readAndInterpret = readAndInterpret
        this.total = motivation!! + community!! + mentality!! + selling!! + health!! + investment!! + interpersonal!! + commitment!!
    }

    fun getTotal(): Int? {
        return if (this.total != null) {
            this.total
        } else {
            this.calculateTotal()
        }

    }

    private fun calculateTotal(): Int? {
        var interviewTotal: Int? = 0
        val motivation = if (this.motivation == null) 0 else this.motivation
        val community = if (this.community == null) 0 else this.community
        val mentality = if (this.mentality == null) 0 else this.mentality
        val selling = if (this.selling == null) 0 else this.selling
        val health = if (this.health == null) 0 else this.health
        val investment = if (this.investment == null) 0 else this.investment
        val interpersonal = if (this.interpersonal == null) 0 else this.interpersonal
        val commitment = if (this.commitment == null) 0 else this.commitment
        interviewTotal = motivation!! + community!! + mentality!! + selling!! + health!! + investment!! + interpersonal!! + commitment!!
        return interviewTotal
    }

    fun hasPassed(): Boolean {
        return this.commitment!! > 1 && getTotal()!! > 24 && isCanJoin
    }
}
