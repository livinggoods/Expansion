package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 8/16/17.
 */


class PartnerActivityTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    internal var columns = arrayOf(ID, PARTNERID, COUNTRY, COUNTY, SUBCOUNTY, PARISH, VILLAGE, COMMUNITYUNIT, MAPPINGID, COMMENT, DOINGMHEALTH, DOINGICCM, GIVINGFREEDRUGS, GIVINGSTIPEND, DATEADDED, ADDEDBY, ACTIVITIES, SYNCED)

    val partnerActivityData: List<PartnerActivity>
        get() {
            val db = readableDatabase
            val c = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val partnersActivityList = ArrayList<PartnerActivity>()
            c.moveToFirst()
            while (!c.isAfterLast) {
                partnersActivityList.add(cursorToPartner(c))
                c.moveToNext()
            }
            db.close()
            return partnersActivityList
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


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_DATABASE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addData(partnerActivity: PartnerActivity): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, partnerActivity.id)
        cv.put(PARTNERID, partnerActivity.partnerId)
        cv.put(COUNTRY, partnerActivity.country)
        cv.put(COUNTY, partnerActivity.county)
        cv.put(SUBCOUNTY, partnerActivity.subcounty)
        cv.put(PARISH, partnerActivity.parish)
        cv.put(VILLAGE, partnerActivity.village)
        cv.put(COMMUNITYUNIT, partnerActivity.communityUnit)
        cv.put(MAPPINGID, partnerActivity.mappingId)
        cv.put(COMMENT, partnerActivity.comment)
        cv.put(DOINGMHEALTH, if (partnerActivity.isDoingMhealth) 1 else 0)
        cv.put(DOINGICCM, if (partnerActivity.isDoingIccm) 1 else 0)
        cv.put(GIVINGFREEDRUGS, if (partnerActivity.isGivingFreeDrugs) 1 else 0)
        cv.put(GIVINGSTIPEND, if (partnerActivity.isGivingStipend) 1 else 0)
        cv.put(DATEADDED, partnerActivity.dateAdded)
        cv.put(ADDEDBY, partnerActivity.addedBy)
        cv.put(ACTIVITIES, partnerActivity.activities)
        cv.put(SYNCED, if (partnerActivity.isSynced) 1 else 0)


        val id: Long
        if (isPartnerExisting(partnerActivity)) {
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++")
            Log.d("Tremap", "Updating Partner Activity")
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++")
            id = db.update(TABLE_NAME, cv, ID + "='" + partnerActivity.id + "'", null).toLong()
        } else {
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++")
            Log.d("Tremap", "Creating Partner Activity")
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++")
            id = db.insert(TABLE_NAME, null, cv)
        }
        db.close()
        return id
    }

    fun isPartnerExisting(partnerActivity: PartnerActivity): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '"
                + partnerActivity.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getPartnerActivityByField(fieldName: String, fieldValue: String): List<PartnerActivity> {

        val db = readableDatabase
        val whereClause = "$fieldName = ? "
        val whereArgs = arrayOf(fieldValue)

        val c = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        val partnerActivityList = ArrayList<PartnerActivity>()
        c.moveToFirst()
        while (!c.isAfterLast) {
            partnerActivityList.add(cursorToPartner(c))
            c.moveToNext()
        }
        db.close()
        return partnerActivityList
    }

    fun getPartnerActivityById(uuid: String): PartnerActivity {

        val db = readableDatabase
        val whereClause = "$ID = ? "
        val whereArgs = arrayOf(uuid)
        val c = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        val partnerActivity = cursorToPartner(c)
        db.close()
        return partnerActivity
    }


    private fun cursorToPartner(c: Cursor): PartnerActivity {
        val partners = PartnerActivity()

        partners.id = c.getString(c.getColumnIndex(ID))
        partners.partnerId = c.getString(c.getColumnIndex(PARTNERID))
        partners.country = c.getString(c.getColumnIndex(COUNTRY))
        partners.county = c.getString(c.getColumnIndex(COUNTY))
        partners.subcounty = c.getString(c.getColumnIndex(SUBCOUNTY))
        partners.parish = c.getString(c.getColumnIndex(PARISH))
        partners.village = c.getString(c.getColumnIndex(VILLAGE))
        partners.communityUnit = c.getString(c.getColumnIndex(COMMUNITYUNIT))
        partners.mappingId = c.getString(c.getColumnIndex(MAPPINGID))
        partners.comment = c.getString(c.getColumnIndex(COMMENT))
        partners.isDoingMhealth = c.getInt(c.getColumnIndex(DOINGMHEALTH)) == 1
        partners.isDoingIccm = c.getInt(c.getColumnIndex(DOINGICCM)) == 1
        partners.isGivingFreeDrugs = c.getInt(c.getColumnIndex(GIVINGFREEDRUGS)) == 1
        partners.isGivingStipend = c.getInt(c.getColumnIndex(GIVINGSTIPEND)) == 1
        partners.dateAdded = c.getLong(c.getColumnIndex(ADDEDBY))
        partners.addedBy = c.getLong(c.getColumnIndex(ADDEDBY))
        partners.activities = c.getString(c.getColumnIndex(ACTIVITIES))
        partners.isSynced = c.getInt(c.getColumnIndex(SYNCED)) == 1
        return partners
    }


    fun fromJson(jsonObject: JSONObject) {
        val partners = PartnerActivity()
        try {
            partners.id = jsonObject.getString(ID)
            partners.partnerId = jsonObject.getString(PARTNERID)
            partners.country = jsonObject.getString(COUNTRY)
            partners.county = jsonObject.getString(COUNTY)
            partners.subcounty = jsonObject.getString(SUBCOUNTY)
            partners.parish = jsonObject.getString(PARISH)
            partners.village = jsonObject.getString(VILLAGE)
            partners.communityUnit = jsonObject.getString(COMMUNITYUNIT)
            partners.mappingId = jsonObject.getString(MAPPINGID)
            partners.comment = jsonObject.getString(COMMENT)
            partners.isDoingMhealth = jsonObject.getInt(DOINGMHEALTH) == 1
            partners.isDoingIccm = jsonObject.getInt(DOINGICCM) == 1
            partners.isGivingFreeDrugs = jsonObject.getInt(GIVINGFREEDRUGS) == 1
            partners.isGivingStipend = jsonObject.getInt(GIVINGSTIPEND) == 1
            partners.dateAdded = jsonObject.getLong(ADDEDBY)
            partners.addedBy = jsonObject.getLong(ADDEDBY)
            partners.activities = jsonObject.getString(ACTIVITIES)
            partners.isSynced = jsonObject.getInt(SYNCED) == 1

            addData(partners)
        } catch (e: Exception) {
            Log.d("Tremap", "+++++++++++++++++++++++++++++++++++++++")
            Log.d("Tremap", "CE ERROR " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "partner_activity"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var integer_field = " integer default 0 "
        var text_field = " text "

        var JSON_ROOT = "partner_activity"

        val ID = "id"
        val PARTNERID = "partner_id"
        val COUNTRY = "country"
        val COUNTY = "county_id"
        val SUBCOUNTY = "sub_county_id"
        val PARISH = "parish_id"
        val VILLAGE = "village_id"
        val COMMUNITYUNIT = "community_unit_id"
        val MAPPINGID = "mapping_id"
        val COMMENT = "comment"
        val DOINGMHEALTH = "doing_mhealth"
        val DOINGICCM = "doing_iccm"
        val GIVINGFREEDRUGS = "giving_free_drugs"
        val GIVINGSTIPEND = "giving_stipend"
        val DATEADDED = "date_added"
        val ADDEDBY = "added_by"
        val ACTIVITIES = "activities"
        val SYNCED = "synced"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ","
                + PARTNERID + varchar_field + ","
                + COUNTRY + varchar_field + ","
                + COUNTY + varchar_field + ","
                + SUBCOUNTY + varchar_field + ","
                + PARISH + varchar_field + ","
                + VILLAGE + varchar_field + ","
                + COMMUNITYUNIT + integer_field + ","
                + MAPPINGID + integer_field + ","
                + COMMENT + text_field + ","
                + DOINGMHEALTH + integer_field + ","
                + DOINGICCM + integer_field + ","
                + GIVINGFREEDRUGS + integer_field + ","
                + GIVINGSTIPEND + integer_field + ","
                + DATEADDED + integer_field + ","
                + ADDEDBY + integer_field + ","
                + ACTIVITIES + text_field + ","
                + SYNCED + integer_field + ");")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

