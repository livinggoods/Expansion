package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable.Companion.TABLE_NAME

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Date
import java.util.UUID


/**
 * Created by kimaru on 2/28/17.
 */


class RecruitmentTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    internal var columns = arrayOf(ID, NAME, DISTRICT, SUB_COUNTY, DIVISION, LAT, LON, ADDED_BY, COMMENT, DATE_ADDED, SYNCED, COUNTRY, COUNTY, SUBCOUNTYID, COUNTYID, LOCATIONID)


    val recruitmentCount: Long
        get() {
            val db = this.readableDatabase
            val cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME)
            db.close()
            return cnt
        }

    val recruitmentData: List<Recruitment>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val recruitmentList = ArrayList<Recruitment>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val recruitment = Recruitment()
                recruitment.id = cursor.getString(cursor.getColumnIndex(ID))
                recruitment.name = cursor.getString(cursor.getColumnIndex(NAME))
                recruitment.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
                recruitment.subcounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
                recruitment.division = cursor.getString(cursor.getColumnIndex(DIVISION))
                recruitment.lat = cursor.getString(cursor.getColumnIndex(LAT))
                recruitment.lon = cursor.getString(cursor.getColumnIndex(LON))
                recruitment.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
                recruitment.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
                recruitment.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
                recruitment.synced = cursor.getInt(cursor.getColumnIndex(SYNCED))
                recruitment.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                recruitment.county = cursor.getString(cursor.getColumnIndex(COUNTY))
                recruitment.countyId = cursor.getInt(cursor.getColumnIndex(COUNTYID))
                recruitment.subCountyId = cursor.getString(cursor.getColumnIndex(SUBCOUNTYID))
                recruitment.locationId = cursor.getInt(cursor.getColumnIndex(LOCATIONID))
                recruitmentList.add(recruitment)
                cursor.moveToNext()
            }
            db.close()
            return recruitmentList
        }
    val recruitmentJson: JSONObject
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

    val recruitmentToSyncAsJson: JSONObject
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


    val recruitmentDataCursor: Cursor
        get() {
            val db = readableDatabase
            return db.query(TABLE_NAME, columns, null, null, null, null, null, null)
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

    fun addData(recruitment: Recruitment): Long {

        val db = writableDatabase

        val cv = ContentValues()
        cv.put(ID, recruitment.id)
        cv.put(NAME, recruitment.name)
        cv.put(DISTRICT, recruitment.district)
        cv.put(LAT, recruitment.lat)
        cv.put(LON, recruitment.lon)
        cv.put(SUB_COUNTY, recruitment.subcounty)
        cv.put(COUNTY, recruitment.county)
        cv.put(COUNTRY, recruitment.country)
        cv.put(DIVISION, recruitment.division)
        cv.put(ADDED_BY, recruitment.addedBy)
        cv.put(COMMENT, recruitment.comment)
        cv.put(DATE_ADDED, recruitment.dateAdded)
        cv.put(SYNCED, recruitment.synced)
        cv.put(SUBCOUNTYID, recruitment.subCountyId)
        cv.put(COUNTYID, recruitment.countyId)
        cv.put(LOCATIONID, recruitment.locationId)
        val id: Long
        if (isExist(recruitment)) {
            //uupdate
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + recruitment.id + "'", null).toLong()
        } else {
            //create new
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()
        return id

    }

    fun isExist(recruitment: Recruitment): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + recruitment.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getRecruitmentDataByCountryCode(country: String): List<Recruitment> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$COUNTRY = ?"
        val whereArgs = arrayOf(country)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val recruitmentList = ArrayList<Recruitment>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val recruitment = Recruitment()
            recruitment.id = cursor.getString(cursor.getColumnIndex(ID))
            recruitment.name = cursor.getString(cursor.getColumnIndex(NAME))
            recruitment.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            recruitment.subcounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
            recruitment.division = cursor.getString(cursor.getColumnIndex(DIVISION))
            recruitment.lat = cursor.getString(cursor.getColumnIndex(LAT))
            recruitment.lon = cursor.getString(cursor.getColumnIndex(LON))
            recruitment.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
            recruitment.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            recruitment.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
            recruitment.synced = cursor.getInt(cursor.getColumnIndex(SYNCED))
            recruitment.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            recruitment.county = cursor.getString(cursor.getColumnIndex(COUNTY))
            recruitment.countyId = cursor.getInt(cursor.getColumnIndex(COUNTYID))
            recruitment.subCountyId = cursor.getString(cursor.getColumnIndex(SUBCOUNTYID))
            recruitment.locationId = cursor.getInt(cursor.getColumnIndex(LOCATIONID))
            recruitmentList.add(recruitment)
            cursor.moveToNext()
        }
        db.close()
        return recruitmentList
    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val recruitment = Recruitment()
            recruitment.id = jsonObject.getString(ID)
            recruitment.name = jsonObject.getString(NAME)
            recruitment.district = jsonObject.getString(DISTRICT)
            recruitment.subcounty = jsonObject.getString(SUB_COUNTY)
            recruitment.division = jsonObject.getString(DIVISION)
            recruitment.country = jsonObject.getString(COUNTRY)
            recruitment.lat = jsonObject.getString(LAT)
            recruitment.lon = jsonObject.getString(LON)
            recruitment.addedBy = jsonObject.getInt(ADDED_BY)
            recruitment.comment = jsonObject.getString(COMMENT)
            recruitment.dateAdded = jsonObject.getLong(DATE_ADDED)
            recruitment.synced = jsonObject.getInt(SYNCED)
            recruitment.county = jsonObject.getString(COUNTY)
            recruitment.subCountyId = jsonObject.getString(SUBCOUNTYID)
            try {
                recruitment.countyId = jsonObject.getInt(COUNTYID)
            } catch (e: Exception) {
            }

            try {
                recruitment.locationId = jsonObject.getInt(LOCATIONID)
            } catch (e: Exception) {
            }

            this.addData(recruitment)
        } catch (e: Exception) {
        }

    }

    fun getRecruitmentbyQuery(whereClause: String): List<Recruitment> {

        val recruitments = ArrayList<Recruitment>()

        val db = readableDatabase
        val queryString = "SELECT * FROM  $TABLE_NAME WHERE $whereClause"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val recruitment = Recruitment()
            recruitment.id = cursor.getString(cursor.getColumnIndex(ID))
            recruitment.name = cursor.getString(cursor.getColumnIndex(NAME))
            recruitment.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            recruitment.subcounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
            recruitment.division = cursor.getString(cursor.getColumnIndex(DIVISION))
            recruitment.lat = cursor.getString(cursor.getColumnIndex(LAT))
            recruitment.lon = cursor.getString(cursor.getColumnIndex(LON))
            recruitment.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
            recruitment.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            recruitment.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
            recruitment.synced = cursor.getInt(cursor.getColumnIndex(SYNCED))
            recruitment.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            recruitment.county = cursor.getString(cursor.getColumnIndex(COUNTY))
            recruitment.countyId = cursor.getInt(cursor.getColumnIndex(COUNTYID))
            recruitment.subCountyId = cursor.getString(cursor.getColumnIndex(SUBCOUNTYID))
            recruitment.locationId = cursor.getInt(cursor.getColumnIndex(LOCATIONID))
            recruitments.add(recruitment)
            cursor.moveToNext()
        }
        db.close()
        return recruitments
    }

    fun getRecruitmentById(id: String): Recruitment? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val recruitment = Recruitment()
            recruitment.id = cursor.getString(cursor.getColumnIndex(ID))
            recruitment.name = cursor.getString(cursor.getColumnIndex(NAME))
            recruitment.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            recruitment.subcounty = cursor.getString(cursor.getColumnIndex(SUB_COUNTY))
            recruitment.division = cursor.getString(cursor.getColumnIndex(DIVISION))
            recruitment.lat = cursor.getString(cursor.getColumnIndex(LAT))
            recruitment.lon = cursor.getString(cursor.getColumnIndex(LON))
            recruitment.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
            recruitment.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            recruitment.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
            recruitment.synced = cursor.getInt(cursor.getColumnIndex(SYNCED))
            recruitment.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            recruitment.county = cursor.getString(cursor.getColumnIndex(COUNTY))
            recruitment.countyId = cursor.getInt(cursor.getColumnIndex(COUNTYID))
            recruitment.subCountyId = cursor.getString(cursor.getColumnIndex(SUBCOUNTYID))
            recruitment.locationId = cursor.getInt(cursor.getColumnIndex(LOCATIONID))
            val subCountyTable = SubCountyTable(context)
            val keCountyTable = KeCountyTable(context)

            if (cursor.getString(cursor.getColumnIndex(COUNTRY)).equals("UG", ignoreCase = true)) {
                recruitment.subCountyObj = subCountyTable
                        .getSubCountyById(cursor.getString(cursor.getColumnIndex(SUB_COUNTY)))!!
            } else {
                recruitment.keCounty = keCountyTable.getCountyById(cursor.getInt(12))!!
                recruitment.name = keCountyTable.getCountyById(cursor.getInt(12))!!.countyName
                recruitment.subCountyObj = subCountyTable.getSubCountyById(cursor.getString(3))!!
            }
            db.close()

            return recruitment
        }
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {
        // For each recruitment, update the County Name with the County ID
        // For each recruitment, update the Sub County Name with the County ID
        val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            // with this selection, let us extract the details
            //
            //
            val country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            if (country.equals("UG", ignoreCase = true)) {
                // get the county
                val countyLocationTable = CountyLocationTable(context)
                countyLocationTable.createLocations()
                //Thread.sleep(timeInMills);
                try {
                    Thread.sleep(1500L)
                } catch (e: Exception) {
                }

                // also update the district
                val districtName = cursor.getString(cursor.getColumnIndex(DISTRICT))
                val district = countyLocationTable.getDistrictByName(districtName)
                val districtId: Long?
                if (district == null) {
                    try {
                        Log.e("expansion", "Creating New District $districtName")
                        val countyLocation = CountyLocation("District", districtName,
                                "UG", 0, "", null, "", "", "", 2, "")

                        val id = countyLocationTable.addData(countyLocation)
                        Log.e("expansion", "New district has been created and ID is: " + id.toString())
                    } catch (e: Exception) {
                        Log.e("expansion", e.message)
                    }

                }
                try {
                    Thread.sleep(1000L)
                } catch (e: Exception) {
                }

                val savedDistrict = countyLocationTable.getDistrictByName(districtName)
                if (savedDistrict != null) {
                    val cv = ContentValues()
                    cv.put(DISTRICT, savedDistrict.id.toString())
                    db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(cursor.getColumnIndex(ID)) + "'", null)
                } else {
                    val retrievedDistrict = countyLocationTable.getDistrictByName(districtName)
                    Log.e("expansion", "Could not get the district with the name $districtName")
                    if (retrievedDistrict != null) {
                        val cv = ContentValues()
                        cv.put(DISTRICT, retrievedDistrict.id.toString())
                        db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(cursor.getColumnIndex(ID)) + "'", null)
                    }

                }
            } else {
                //Get the county
                val keCountyTable = KeCountyTable(context)
                val keCounty: KeCounty?
                keCounty = keCountyTable.getKeCountyByName(cursor.getString(cursor.getColumnIndex(COUNTY)))
                if (keCounty == null) {
                    // create a holding county
                    val ke = KeCounty()
                    ke.countyName = cursor.getString(cursor.getColumnIndex(COUNTY))
                    ke.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                    keCountyTable.addKeCounty(ke)
                }
                try {
                    Thread.sleep(1500L)
                } catch (e: Exception) {
                }

                val addedKeCounty = keCountyTable.getKeCountyByName(cursor.getString(cursor.getColumnIndex(COUNTY)))
                // get subCounty
                val subCountyTable = SubCountyTable(context)
                var subCounty = subCountyTable.getSubCountyByCountyAndName(addedKeCounty!!.id.toString(), country, cursor.getString(cursor.getColumnIndex(SUB_COUNTY)))
                if (subCounty == null) {
                    // Create subcounty
                    val uuid = UUID.randomUUID().toString()
                    subCounty = SubCounty(uuid, cursor.getString(cursor.getColumnIndex(SUB_COUNTY)),
                            keCounty!!.id.toString(), country, "", "", "", "", "", "",
                            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", false, Date().time, 0)
                    subCountyTable.addData(subCounty)
                }
                try {
                    Thread.sleep(1500L)
                } catch (e: Exception) {
                }

                val addedSubCounty = subCountyTable.getSubCountyByCountyAndName(addedKeCounty.id.toString(), country, cursor.getString(cursor.getColumnIndex(SUB_COUNTY)))
                // noe we can update the Recruitment
                val updateValues = ContentValues()

                updateValues.put(ID, cursor.getString(cursor.getColumnIndex(ID)))
                updateValues.put(NAME, cursor.getString(cursor.getColumnIndex(NAME)))
                updateValues.put(COUNTY, addedKeCounty.id.toString())
                updateValues.put(SUB_COUNTY, addedSubCounty!!.id.toString())
                updateValues.put(COUNTRY, cursor.getString(cursor.getColumnIndex(COUNTRY)))
                db.update(TABLE_NAME, updateValues, ID + "='" + cursor.getString(cursor.getColumnIndex(ID)) + "'", null)
                try {
                    Thread.sleep(1000L)
                } catch (e: Exception) {
                }

            }
            cursor.moveToNext()
        }
    }

    companion object {

        val TABLE_NAME = "recruitment"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val JSON_ROOT = "recruitments"
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "id"
        val NAME = "name"
        val LON = "lon"
        val LAT = "lat"
        val DISTRICT = "district"
        val SUB_COUNTY = "subcounty"
        val COUNTY = "county"
        val DIVISION = "division"
        val COUNTRY = "country"
        val ADDED_BY = "added_by"
        val COMMENT = "comment"
        val DATE_ADDED = "client_time"
        val SYNCED = "synced"
        val SUBCOUNTYID = "subcounty_id"
        val COUNTYID = "county_id"
        val LOCATIONID = "location_id"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + NAME + varchar_field + ", "
                + LAT + varchar_field + ", "
                + LON + varchar_field + ", "
                + DISTRICT + varchar_field + ", "
                + SUB_COUNTY + varchar_field + ", "
                + COUNTY + varchar_field + ", "
                + DIVISION + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + ADDED_BY + integer_field + ", "
                + COMMENT + text_field + ", "
                + DATE_ADDED + integer_field + ", "
                + SUBCOUNTYID + varchar_field + ", "
                + COUNTYID + varchar_field + ", "
                + LOCATIONID + varchar_field + ", "
                + SYNCED + integer_field + ");")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

