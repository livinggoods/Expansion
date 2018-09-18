package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.ExamTable.Companion.TABLE_NAME

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class ExamTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    internal var columns = arrayOf(ID, APPLICANT, RECRUITMENT, MATH, PERSONALITY, ENGLISH, ADDED_BY, COMMENT, DATE_ADDED, SYNCED, COUNTRY)
    val examData: List<Exam>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val examList = ArrayList<Exam>()


            cursor.moveToFirst()
            while (!cursor.isAfterLast) {


                val exam = Exam()

                exam.id = cursor.getString(0)
                exam.applicant = cursor.getString(1)
                exam.recruitment = cursor.getString(2)
                exam.math = cursor.getDouble(3)
                exam.personality = cursor.getDouble(4)
                exam.english = cursor.getDouble(5)
                exam.addedBy = cursor.getInt(6)
                exam.comment = cursor.getString(7)
                exam.dateAdded = cursor.getLong(8)
                exam.synced = cursor.getInt(9)
                exam.country = cursor.getString(10)

                examList.add(exam)
                cursor.moveToNext()
            }
            db.close()

            return examList
        }

    val examCount: Long
        get() {
            val db = this.readableDatabase
            val cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME)
            db.close()
            return cnt
        }

    val examDataCursor: Cursor
        get() {

            val db = readableDatabase

            return db.query(TABLE_NAME, columns, null, null, null, null, null, null)
        }
    val examJson: JSONObject
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

    val examsToSyncAsJson: JSONObject
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

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_DATABASE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addData(exam: Exam): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, exam.id)
        cv.put(APPLICANT, exam.applicant)
        cv.put(RECRUITMENT, exam.recruitment)
        cv.put(COUNTRY, exam.country)
        cv.put(MATH, exam.math)
        cv.put(PERSONALITY, exam.personality)
        cv.put(ENGLISH, exam.english)
        cv.put(ADDED_BY, exam.addedBy)
        cv.put(COMMENT, exam.comment)
        cv.put(DATE_ADDED, exam.dateAdded)
        cv.put(SYNCED, exam.synced)


        val id: Long
        if (isExist(exam)) {
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + exam.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()

        // When one passes the interview, we need to update the registration
        val registrationTable = RegistrationTable(context)
        val registration = registrationTable.getRegistrationById(exam.applicant)
        if (registration!!.hasPassed() && exam.hasPassed()) {
            registration.proceed = 1
        } else {
            registration.proceed = 0
        }
        registrationTable.addData(registration)
        return id

    }

    fun getExamByRegistration(registrationUuid: String): Exam? {
        val db = readableDatabase

        val whereClause = "$APPLICANT = ?"
        val whereArgs = arrayOf(registrationUuid)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val exam = Exam()
            exam.id = cursor.getString(0)
            exam.applicant = cursor.getString(1)
            exam.recruitment = cursor.getString(2)
            exam.math = cursor.getDouble(3)
            exam.personality = cursor.getDouble(4)
            exam.english = cursor.getDouble(5)
            exam.addedBy = cursor.getInt(6)
            exam.comment = cursor.getString(7)
            exam.dateAdded = cursor.getLong(8)
            exam.synced = cursor.getInt(9)
            exam.country = cursor.getString(10)
            return exam
        }

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val exam = Exam()
            exam.id = jsonObject.getString(ExamTable.ID)
            exam.applicant = jsonObject.getString(ExamTable.APPLICANT)
            exam.recruitment = jsonObject.getString(ExamTable.RECRUITMENT)
            exam.country = jsonObject.getString(ExamTable.COUNTRY)
            exam.math = jsonObject.getDouble(ExamTable.MATH)
            exam.personality = jsonObject.getDouble(ExamTable.PERSONALITY)
            exam.english = jsonObject.getDouble(ExamTable.ENGLISH)
            exam.addedBy = jsonObject.getInt(ExamTable.ADDED_BY)
            exam.comment = jsonObject.getString(ExamTable.COMMENT)
            exam.dateAdded = jsonObject.getLong(ExamTable.DATE_ADDED)
            exam.synced = jsonObject.getInt(ExamTable.SYNCED)
            this.addData(exam)
        } catch (e: Exception) {
        }

    }

    fun getExamById(id: String): Exam? {
        val db = readableDatabase

        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val exam = Exam()
            exam.id = cursor.getString(0)
            exam.applicant = cursor.getString(1)
            exam.recruitment = cursor.getString(2)
            exam.math = cursor.getDouble(3)
            exam.personality = cursor.getDouble(4)
            exam.english = cursor.getDouble(5)
            exam.addedBy = cursor.getInt(6)
            exam.comment = cursor.getString(7)
            exam.dateAdded = cursor.getLong(8)
            exam.synced = cursor.getInt(9)
            exam.country = cursor.getString(10)
            return exam
        }

    }

    fun getExamsByRecruitment(recruitment: Recruitment): List<Exam> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$RECRUITMENT = ?"
        val whereArgs = arrayOf(recruitment.id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)


        val examList = ArrayList<Exam>()


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {


            val exam = Exam()

            exam.id = cursor.getString(0)
            exam.applicant = cursor.getString(1)
            exam.recruitment = cursor.getString(2)
            exam.math = cursor.getDouble(3)
            exam.personality = cursor.getDouble(4)
            exam.english = cursor.getDouble(5)
            exam.addedBy = cursor.getInt(6)
            exam.comment = cursor.getString(7)
            exam.dateAdded = cursor.getLong(8)
            exam.synced = cursor.getInt(9)
            exam.country = cursor.getString(10)

            examList.add(exam)
            cursor.moveToNext()
        }
        db.close()

        return examList
    }

    fun isExist(exam: Exam): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + exam.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "exam"
        val JSON_ROOT = "exams"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var real_field = " REAL "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "id"
        val APPLICANT = "applicant"
        val RECRUITMENT = "recruitment"
        val COUNTRY = "country"
        val MATH = "math"
        val PERSONALITY = "personality"
        val ENGLISH = "english"
        val ADDED_BY = "added_by"
        val COMMENT = "comment"
        val DATE_ADDED = "client_time"
        val SYNCED = "synced"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + APPLICANT + varchar_field + ", "
                + RECRUITMENT + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + MATH + real_field + ", "
                + PERSONALITY + real_field + ", "
                + ENGLISH + real_field + ", "
                + ADDED_BY + integer_field + ", "
                + COMMENT + text_field + ", "
                + DATE_ADDED + integer_field + ", "
                + SYNCED + integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

