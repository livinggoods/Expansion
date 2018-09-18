package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class InterviewTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    internal var columns = arrayOf(ID, APPLICANT, RECRUITMENT, MOTIVATION, COMMUNITY, MENTALITY, SELLING, HEALTH, INVESTMENT, INTERPERSONAL, TOTAL, SELECTED, ADDED_BY, COMMENT, COMMITMENT, DATE_ADDED, SYNCED, CANJOIN, COUNTRY, READ_AND_INTERPRET_PASSAGE, INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION, INTERVIEWER_ASSESSMENT_AGE, INTERVIEWER_ASSESSMENT_RESIDENCY, INTERVIEWER_ASSESSMENT_BRAC_CHP, INTERVIEWER_ASSESSMENT_ABILITY_TO_READ, INTERVIEWER_ASSESSMENT_QUALIFIES)

    val interviewData: List<Interview>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val interviewList = ArrayList<Interview>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val interview = cursorToInterview(cursor)
                interviewList.add(interview)
                cursor.moveToNext()
            }
            cursor.close()
            return interviewList
        }
    val interviewDataCursor: Cursor
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            db.close()
            return cursor
        }

    val interviewJson: JSONObject
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val results = JSONObject()
            val resultSet = JSONArray()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val totalColumns = cursor.columnCount
                val rowObject = JSONObject()
                for (i in 0 until totalColumns) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            if (cursor.getString(i) != null) {
                                rowObject.put(cursor.getColumnName(i), cursor.getString(i))
                            } else {
                                rowObject.put(cursor.getColumnName(i), "")
                            }
                        } catch (e: Exception) {
                        }

                    }
                }
                resultSet.put(rowObject)
                try {
                    results.put(JSON_ROOT, resultSet)
                } catch (e: JSONException) {
                }

                cursor.moveToNext()
            }
            cursor.close()
            db.close()
            return results
        }

    val interviewsToSyncAsJson: JSONObject
        get() {
            val db = readableDatabase
            val whereClause = "$SYNCED = ?"
            val whereArgs = arrayOf("0")
            val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
            val results = JSONObject()
            val resultSet = JSONArray()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val totalColumns = cursor.columnCount
                val rowObject = JSONObject()
                for (i in 0 until totalColumns) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            if (cursor.getString(i) != null) {
                                rowObject.put(cursor.getColumnName(i), cursor.getString(i))
                            } else {
                                rowObject.put(cursor.getColumnName(i), "")
                            }
                        } catch (e: Exception) {
                        }

                    }
                }
                resultSet.put(rowObject)
                try {
                    results.put(JSON_ROOT, resultSet)
                } catch (e: JSONException) {
                }

                cursor.moveToNext()
            }
            cursor.close()
            db.close()
            return results
        }

    init {
        if (!isFieldExist(READ_AND_INTERPRET_PASSAGE)) {
            this.addReadAndinterpretField()
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION)) {
            this.addInterviewerGoodMotivation()
        }

        if (!isFieldExist(INTERVIEWER_ASSESSMENT_AGE)) {
            this.addInterviewerAge()
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_RESIDENCY)) {
            this.addInterviewerResidency()
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_BRAC_CHP)) {
            this.addInterviewerBracChp()
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ)) {
            this.addInterviewerAbilityToRead()
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_QUALIFIES)) {
            this.addInterviewerQualifies()
        }
    }

    fun addReadAndinterpretField() {
        val db = readableDatabase
        db.execSQL(ADD_READ_FIELD)
    }

    fun addInterviewerGoodMotivation() {
        val db = readableDatabase
        db.execSQL(ADD_INTERVIEWER_MOTIVATION)
    }

    fun addInterviewerAge() {
        val db = readableDatabase
        db.execSQL(ADD_INTERVIEWER_AGE_FIELD)
    }

    fun addInterviewerResidency() {
        val db = readableDatabase
        db.execSQL(ADD_INTERVIEWER_RESIDENCY_FIELD)
    }

    fun addInterviewerBracChp() {
        val db = readableDatabase
        db.execSQL(ADD_INTERVIEWER_BRAC_CHP_FIELD)
    }

    fun addInterviewerAbilityToRead() {
        val db = readableDatabase
        db.execSQL(ADD_INTERVIEWER_ABILITY_TO_READ)
    }

    fun addInterviewerQualifies() {
        val db = readableDatabase
        db.execSQL(ADD_INTERVIEWER_QUALIFIES)
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_DATABASE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addData(interview: Interview): Long {

        val db = writableDatabase

        val cv = ContentValues()
        cv.put(ID, interview.id)
        cv.put(APPLICANT, interview.applicant)
        cv.put(RECRUITMENT, interview.recruitment)
        cv.put(MOTIVATION, interview.motivation)
        cv.put(COMMUNITY, interview.community)
        cv.put(MENTALITY, interview.mentality)
        cv.put(COUNTRY, interview.country)
        cv.put(SELLING, interview.selling)
        cv.put(HEALTH, interview.health)
        cv.put(INVESTMENT, interview.investment)
        cv.put(INTERPERSONAL, interview.interpersonal)
        cv.put(TOTAL, interview.total)
        cv.put(SELECTED, interview.selected)
        cv.put(CANJOIN, if (interview.isCanJoin) 1 else 0)
        cv.put(ADDED_BY, interview.addedBy)
        cv.put(COMMENT, interview.comment)
        cv.put(COMMITMENT, interview.commitment)
        cv.put(DATE_ADDED, interview.dateAdded)
        cv.put(SYNCED, interview.synced)
        cv.put(READ_AND_INTERPRET_PASSAGE, interview.readAndInterpret)
        cv.put(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION, interview.interviewerMotivationAssessment)
        cv.put(INTERVIEWER_ASSESSMENT_AGE, interview.interviewerAgeAssessment)
        cv.put(INTERVIEWER_ASSESSMENT_RESIDENCY, interview.interviewerResidenyAssessment)
        cv.put(INTERVIEWER_ASSESSMENT_BRAC_CHP, interview.interviewerBracAssessment)
        cv.put(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ, interview.interviewerAbilityToReadAssessment)
        cv.put(INTERVIEWER_ASSESSMENT_QUALIFIES, interview.interviewerQualifyAssessment)

        val id: Long
        if (isExist(interview)) {
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + interview.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }

        db.close()
        return id

    }

    fun isExist(interview: Interview): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + interview.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    fun getInterviewByRegistrationId(registrationUuid: String): Interview? {
        val db = readableDatabase
        val whereClause = "$APPLICANT = ?"
        val whereArgs = arrayOf(registrationUuid)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {

            val interview = cursorToInterview(cursor)
            cursor.close()
            return interview
        }
    }

    fun getInterviewById(id: String): Interview? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val interview = cursorToInterview(cursor)
            cursor.close()
            return interview
        }
    }

    //getInterviewById

    fun getInterviewsByRecruitment(recruitment: Recruitment): List<Interview> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$RECRUITMENT = ?"
        val whereArgs = arrayOf(recruitment.id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val interviewList = ArrayList<Interview>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val interview = cursorToInterview(cursor)
            interviewList.add(interview)
            cursor.moveToNext()
        }
        db.close()
        return interviewList
    }

    fun fromJson(jsonObject: JSONObject) {
        val interview = Interview()
        try {
            interview.id = jsonObject.getString(InterviewTable.ID)
            interview.applicant = jsonObject.getString(InterviewTable.APPLICANT)
            interview.recruitment = jsonObject.getString(InterviewTable.RECRUITMENT)
            interview.motivation = jsonObject.getInt(InterviewTable.MOTIVATION)
            interview.community = jsonObject.getInt(InterviewTable.COMMUNITY)
            interview.mentality = jsonObject.getInt(InterviewTable.MENTALITY)
            interview.selling = jsonObject.getInt(InterviewTable.SELLING)
            interview.health = jsonObject.getInt(InterviewTable.HEALTH)
            interview.investment = jsonObject.getInt(InterviewTable.INVESTMENT)
            interview.country = jsonObject.getString(InterviewTable.COUNTRY)
            interview.interpersonal = jsonObject.getInt(InterviewTable.INTERPERSONAL)
            interview.selected = jsonObject.getInt(SELECTED)
            interview.addedBy = jsonObject.getInt(InterviewTable.ADDED_BY)
            interview.comment = jsonObject.getString(InterviewTable.COMMENT)
            interview.commitment = jsonObject.getInt(InterviewTable.COMMITMENT)
            interview.dateAdded = jsonObject.getLong(InterviewTable.DATE_ADDED)
            interview.synced = jsonObject.getInt(InterviewTable.SYNCED)
            interview.isCanJoin = jsonObject.getInt(InterviewTable.CANJOIN) == 1
            interview.readAndInterpret = jsonObject.getInt(READ_AND_INTERPRET_PASSAGE)
            interview.interviewerMotivationAssessment = jsonObject.getInt(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION)
            interview.interviewerAgeAssessment = jsonObject.getInt(INTERVIEWER_ASSESSMENT_AGE)
            interview.interviewerResidenyAssessment = jsonObject.getInt(INTERVIEWER_ASSESSMENT_RESIDENCY)
            interview.interviewerBracAssessment = jsonObject.getInt(INTERVIEWER_ASSESSMENT_BRAC_CHP)
            interview.interviewerAbilityToReadAssessment = jsonObject.getInt(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ)
            interview.interviewerQualifyAssessment = jsonObject.getInt(INTERVIEWER_ASSESSMENT_QUALIFIES)
            // add six fields
            this.addData(interview)
        } catch (e: Exception) {
        }

    }

    private fun cursorToInterview(cursor: Cursor): Interview {
        val interview = Interview()
        interview.id = cursor.getString(cursor.getColumnIndex(ID))
        interview.applicant = cursor.getString(cursor.getColumnIndex(APPLICANT))
        interview.recruitment = cursor.getString(cursor.getColumnIndex(RECRUITMENT))
        interview.motivation = cursor.getInt(cursor.getColumnIndex(MOTIVATION))
        interview.community = cursor.getInt(cursor.getColumnIndex(COMMUNITY))
        interview.mentality = cursor.getInt(cursor.getColumnIndex(MENTALITY))
        interview.selling = cursor.getInt(cursor.getColumnIndex(SELLING))
        interview.health = cursor.getInt(cursor.getColumnIndex(HEALTH))
        interview.investment = cursor.getInt(cursor.getColumnIndex(INVESTMENT))
        interview.interpersonal = cursor.getInt(cursor.getColumnIndex(INTERPERSONAL))
        interview.selected = cursor.getInt(cursor.getColumnIndex(SELECTED))
        interview.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
        interview.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
        interview.commitment = cursor.getInt(cursor.getColumnIndex(COMMITMENT))
        interview.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
        interview.synced = cursor.getInt(cursor.getColumnIndex(SYNCED))
        interview.isCanJoin = cursor.getInt(cursor.getColumnIndex(CANJOIN)) == 1
        interview.readAndInterpret = cursor.getInt(cursor.getColumnIndex(READ_AND_INTERPRET_PASSAGE))
        interview.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
        interview.readAndInterpret = cursor.getInt(cursor.getColumnIndex(READ_AND_INTERPRET_PASSAGE))
        interview.interviewerMotivationAssessment = cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION))
        interview.interviewerAgeAssessment = cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_AGE))
        interview.interviewerResidenyAssessment = cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_RESIDENCY))
        interview.interviewerBracAssessment = cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_BRAC_CHP))
        interview.interviewerAbilityToReadAssessment = cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ))
        interview.interviewerQualifyAssessment = cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_QUALIFIES))
        return interview
    }

    fun isFieldExist(fieldName: String): Boolean {
        val db = readableDatabase
        var isExist = false
        var res: Cursor? = null
        try {
            res = db.rawQuery("Select * from $TABLE_NAME limit 1", null)
            val colIndex = res!!.getColumnIndex(fieldName)
            if (colIndex != -1) {
                isExist = true
            } else {
                Log.d("Tremap", "The col $fieldName is NOT found")
            }
        } catch (e: Exception) {
            Log.d("Tremap", "Error getting  $fieldName")
        } finally {
            try {
                res?.close()
            } catch (e1: Exception) {
            }

        }
        return isExist
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "interview"
        val JSON_ROOT = "interviews"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "


        //applicant, recruitment, motivation, community, mentality, selling, health, investment,
        // interpersonal, commitment, total, selected, addedBy,
        // dateAdded, synced
        val ID = "id"
        val APPLICANT = "applicant"
        val RECRUITMENT = "recruitment"
        val MOTIVATION = "motivation"
        val COMMUNITY = "community"
        val MENTALITY = "mentality"
        val COUNTRY = "country"
        val SELLING = "selling"
        val HEALTH = "health"
        val INVESTMENT = "investment"
        val INTERPERSONAL = "interpersonal"
        val CANJOIN = "canjoin"
        val COMMITMENT = "commitment"
        val TOTAL = "total"
        val SELECTED = "selected"
        val SYNCED = "synced"
        val ADDED_BY = "added_by"
        val COMMENT = "comment"
        val DATE_ADDED = "client_time"
        val INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION = "interviewer_motivation"
        val INTERVIEWER_ASSESSMENT_AGE = "interviewer_age"
        val INTERVIEWER_ASSESSMENT_RESIDENCY = "interviewer_residency"
        val INTERVIEWER_ASSESSMENT_BRAC_CHP = "interviewer_brac"
        val INTERVIEWER_ASSESSMENT_QUALIFIES = "interviewer_qualifies"
        val INTERVIEWER_ASSESSMENT_ABILITY_TO_READ = "interviewer_read"
        val READ_AND_INTERPRET_PASSAGE = "read_and_interpret"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + APPLICANT + varchar_field + ", "
                + RECRUITMENT + integer_field + ", "
                + MOTIVATION + integer_field + ", "
                + COMMUNITY + integer_field + ", "
                + MENTALITY + integer_field + ", "
                + SELLING + integer_field + ", "
                + COUNTRY + varchar_field + ", "
                + HEALTH + integer_field + ", "
                + INVESTMENT + integer_field + ", "
                + INTERPERSONAL + integer_field + ", "
                + COMMITMENT + integer_field + ", "
                + TOTAL + integer_field + ", "
                + CANJOIN + integer_field + ", "
                + SELECTED + integer_field + ", "
                + ADDED_BY + integer_field + ", "
                + COMMENT + text_field + ", "
                + READ_AND_INTERPRET_PASSAGE + integer_field + ", "
                + INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION + integer_field + ", "
                + INTERVIEWER_ASSESSMENT_AGE + integer_field + ", "
                + INTERVIEWER_ASSESSMENT_RESIDENCY + integer_field + ", "
                + INTERVIEWER_ASSESSMENT_BRAC_CHP + integer_field + ", "
                + INTERVIEWER_ASSESSMENT_ABILITY_TO_READ + integer_field + ", "
                + INTERVIEWER_ASSESSMENT_QUALIFIES + integer_field + ", "
                + DATE_ADDED + integer_field + ", "
                + SYNCED + integer_field + ") ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
        val ADD_READ_FIELD = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + READ_AND_INTERPRET_PASSAGE + integer_field + ";"

        val ADD_INTERVIEWER_MOTIVATION = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION + integer_field + ";"

        val ADD_INTERVIEWER_AGE_FIELD = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + INTERVIEWER_ASSESSMENT_AGE + integer_field + ";"
        val ADD_INTERVIEWER_RESIDENCY_FIELD = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + INTERVIEWER_ASSESSMENT_RESIDENCY + integer_field + ";"
        val ADD_INTERVIEWER_BRAC_CHP_FIELD = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + INTERVIEWER_ASSESSMENT_BRAC_CHP + integer_field + ";"
        val ADD_INTERVIEWER_ABILITY_TO_READ = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + INTERVIEWER_ASSESSMENT_ABILITY_TO_READ + integer_field + ";"
        val ADD_INTERVIEWER_QUALIFIES = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + INTERVIEWER_ASSESSMENT_QUALIFIES + integer_field + ";"
    }
}

