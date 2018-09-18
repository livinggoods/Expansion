package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable.Companion.TABLE_NAME

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 5/12/17.
 */


class ChewReferralTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    var columns = arrayOf(ID, NAME, PHONE, TITLE, COUNTRY, RECRUITMENT, SYNCED, COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON)

    val chewReferralData: List<ChewReferral>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val chewReferralList = ArrayList<ChewReferral>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val chewReferral = ChewReferral()

                chewReferral.id = cursor.getString(0)
                chewReferral.name = cursor.getString(1)
                chewReferral.phone = cursor.getString(2)
                chewReferral.title = cursor.getString(3)
                chewReferral.country = cursor.getString(4)
                chewReferral.recruitmentId = cursor.getString(5)
                chewReferral.synced = cursor.getInt(6)
                chewReferral.county = cursor.getString(7)
                chewReferral.district = cursor.getString(8)
                chewReferral.subCounty = cursor.getString(9)
                chewReferral.communityUnit = cursor.getString(10)
                chewReferral.village = cursor.getString(11)
                chewReferral.mapping = cursor.getString(12)
                chewReferral.mobilization = cursor.getString(13)
                chewReferral.lat = cursor.getString(14)
                chewReferral.lon = cursor.getString(15)

                chewReferralList.add(chewReferral)
                cursor.moveToNext()
            }
            db.close()
            return chewReferralList
        }


    val chewReferralCursor: Cursor
        get() {

            val db = readableDatabase

            return db.query(TABLE_NAME, columns, null, null, null, null, null, null)
        }

    val chewReferralJson: JSONObject
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

    fun addChewReferral(chewReferral: ChewReferral): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, chewReferral.id)
        cv.put(NAME, chewReferral.name)
        cv.put(PHONE, chewReferral.phone)
        cv.put(TITLE, chewReferral.title)
        cv.put(COUNTRY, chewReferral.country)
        cv.put(RECRUITMENT, chewReferral.recruitmentId)
        cv.put(SYNCED, chewReferral.synced)
        cv.put(COUNTY, chewReferral.county)
        cv.put(DISTRICT, chewReferral.district)
        cv.put(SUBCOUNTY, chewReferral.subCounty)
        cv.put(COMMUNITY_UNIT, chewReferral.communityUnit)
        cv.put(VILLAGE, chewReferral.village)
        cv.put(MAPPING, chewReferral.mapping)
        cv.put(MOBILIZATION, chewReferral.mobilization)
        cv.put(LAT, chewReferral.lat)
        cv.put(LON, chewReferral.lon)
        //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
        val id: Long
        if (isExist(chewReferral)) {
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + chewReferral.id + "'", null).toLong()
            Log.d("Tremap DB Op", "CHEW Referral updated")
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Tremap DB Op", "CHEW Referral created")
        }
        db.close()
        return id

    }

    fun isExist(chewReferral: ChewReferral): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + chewReferral.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    fun getChewReferralByRecruitmentId(recruitmentId: String): List<ChewReferral> {

        val db = readableDatabase

        val whereClause = "$RECRUITMENT = ?"
        val whereArgs = arrayOf(recruitmentId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val chewReferralList = ArrayList<ChewReferral>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val chewReferral = ChewReferral()

            chewReferral.id = cursor.getString(0)
            chewReferral.name = cursor.getString(1)
            chewReferral.phone = cursor.getString(2)
            chewReferral.title = cursor.getString(3)
            chewReferral.country = cursor.getString(4)
            chewReferral.recruitmentId = cursor.getString(5)
            chewReferral.synced = cursor.getInt(6)
            //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
            chewReferral.county = cursor.getString(7)
            chewReferral.district = cursor.getString(8)
            chewReferral.subCounty = cursor.getString(9)
            chewReferral.communityUnit = cursor.getString(10)
            chewReferral.village = cursor.getString(11)
            chewReferral.mapping = cursor.getString(12)
            chewReferral.mobilization = cursor.getString(13)
            chewReferral.lat = cursor.getString(14)
            chewReferral.lon = cursor.getString(15)

            chewReferralList.add(chewReferral)
            cursor.moveToNext()
        }
        db.close()
        return chewReferralList
    }

    fun getChewReferralByMappingId(mappingId: String): List<ChewReferral> {

        val db = readableDatabase

        val whereClause = "$MAPPING = ?"
        val whereArgs = arrayOf(mappingId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val chewReferralList = ArrayList<ChewReferral>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val chewReferral = ChewReferral()

            chewReferral.id = cursor.getString(0)
            chewReferral.name = cursor.getString(1)
            chewReferral.phone = cursor.getString(2)
            chewReferral.title = cursor.getString(3)
            chewReferral.country = cursor.getString(4)
            chewReferral.recruitmentId = cursor.getString(5)
            chewReferral.synced = cursor.getInt(6)
            //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
            chewReferral.county = cursor.getString(7)
            chewReferral.district = cursor.getString(8)
            chewReferral.subCounty = cursor.getString(9)
            chewReferral.communityUnit = cursor.getString(10)
            chewReferral.village = cursor.getString(11)
            chewReferral.mapping = cursor.getString(12)
            chewReferral.mobilization = cursor.getString(13)
            chewReferral.lat = cursor.getString(14)
            chewReferral.lon = cursor.getString(15)

            chewReferralList.add(chewReferral)
            cursor.moveToNext()
        }
        db.close()
        return chewReferralList
    }

    fun getChewReferralBySubCountyId(subCounty: String): List<ChewReferral> {

        val db = readableDatabase

        val whereClause = "$SUBCOUNTY = ?"
        val whereArgs = arrayOf(subCounty)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val chewReferralList = ArrayList<ChewReferral>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val chewReferral = ChewReferral()

            chewReferral.id = cursor.getString(0)
            chewReferral.name = cursor.getString(1)
            chewReferral.phone = cursor.getString(2)
            chewReferral.title = cursor.getString(3)
            chewReferral.country = cursor.getString(4)
            chewReferral.recruitmentId = cursor.getString(5)
            chewReferral.synced = cursor.getInt(6)
            //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
            chewReferral.county = cursor.getString(7)
            chewReferral.district = cursor.getString(8)
            chewReferral.subCounty = cursor.getString(9)
            chewReferral.communityUnit = cursor.getString(10)
            chewReferral.village = cursor.getString(11)
            chewReferral.mapping = cursor.getString(12)
            chewReferral.mobilization = cursor.getString(13)
            chewReferral.lat = cursor.getString(14)
            chewReferral.lon = cursor.getString(15)

            chewReferralList.add(chewReferral)
            cursor.moveToNext()
        }
        db.close()
        return chewReferralList
    }

    fun getChewReferralByPhone(phone: String): List<ChewReferral> {

        val db = readableDatabase

        val whereClause = "$PHONE = ?"
        val whereArgs = arrayOf(phone)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val chewReferralList = ArrayList<ChewReferral>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val chewReferral = ChewReferral()

            chewReferral.id = cursor.getString(0)
            chewReferral.name = cursor.getString(1)
            chewReferral.phone = cursor.getString(2)
            chewReferral.title = cursor.getString(3)
            chewReferral.country = cursor.getString(4)
            chewReferral.recruitmentId = cursor.getString(5)
            chewReferral.synced = cursor.getInt(6)
            chewReferral.county = cursor.getString(7)
            chewReferral.district = cursor.getString(8)
            chewReferral.subCounty = cursor.getString(9)
            chewReferral.communityUnit = cursor.getString(10)
            chewReferral.village = cursor.getString(11)
            chewReferral.mapping = cursor.getString(12)
            chewReferral.mobilization = cursor.getString(13)
            chewReferral.lat = cursor.getString(14)
            chewReferral.lon = cursor.getString(15)

            chewReferralList.add(chewReferral)
            cursor.moveToNext()
        }
        db.close()
        return chewReferralList
    }

    fun getChewReferralById(id: String): ChewReferral? {

        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val chewReferral = ChewReferral()
            chewReferral.id = cursor.getString(0)
            chewReferral.name = cursor.getString(1)
            chewReferral.phone = cursor.getString(2)
            chewReferral.title = cursor.getString(3)
            chewReferral.country = cursor.getString(4)
            chewReferral.recruitmentId = cursor.getString(5)
            chewReferral.synced = cursor.getInt(6)
            chewReferral.county = cursor.getString(7)
            chewReferral.district = cursor.getString(8)
            chewReferral.subCounty = cursor.getString(9)
            chewReferral.communityUnit = cursor.getString(10)
            chewReferral.village = cursor.getString(11)
            chewReferral.mapping = cursor.getString(12)
            chewReferral.mobilization = cursor.getString(13)
            chewReferral.lat = cursor.getString(14)
            chewReferral.lon = cursor.getString(15)

            db.close()

            /**
             * @TODO: Add all registrations referred by this user.
             * When one retrieves a referral, it would be good to get all the registrations that (s)he
             * has referred
             */
            return chewReferral
        }

    }

    fun fromJson(jsonObject: JSONObject) {
        val chewReferral = ChewReferral()
        try {

            if (!jsonObject.isNull(MAPPING)) {
                chewReferral.id = jsonObject.getString(ID)
            }
            if (!jsonObject.isNull(MAPPING)) {
                chewReferral.name = jsonObject.getString(NAME)
            }
            if (!jsonObject.isNull(MAPPING)) {
                chewReferral.phone = jsonObject.getString(PHONE)
            }
            if (!jsonObject.isNull(TITLE)) {
                chewReferral.title = jsonObject.getString(TITLE)
            }
            if (!jsonObject.isNull(COUNTRY)) {
                chewReferral.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(RECRUITMENT)) {
                chewReferral.recruitmentId = jsonObject.getString(RECRUITMENT)
            }
            if (!jsonObject.isNull(SYNCED)) {
                chewReferral.synced = jsonObject.getInt(SYNCED)
            }
            if (!jsonObject.isNull(COUNTY)) {
                chewReferral.county = jsonObject.getString(COUNTY)
            }
            if (!jsonObject.isNull(DISTRICT)) {
                chewReferral.district = jsonObject.getString(DISTRICT)
            }
            if (!jsonObject.isNull(SUBCOUNTY)) {
                chewReferral.subCounty = jsonObject.getString(SUBCOUNTY)
            }
            if (!jsonObject.isNull(COMMUNITY_UNIT)) {
                chewReferral.communityUnit = jsonObject.getString(COMMUNITY_UNIT)
            }
            if (!jsonObject.isNull(VILLAGE)) {
                chewReferral.village = jsonObject.getString(VILLAGE)
            }
            if (!jsonObject.isNull(MAPPING)) {
                chewReferral.mapping = jsonObject.getString(MAPPING)
            }
            if (!jsonObject.isNull(MOBILIZATION)) {
                chewReferral.mobilization = jsonObject.getString(MOBILIZATION)
            }
            if (!jsonObject.isNull(LAT)) {
                chewReferral.lat = jsonObject.getString(LAT)
            }
            if (!jsonObject.isNull(LON)) {
                chewReferral.lon = jsonObject.getString(LON)
            }

            addChewReferral(chewReferral)
        } catch (e: Exception) {
            Log.d("Tremap ChewReferral ERR", "From Json : " + e.message)
        }

    }

    fun deleteChewReferral(chewReferral: ChewReferral) {
        val db = readableDatabase
        db.delete(TABLE_NAME, "$ID = ?", arrayOf(chewReferral.id))
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "chew_referral"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        //String email, username, password, name;

        var varchar_field = " varchar(512) "
        var primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "


        var JSON_ROOT = "chew_referrals"

        val ID = "id"
        val PHONE = "phone"
        val TITLE = "title"
        val RECRUITMENT = "recruitment"
        val NAME = "name"
        val COUNTRY = "country"
        val COUNTY = "county"
        val DISTRICT = "district"
        val SUBCOUNTY = "subcounty"
        val COMMUNITY_UNIT = "community_unit"
        val VILLAGE = "village"
        val MAPPING = "mapping"
        val LAT = "lat"
        val LON = "lon"
        val MOBILIZATION = "mobilization"
        val SYNCED = "synced"
        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ","
                + NAME + varchar_field + ", "
                + PHONE + varchar_field + ", "
                + TITLE + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + SYNCED + integer_field + ", "
                + COUNTY + varchar_field + ", "
                + DISTRICT + varchar_field + ", "
                + SUBCOUNTY + varchar_field + ", "
                + COMMUNITY_UNIT + varchar_field + ", "
                + VILLAGE + varchar_field + ", "
                + MAPPING + varchar_field + ", "
                + MOBILIZATION + varchar_field + ", "
                + LAT + varchar_field + ", "
                + LON + varchar_field + ", "
                + RECRUITMENT + varchar_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

