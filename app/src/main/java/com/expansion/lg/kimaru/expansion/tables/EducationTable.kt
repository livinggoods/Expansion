package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Education
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.EducationTable.Companion.TABLE_NAME

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class EducationTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    internal var columns = arrayOf(ID, LEVEL_NAME, TYPE, HIERACHY, COUNTRY)
    val educationData: List<Education>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val educationList = ArrayList<Education>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val education = cursorToEducation(cursor)

                educationList.add(education)
                cursor.moveToNext()
            }
            db.close()
            return educationList
        }


    val educationJson: JSONObject
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

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_DATABASE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun createEducationFromJson(jsonObject: JSONObject) {
        val education = Education()
        try {
            education.id = jsonObject.getInt(ID)
            if (!jsonObject.isNull(LEVEL_NAME)) {
                education.levelName = jsonObject.getString(LEVEL_NAME)
            }
            if (!jsonObject.isNull(COUNTRY)) {
                education.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(TYPE)) {
                education.levelType = jsonObject.getString(TYPE)
            }
            if (!jsonObject.isNull(HIERACHY)) {
                education.hierachy = jsonObject.getInt(HIERACHY)
            }
            this.addEducation(education)
        } catch (e: Exception) {
        }

    }

    fun addEducation(education: Education): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, education.id)
        cv.put(LEVEL_NAME, education.levelName)
        cv.put(TYPE, education.levelType)
        cv.put(HIERACHY, education.hierachy)
        cv.put(COUNTRY, education.country)

        val id: Long
        if (isExist(education)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + education.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()
        return id
    }

    fun getEducationById(id: Int): Education? {

        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        return if (!cursor.moveToFirst() || cursor.count == 0) {
            null
        } else {
            cursorToEducation(cursor)
        }
    }

    private fun cursorToEducation(cursor: Cursor): Education {
        val education = Education()

        education.id = cursor.getInt(cursor.getColumnIndex(ID))
        education.levelName = cursor.getString(cursor.getColumnIndex(LEVEL_NAME))
        education.levelType = cursor.getString(cursor.getColumnIndex(TYPE))
        education.hierachy = cursor.getInt(cursor.getColumnIndex(HIERACHY))
        education.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
        return education
    }

    fun getEducationDataCursor(country: String): Cursor {

        val db = readableDatabase
        val whereClause = "$COUNTRY = ?"
        val whereArgs = arrayOf(country)
        val columns = arrayOf(ID, LEVEL_NAME, TYPE, HIERACHY, COUNTRY)
        return db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
    }

    fun createEducationLevels() {
        //P1 to 8
        // id, hierachy;
        val levelName: String
        val levelType: String
        val country: String
        // set Ugandan Education System
        for (y in 1..8) {
            val education = Education()
            education.id = y
            education.country = "UG"
            education.hierachy = y
            if (y == 1) {
                education.levelName = "Less than P7"
                education.levelType = "primary"
            } else if (y >= 2 && y <= 7) {
                education.levelName = "S" + (y - 1)
                education.levelType = "secondary"
            } else {
                education.levelName = "Tertiary"
                education.levelType = "tertiary"
            }

            this.addEducation(education)
        }
        for (x in 9..15) {
            val education = Education()
            education.id = x
            education.hierachy = x - 8
            education.country = "KE"
            if (x > 14) {
                education.levelName = "Tertiary"
                education.levelType = "tertiary"
            } else if (x > 10) {
                education.levelName = "Form " + (x - 10)
                education.levelType = "secondary"
            } else if (x == 10) {
                education.levelName = "P" + (x - 2)
                education.levelType = "primary"
            } else {
                education.levelName = "Less than P" + (x - 2)
                education.levelType = "primary"
            }
            this.addEducation(education)
        }
    }

    fun isExist(education: Education): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + education.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val education = Education()
            education.id = jsonObject.getInt(ID)
            education.levelName = jsonObject.getString(LEVEL_NAME)
            education.levelType = jsonObject.getString(TYPE)
            education.hierachy = jsonObject.getInt(HIERACHY)
            education.country = jsonObject.getString(COUNTRY)
            this.addEducation(education)
        } catch (e: Exception) {
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "education"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        //String id, level_name, level_type, hierachy, country;

        var varchar_field = " varchar(512) "
        var primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "_id"
        val LEVEL_NAME = "name"
        val TYPE = "level_type"
        val HIERACHY = "hierachy"
        val COUNTRY = "country"
        val JSON_ROOT = "education"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LEVEL_NAME + varchar_field + ", "
                + TYPE + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + HIERACHY + integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS $TABLE_NAME"
    }


}

