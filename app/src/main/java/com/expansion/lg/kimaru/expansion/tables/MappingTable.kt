package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class MappingTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    var columns = arrayOf(ID, MAPPINGNAME, COUNTRY, COUNTY, ADDED_BY, CONTACTPERSON, CONTACTPERSONPHONE, COMMENT, DATE_ADDED, SYNCED, DISTRICT, SUBCOUNTY, REGION)

    val mappingData: List<Mapping>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val mappingList = ArrayList<Mapping>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                mappingList.add(cursorToMapping(cursor))
                cursor.moveToNext()
            }
            db.close()
            return mappingList
        }

    //JSON
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

        if (!isFieldExist(REGION)) {
            this.addRegionField()
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_DATABASE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addRegionField() {
        val db = readableDatabase
        db.execSQL(ADD_REGION_FIELD_SQL)
    }

    fun addData(mapping: Mapping): String {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, mapping.id)
        cv.put(MAPPINGNAME, mapping.mappingName)
        cv.put(COUNTRY, mapping.country)
        cv.put(COUNTY, mapping.county)
        cv.put(DISTRICT, mapping.district)
        cv.put(ADDED_BY, mapping.addedBy)
        cv.put(CONTACTPERSON, mapping.contactPerson)
        cv.put(CONTACTPERSONPHONE, mapping.contactPersonPhone)
        cv.put(COMMENT, mapping.comment)
        cv.put(SUBCOUNTY, mapping.subCounty)
        cv.put(SYNCED, if (mapping.isSynced) 1 else 0)
        cv.put(DATE_ADDED, mapping.dateAdded)
        cv.put(REGION, mapping.mappingRegion)

        val id: Long
        if (isExist(mapping)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + mapping.id + "'", null).toLong()
            Log.d("Tremap DB Op", "Mapping updated")
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Tremap DB Op", "Mapping Created")
        }
        db.close()
        return id.toString()

    }

    fun isExist(mapping: Mapping): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + mapping.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getMappingByUuid(uuid: String): Mapping {
        val whereClause = "$ID = ?"
        Log.d("======", "TREMAP $uuid")
        val whereArgs = arrayOf(uuid)
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        return cursorToMapping(cursor)
    }

    fun getMappingById(uuid: String): Mapping? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(uuid)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val mapping = cursorToMapping(cursor)
            db.close()
            return mapping
        }
    }


    fun getMappingsByCountry(countryCode: String): List<Mapping> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$COUNTRY = ?"
        val whereArgs = arrayOf(countryCode)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val mappingList = ArrayList<Mapping>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            mappingList.add(cursorToMapping(cursor))
            cursor.moveToNext()
        }
        db.close()

        return mappingList
    }

    fun getMappingByCounty(uuid: String): Mapping {
        var mapping = Mapping()
        val selection = arrayOf(uuid)
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, columns, ID, selection, null, null, null, null)
        mapping = cursorToMapping(cursor)
        cursor.close()
        return mapping
    }

    fun getMappingByDistrict(uuid: String): Mapping {
        var mapping = Mapping()
        val selection = arrayOf(uuid)
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, columns, ID, selection, null, null, null, null)
        mapping = cursorToMapping(cursor)
        cursor.close()
        return mapping
    }

    fun fromJson(jsonObject: JSONObject) {
        val mapping = Mapping()
        try {


            mapping.id = jsonObject.getString(ID)
            if (!jsonObject.isNull(MAPPINGNAME)) {
                mapping.mappingName = jsonObject.getString(MAPPINGNAME)
            } else {
                mapping.mappingName = ""
            }

            if (!jsonObject.isNull(COUNTRY)) {
                mapping.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(COUNTY)) {
                mapping.county = jsonObject.getString(COUNTY)
            }
            if (!jsonObject.isNull(ADDED_BY)) {
                mapping.addedBy = jsonObject.getInt(ADDED_BY)
            }
            if (!jsonObject.isNull(CONTACTPERSON)) {
                mapping.contactPerson = jsonObject.getString(CONTACTPERSON)
            }
            if (!jsonObject.isNull(CONTACTPERSONPHONE)) {
                mapping.contactPersonPhone = jsonObject.getString(CONTACTPERSONPHONE)
            }
            if (!jsonObject.isNull(COMMENT)) {
                mapping.comment = jsonObject.getString(COMMENT)
            }
            if (!jsonObject.isNull(DATE_ADDED)) {
                mapping.dateAdded = jsonObject.getLong(DATE_ADDED)
            }
            if (!jsonObject.isNull(SYNCED)) {
                mapping.isSynced = jsonObject.getInt(SYNCED) == 1
            }
            if (!jsonObject.isNull(DISTRICT)) {
                mapping.district = jsonObject.getString(DISTRICT)
            }
            if (!jsonObject.isNull(SUBCOUNTY)) {
                mapping.subCounty = jsonObject.getString(SUBCOUNTY)
            }
            addData(mapping)
        } catch (e: Exception) {
            Log.d("Tremap", "ERROR CREATING MAPPING FROM JSON")
            Log.d("Tremap", "CE ERROR " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {
        // add column
        db.execSQL(DB_UPDATE_V2)
        val locationDataSync = LocationDataSync(context)
        locationDataSync.pollLocations()
    }

    private fun cursorToMapping(cursor: Cursor): Mapping {
        val mapping = Mapping()
        mapping.id = cursor.getString(cursor.getColumnIndex(ID))
        mapping.mappingName = cursor.getString(cursor.getColumnIndex(MAPPINGNAME))
        mapping.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
        mapping.county = cursor.getString(cursor.getColumnIndex(COUNTY))
        mapping.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
        mapping.contactPerson = cursor.getString(cursor.getColumnIndex(CONTACTPERSON))
        mapping.contactPersonPhone = cursor.getString(cursor.getColumnIndex(CONTACTPERSONPHONE))
        mapping.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
        mapping.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
        mapping.isSynced = cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1
        mapping.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
        mapping.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
        mapping.mappingRegion = cursor.getString(cursor.getColumnIndex(REGION))
        return mapping
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

    companion object {

        val TABLE_NAME = "mapping"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "
        var real_field = " REAL "

        val ID = "id"
        val MAPPINGNAME = "name"
        val COUNTRY = "country"
        val COUNTY = "county"
        val SUBCOUNTY = "subcounty"
        val DISTRICT = "district"
        val REGION = "region"
        val ADDED_BY = "added_by"
        val CONTACTPERSON = "contact_person"
        val CONTACTPERSONPHONE = "phone"
        val COMMENT = "comment"
        val SYNCED = "synced"
        val DATE_ADDED = "date_added"
        val JSON_ROOT = "mappings"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + MAPPINGNAME + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + COUNTY + varchar_field + ", "
                + DISTRICT + varchar_field + ", "
                + SUBCOUNTY + varchar_field + ", "
                + ADDED_BY + integer_field + ", "
                + CONTACTPERSON + varchar_field + ", "
                + CONTACTPERSONPHONE + varchar_field + ", "
                + REGION + varchar_field + ", "
                + COMMENT + text_field + ", "
                + SYNCED + integer_field + ", "
                + DATE_ADDED + real_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
        val DB_UPDATE_V2 = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + SUBCOUNTY + varchar_field + ";"

        val ADD_REGION_FIELD_SQL = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + REGION + varchar_field + ";"
    }
}