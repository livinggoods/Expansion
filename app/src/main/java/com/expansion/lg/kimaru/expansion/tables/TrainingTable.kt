package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Training
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.TrainingTable.Companion.TABLE_NAME


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 9/4/17.
 */


class TrainingTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    var columns = arrayOf(ID, TRAINNIG_NAME, COUNTRY, COUNTY, SUBCOUNTY, WARD, DISTRICT, PARISH, LOCATION, CREATED_BY, STATUS, CLIENT_TIME, LAT, LON, COMMENT, RECRUITMENT)


    val trainingData: List<Training>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val trainingList = ArrayList<Training>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val training = Training()
                training.id = cursor.getString(cursor.getColumnIndex(ID))
                training.trainingName = cursor.getString(cursor.getColumnIndex(TRAINNIG_NAME))
                training.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                training.county = cursor.getString(cursor.getColumnIndex(COUNTY))
                training.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
                training.ward = cursor.getString(cursor.getColumnIndex(WARD))
                training.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
                training.parish = cursor.getString(cursor.getColumnIndex(PARISH))
                training.location = cursor.getString(cursor.getColumnIndex(LOCATION))
                training.recruitment = cursor.getString(cursor.getColumnIndex(RECRUITMENT))
                training.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
                training.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
                training.status = cursor.getInt(cursor.getColumnIndex(STATUS))
                training.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                training.lat = cursor.getDouble(cursor.getColumnIndex(LAT))
                training.lon = cursor.getDouble(cursor.getColumnIndex(LON))

                trainingList.add(training)
                cursor.moveToNext()
            }
            db.close()
            return trainingList
        }


    val cursor: Cursor
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

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_DATABASE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //prior to this, there are registrations that have been created using the old Scoring
        // We need to select all of them, and for each, we shall create the referrals and return the
        //IDs. Then we update the registration
        Log.w("referral", "upgrading database from" + oldVersion + "to" + newVersion)

        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addTraining(training: Training): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, training.id)
        cv.put(TRAINNIG_NAME, training.trainingName)
        cv.put(COUNTRY, training.country)
        cv.put(COUNTY, training.county)
        cv.put(SUBCOUNTY, training.subCounty)
        cv.put(WARD, training.ward)
        cv.put(DISTRICT, training.district)
        cv.put(PARISH, training.parish)
        cv.put(LOCATION, training.location)
        cv.put(RECRUITMENT, training.recruitment)
        cv.put(COMMENT, training.comment)
        cv.put(CREATED_BY, training.createdBy)
        cv.put(STATUS, training.status)
        cv.put(CLIENT_TIME, training.clientTime)
        cv.put(LAT, training.lat)
        cv.put(LON, training.lon)

        val id: Long
        if (isExist(training)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + training.id + "'", null).toLong()
            Log.d("Tremap DB Op", "Training updated")
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Tremap DB Op", "Training created")
        }
        db.close()
        return id

    }

    fun isExist(training: Training): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + training.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    fun getTrainingByCountry(countryCode: String): List<Training>? {

        val db = readableDatabase
        val whereClause = "$COUNTRY = ?"
        val whereArgs = arrayOf(countryCode)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingList = ArrayList<Training>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val training = Training()
                training.id = cursor.getString(cursor.getColumnIndex(ID))
                training.trainingName = cursor.getString(cursor.getColumnIndex(TRAINNIG_NAME))
                training.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                training.county = cursor.getString(cursor.getColumnIndex(COUNTY))
                training.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
                training.ward = cursor.getString(cursor.getColumnIndex(WARD))
                training.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
                training.parish = cursor.getString(cursor.getColumnIndex(PARISH))
                training.location = cursor.getString(cursor.getColumnIndex(LOCATION))
                training.recruitment = cursor.getString(cursor.getColumnIndex(RECRUITMENT))
                training.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
                training.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
                training.status = cursor.getInt(cursor.getColumnIndex(STATUS))
                training.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                training.lat = cursor.getDouble(cursor.getColumnIndex(LAT))
                training.lon = cursor.getDouble(cursor.getColumnIndex(LON))

                trainingList.add(training)
                cursor.moveToNext()
            }
            db.close()
            return trainingList
        }

    }

    fun getTrainingById(id: String): Training? {

        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val training = Training()
            training.id = cursor.getString(cursor.getColumnIndex(ID))
            training.trainingName = cursor.getString(cursor.getColumnIndex(TRAINNIG_NAME))
            training.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            training.county = cursor.getString(cursor.getColumnIndex(COUNTY))
            training.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
            training.ward = cursor.getString(cursor.getColumnIndex(WARD))
            training.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            training.parish = cursor.getString(cursor.getColumnIndex(PARISH))
            training.location = cursor.getString(cursor.getColumnIndex(LOCATION))
            training.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            training.recruitment = cursor.getString(cursor.getColumnIndex(RECRUITMENT))
            training.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
            training.status = cursor.getInt(cursor.getColumnIndex(STATUS))
            training.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
            training.lat = cursor.getDouble(cursor.getColumnIndex(LAT))
            training.lon = cursor.getDouble(cursor.getColumnIndex(LON))

            db.close()

            /**
             * @TODO: Add all registrations referred by this user.
             * When one retrieves a referral, it would be good to get all the registrations that (s)he
             * has referred
             */
            return training
        }

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val training = Training()
            training.id = jsonObject.getString(ID)
            if (!jsonObject.isNull(TRAINNIG_NAME)) {
                training.trainingName = jsonObject.getString(TRAINNIG_NAME)
            }
            if (!jsonObject.isNull(COUNTRY)) {
                training.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(COUNTY)) {
                training.county = jsonObject.getString(COUNTY)
            }
            if (!jsonObject.isNull(SUBCOUNTY)) {
                training.subCounty = jsonObject.getString(SUBCOUNTY)
            }
            if (!jsonObject.isNull(WARD)) {
                training.ward = jsonObject.getString(WARD)
            }
            if (!jsonObject.isNull(DISTRICT)) {
                training.district = jsonObject.getString(DISTRICT)
            }
            if (!jsonObject.isNull(PARISH)) {
                training.parish = jsonObject.getString(PARISH)
            }
            if (!jsonObject.isNull(LOCATION)) {
                training.location = jsonObject.getString(LOCATION)
            }
            if (!jsonObject.isNull(CREATED_BY)) {
                training.createdBy = jsonObject.getInt(CREATED_BY)
            }
            if (!jsonObject.isNull(STATUS)) {
                training.status = jsonObject.getInt(STATUS)
            }
            if (!jsonObject.isNull(RECRUITMENT)) {
                training.recruitment = jsonObject.getString(RECRUITMENT)
            }
            if (!jsonObject.isNull(COMMENT)) {
                training.comment = jsonObject.getString(COMMENT)
            }
            if (!jsonObject.isNull(CLIENT_TIME)) {
                training.clientTime = jsonObject.getLong(CLIENT_TIME)
            }
            if (!jsonObject.isNull(LAT)) {
                training.lat = jsonObject.getDouble(LAT)
            }
            if (!jsonObject.isNull(LON)) {
                training.lon = jsonObject.getDouble(LON)
            }
            addTraining(training)
        } catch (e: Exception) {
            Log.d("Tremap Training ERR", "From Json : " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "training"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var JSON_ROOT = "trainings"

        val ID = "id"
        val TRAINNIG_NAME = "training_name"
        val COUNTRY = "country"
        val COUNTY = "county_id"
        val SUBCOUNTY = "subcounty_id"
        val WARD = "ward_id"
        val RECRUITMENT = "recruitment_id"
        val DISTRICT = "district"
        val PARISH = "parish_id"
        val LOCATION = "location_id"
        val CREATED_BY = "created_by"
        val STATUS = "status"
        val CLIENT_TIME = "client_time"
        val LAT = "lat"
        val LON = "lon"
        val COMMENT = "comment"
        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + Constants.varchar_field + ","
                + TRAINNIG_NAME + Constants.varchar_field + ", "
                + COUNTRY + Constants.varchar_field + ", "
                + COUNTY + Constants.varchar_field + ", "
                + SUBCOUNTY + Constants.varchar_field + ", "
                + WARD + Constants.varchar_field + ", "
                + DISTRICT + Constants.varchar_field + ", "
                + PARISH + Constants.varchar_field + ", "
                + LOCATION + Constants.varchar_field + ", "
                + CREATED_BY + Constants.integer_field + ", "
                + STATUS + Constants.integer_field + ", "
                + CLIENT_TIME + Constants.real_field + ", "
                + COMMENT + Constants.text_field + ", "
                + RECRUITMENT + Constants.varchar_field + ", "
                + LAT + Constants.real_field + ", "
                + LON + Constants.real_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}


