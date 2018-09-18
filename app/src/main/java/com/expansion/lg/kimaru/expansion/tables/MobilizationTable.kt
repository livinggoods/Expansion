package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Mobilization
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class MobilizationTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    var columns = arrayOf(ID, NAME, MAPPING, COUNTRY, ADDED_BY, COMMENT, DATE_ADDED, SYNCED, DISTRICT, COUNTY, SUB_COUNTY, PARISH)

    val mobilizationData: List<Mobilization>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val mobilizationList = ArrayList<Mobilization>()


            cursor.moveToFirst()
            while (!cursor.isAfterLast) {


                val mobilization = Mobilization()

                mobilization.id = cursor.getString(cursor.getColumnIndex(ID))
                mobilization.name = cursor.getString(cursor.getColumnIndex(NAME))
                mobilization.mappingId = cursor.getString(cursor.getColumnIndex(MAPPING))
                mobilization.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                mobilization.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
                mobilization.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
                mobilization.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
                mobilization.synced = cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1
                mobilization.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
                mobilization.county = cursor.getString(cursor.getColumnIndex(COUNTY))
                mobilization.subCounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
                mobilization.parish = cursor.getString(cursor.getColumnIndex(PARISH))

                mobilizationList.add(mobilization)
                cursor.moveToNext()
            }
            db.close()

            return mobilizationList
        }

    val mobilizationJson: JSONObject
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

    val mobilizationToSyncAsJson: JSONObject
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

    fun addData(mobilization: Mobilization): Long {

        val db = writableDatabase
        val cv = ContentValues()

        cv.put(ID, mobilization.id)
        cv.put(NAME, mobilization.name)
        cv.put(MAPPING, mobilization.mappingId)
        cv.put(COUNTRY, mobilization.country)
        cv.put(ADDED_BY, mobilization.addedBy)
        cv.put(COMMENT, mobilization.comment)
        cv.put(DATE_ADDED, mobilization.dateAdded)
        cv.put(COUNTY, mobilization.county)
        cv.put(DISTRICT, mobilization.district)
        cv.put(SUB_COUNTY, mobilization.subCounty)
        cv.put(PARISH, mobilization.parish)
        cv.put(SYNCED, mobilization.synced)
        val id: Long
        if (isExist(mobilization)) {
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + mobilization.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()

        return id

    }

    fun getMobilizationByMappingId(mappingId: String): List<Mobilization> {
        val db = readableDatabase

        val whereClause = "$MAPPING = ?"
        val whereArgs = arrayOf(mappingId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val mobilizationList = ArrayList<Mobilization>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {


            val mobilization = Mobilization()

            mobilization.id = cursor.getString(cursor.getColumnIndex(ID))
            mobilization.name = cursor.getString(cursor.getColumnIndex(NAME))
            mobilization.mappingId = cursor.getString(cursor.getColumnIndex(MAPPING))
            mobilization.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            mobilization.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
            mobilization.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            mobilization.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
            mobilization.synced = cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1
            mobilization.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            mobilization.county = cursor.getString(cursor.getColumnIndex(COUNTY))
            mobilization.subCounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
            mobilization.parish = cursor.getString(cursor.getColumnIndex(PARISH))

            mobilizationList.add(mobilization)
            cursor.moveToNext()
        }
        db.close()

        return mobilizationList
    }


    fun fromJson(jsonObject: JSONObject) {
        try {
            val mobilization = Mobilization()
            mobilization.id = jsonObject.getString(ID)
            mobilization.name = jsonObject.getString(NAME)
            mobilization.mappingId = jsonObject.getString(MAPPING)
            mobilization.country = jsonObject.getString(COUNTRY)
            mobilization.addedBy = jsonObject.getInt(ADDED_BY)
            mobilization.comment = jsonObject.getString(COMMENT)
            mobilization.dateAdded = jsonObject.getLong(DATE_ADDED)
            mobilization.synced = jsonObject.getBoolean(SYNCED)
            mobilization.county = jsonObject.getString(COUNTY)
            mobilization.district = jsonObject.getString(DISTRICT)
            mobilization.subCounty = jsonObject.getString(SUB_COUNTY)
            mobilization.parish = jsonObject.getString(PARISH)
            this.addData(mobilization)
        } catch (e: Exception) {
        }

    }

    fun getMobilizationById(id: String): Mobilization? {
        val db = readableDatabase

        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val mobilization = Mobilization()
            mobilization.id = cursor.getString(cursor.getColumnIndex(ID))
            mobilization.name = cursor.getString(cursor.getColumnIndex(NAME))
            mobilization.mappingId = cursor.getString(cursor.getColumnIndex(MAPPING))
            mobilization.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            mobilization.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
            mobilization.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            mobilization.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
            mobilization.synced = cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1
            mobilization.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            mobilization.county = cursor.getString(cursor.getColumnIndex(COUNTY))
            mobilization.subCounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
            mobilization.parish = cursor.getString(cursor.getColumnIndex(PARISH))
            db.close()
            return mobilization
        }

    }

    fun getMobilizationByCountyId(countyId: String): List<Mobilization> {
        val db = readableDatabase

        val whereClause = "$COUNTY = ?"
        val whereArgs = arrayOf(countyId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val mobilizationList = ArrayList<Mobilization>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {


            val mobilization = Mobilization()

            mobilization.id = cursor.getString(cursor.getColumnIndex(ID))
            mobilization.name = cursor.getString(cursor.getColumnIndex(NAME))
            mobilization.mappingId = cursor.getString(cursor.getColumnIndex(MAPPING))
            mobilization.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            mobilization.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
            mobilization.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            mobilization.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
            mobilization.synced = cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1
            mobilization.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            mobilization.county = cursor.getString(cursor.getColumnIndex(COUNTY))
            mobilization.subCounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
            mobilization.parish = cursor.getString(cursor.getColumnIndex(PARISH))

            mobilizationList.add(mobilization)
            cursor.moveToNext()
        }
        db.close()

        return mobilizationList
    }

    fun isExist(mobilization: Mobilization): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + mobilization.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "mobilization"
        val JSON_ROOT = "mobilization"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var real_field = " REAL "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "id"
        val NAME = "name"
        val MAPPING = "mapping"
        val COUNTRY = "country"
        val ADDED_BY = "added_by"
        val COMMENT = "comment"
        val DATE_ADDED = "client_time"
        val COUNTY = "county"
        val DISTRICT = "district"
        val SUB_COUNTY = "sub_county"
        val PARISH = "parish"
        val SYNCED = "synced"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + NAME + varchar_field + ", "
                + MAPPING + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + ADDED_BY + integer_field + ", "
                + COMMENT + text_field + ", "
                + COUNTY + text_field + ", "
                + DISTRICT + text_field + ", "
                + SUB_COUNTY + text_field + ", "
                + PARISH + text_field + ", "
                + DATE_ADDED + real_field + ", "
                + SYNCED + integer_field + ");")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

