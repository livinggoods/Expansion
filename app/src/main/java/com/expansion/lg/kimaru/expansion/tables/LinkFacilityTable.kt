package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable.Companion.TABLE_NAME

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class LinkFacilityTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    internal var columns = arrayOf(ID, NAME, COUNTY, LAT, LON, SUBCOUNTY, ADDED, ADDEDBY, MRDTLEVELS, ACTLEVELS, COUNTRY, MFLCODE, MAPPING, PARISH)

    val linkFacilityData: List<LinkFacility>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val facilityList = ArrayList<LinkFacility>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val linkFacility = cursorToLinkFacility(cursor)
                facilityList.add(linkFacility)
                cursor.moveToNext()
            }
            db.close()
            return facilityList
        }

    val linkFacilityCursor: Cursor
        get() {

            val db = readableDatabase
            return db.query(TABLE_NAME, columns, null, null, null, null, null, null)
        }

    val json: JSONObject
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

    init {
        if (!isFieldExist(PARISH)) {
            this.addParishFields()
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_DATABASE)

    }

    fun addParishFields() {
        val ADD_PARISH = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + PARISH + varchar_field + ";"
        val db = readableDatabase
        db.execSQL(ADD_PARISH)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        Log.w("Link Facility", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addData(linkFacility: LinkFacility): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, linkFacility.id)
        cv.put(NAME, linkFacility.facilityName)
        cv.put(COUNTY, linkFacility.county)
        cv.put(MAPPING, linkFacility.mappingId)
        cv.put(LAT, linkFacility.lat)
        cv.put(LON, linkFacility.lon)
        cv.put(MFLCODE, linkFacility.mflCode)
        cv.put(SUBCOUNTY, linkFacility.subCountyId)
        cv.put(ADDED, linkFacility.dateAdded)
        cv.put(ADDEDBY, linkFacility.addedBy)
        cv.put(MRDTLEVELS, linkFacility.mrdtLevels)
        cv.put(ACTLEVELS, linkFacility.actLevels)
        cv.put(COUNTRY, linkFacility.country)
        cv.put(PARISH, linkFacility.parish)

        val id: Long
        if (isExist(linkFacility)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + linkFacility.id + "'", null).toLong()
            Log.d("Tremap DB Op", "Link Facility updated")
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Tremap DB Op", "Link Facility Created")
        }

        db.close()
        return id

    }

    fun isExist(linkFacility: LinkFacility): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + linkFacility.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getLinkFacilityByParish(parishId: String): List<LinkFacility> {

        val db = readableDatabase

        val whereClause = "$PARISH = ? "

        val whereArgs = arrayOf(parishId)

        val facilityList = ArrayList<LinkFacility>()
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val linkFacility = cursorToLinkFacility(cursor)
            facilityList.add(linkFacility)
            cursor.moveToNext()
        }
        db.close()
        return facilityList
    }

    fun getLinkFacilityBySubCounty(subCountyId: String): List<LinkFacility> {

        val db = readableDatabase

        val whereClause = "$SUBCOUNTY = ? "

        val whereArgs = arrayOf(subCountyId)

        val facilityList = ArrayList<LinkFacility>()
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val linkFacility = cursorToLinkFacility(cursor)
            facilityList.add(linkFacility)
            cursor.moveToNext()
        }
        db.close()
        return facilityList
    }

    fun getLinkFacilityByName(facilityName: String): LinkFacility? {

        val db = readableDatabase

        val whereClause = "$NAME = ? "

        val whereArgs = arrayOf(facilityName)

        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val linkFacility = cursorToLinkFacility(cursor)
            db.close()
            return linkFacility
        }
    }

    fun getLinkFacilityById(uuid: String): LinkFacility? {

        val db = readableDatabase

        val whereClause = "$ID = ? "

        val whereArgs = arrayOf(uuid)

        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val linkFacility = cursorToLinkFacility(cursor)
            db.close()
            return linkFacility
        }
    }

    fun fromJson(jsonObject: JSONObject) {
        val linkFacility = LinkFacility()
        try {

            if (!jsonObject.isNull(ID)) {
                linkFacility.id = jsonObject.getString(ID)
            }
            if (!jsonObject.isNull(NAME)) {
                linkFacility.facilityName = jsonObject.getString(NAME)
            }

            if (!jsonObject.isNull(LAT)) {
                linkFacility.lat = jsonObject.getDouble(LAT)
            }

            if (!jsonObject.isNull(LON)) {
                linkFacility.lon = jsonObject.getDouble(LON)
            }

            if (!jsonObject.isNull(MAPPING)) {
                linkFacility.mappingId = jsonObject.getString(MAPPING)
            }

            if (!jsonObject.isNull(MAPPING)) {
                linkFacility.mappingId = jsonObject.getString(MAPPING)
            }

            if (!jsonObject.isNull(MFLCODE)) {
                linkFacility.mflCode = jsonObject.getString(MFLCODE)
            }

            if (!jsonObject.isNull(SUBCOUNTY)) {
                linkFacility.subCountyId = jsonObject.getString(SUBCOUNTY)
            }

            if (!jsonObject.isNull(ADDED)) {
                linkFacility.dateAdded = jsonObject.getLong(ADDED)
            } else {
                linkFacility.dateAdded = 1L
            }

            if (!jsonObject.isNull(COUNTY)) {
                linkFacility.county = jsonObject.getString(COUNTY)
            }

            if (!jsonObject.isNull(ADDEDBY)) {
                linkFacility.addedBy = jsonObject.getInt(ADDEDBY)
            }

            if (!jsonObject.isNull(MRDTLEVELS)) {
                linkFacility.setMrdtLevels(jsonObject.getInt(MRDTLEVELS).toLong())
            }

            if (!jsonObject.isNull(ACTLEVELS)) {
                linkFacility.setActLevels(jsonObject.getInt(ACTLEVELS).toLong())
            }
            if (!jsonObject.isNull(COUNTRY)) {
                linkFacility.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(PARISH)) {
                linkFacility.parish = jsonObject.getString(PARISH)
            }
            ///////
            addData(linkFacility)
        } catch (e: Exception) {
            Log.d("Tremap ERR", "Link Facility from JSON " + e.message)
        }

    }

    private fun cursorToLinkFacility(cursor: Cursor): LinkFacility {
        val linkFacility = LinkFacility()
        linkFacility.id = cursor.getString(cursor.getColumnIndex(ID))
        linkFacility.facilityName = cursor.getString(cursor.getColumnIndex(NAME))
        linkFacility.mappingId = cursor.getString(cursor.getColumnIndex(MAPPING))
        linkFacility.lat = cursor.getDouble(cursor.getColumnIndex(LAT))
        linkFacility.mflCode = cursor.getString(cursor.getColumnIndex(MFLCODE))
        linkFacility.county = cursor.getString(cursor.getColumnIndex(COUNTY))
        linkFacility.lon = cursor.getDouble(cursor.getColumnIndex(LON))
        linkFacility.subCountyId = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
        linkFacility.dateAdded = cursor.getLong(cursor.getColumnIndex(ADDED))
        linkFacility.addedBy = cursor.getInt(cursor.getColumnIndex(ADDEDBY))
        linkFacility.setMrdtLevels(cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)).toLong())
        linkFacility.setActLevels(cursor.getInt(cursor.getColumnIndex(ACTLEVELS)).toLong())
        linkFacility.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
        linkFacility.parish = cursor.getString(cursor.getColumnIndex(PARISH))

        return linkFacility
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

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
                Log.d("Tremap", "Error closing the  DB  $fieldName")
            }

        }
        return isExist
    }

    companion object {

        val TABLE_NAME = "link_facility"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        var JSON_ROOT = "link_facilities"

        val ID = "id"
        val NAME = "facility_name"
        val COUNTY = "county"
        val LAT = "lat"
        val LON = "lon"
        val SUBCOUNTY = "subcounty"
        val MFLCODE = "mfl_code"
        val MAPPING = "mapping"
        val ADDED = "date_added"
        val ADDEDBY = "added_by"
        val MRDTLEVELS = "mrdt_levels"
        val ACTLEVELS = "act_levels"
        val COUNTRY = "country"
        val PARISH = "parish"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + NAME + varchar_field + ", "
                + COUNTY + varchar_field + ", "
                + LAT + varchar_field + ", "
                + LON + varchar_field + ", "
                + MFLCODE + varchar_field + ", "
                + MAPPING + varchar_field + ", "
                + SUBCOUNTY + varchar_field + ", "
                + ADDED + integer_field + ", "
                + ADDEDBY + integer_field + ", "
                + MRDTLEVELS + integer_field + ", "
                + ACTLEVELS + integer_field + ", "
                + PARISH + varchar_field + ", "
                + COUNTRY + varchar_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }

}

