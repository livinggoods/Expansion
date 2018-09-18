package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Partners
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 8/16/17.
 */


class PartnersTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    internal var columns = arrayOf(ID, NAME, CONTACTPERSON, CONTACTPERSONPHONE, PARENT, MAPPINGID, DATEADDED, ADDEDBY, SYNCED, COUNTRY, COMMENT, ARCHIVED, COUNTY, SUBCOUNTY, COMMUNITYUNIT, VILLAGE)

    val partnersData: List<Partners>
        get() {
            val db = readableDatabase
            val c = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val partnersList = ArrayList<Partners>()
            c.moveToFirst()
            while (!c.isAfterLast) {
                val partners = Partners()
                partners.partnerID = c.getString(c.getColumnIndex(ID))
                partners.partnerName = c.getString(c.getColumnIndex(NAME))
                partners.contactPerson = c.getString(c.getColumnIndex(CONTACTPERSON))
                partners.contactPersonPhone = c.getString(c.getColumnIndex(CONTACTPERSONPHONE))
                partners.parent = c.getString(c.getColumnIndex(PARENT))
                partners.mappingId = c.getString(c.getColumnIndex(MAPPINGID))
                partners.country = c.getString(c.getColumnIndex(COUNTRY))
                partners.comment = c.getString(c.getColumnIndex(COMMENT))
                partners.isSynced = c.getInt(c.getColumnIndex(SYNCED)) == 1
                partners.isArchived = c.getInt(c.getColumnIndex(ARCHIVED)) == 1
                partners.dateAdded = c.getLong(c.getColumnIndex(DATEADDED))
                partners.addedBy = c.getLong(c.getColumnIndex(ADDEDBY))

                partnersList.add(partners)
                c.moveToNext()
            }
            db.close()
            return partnersList
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

    fun addData(partner: Partners): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, partner.partnerID)
        cv.put(NAME, partner.partnerName)
        cv.put(CONTACTPERSON, partner.contactPerson)
        cv.put(CONTACTPERSONPHONE, partner.contactPersonPhone)
        cv.put(PARENT, partner.parent)
        cv.put(MAPPINGID, partner.mappingId)
        cv.put(DATEADDED, partner.dateAdded)
        cv.put(ADDEDBY, partner.addedBy)
        cv.put(SYNCED, if (partner.isSynced) 1 else 0)
        cv.put(COUNTRY, partner.country)
        cv.put(COMMENT, partner.comment)
        cv.put(ARCHIVED, if (partner.isArchived) 1 else 0)


        val id: Long
        if (isPartnerExisting(partner)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + partner.partnerID + "'", null).toLong()
        } else {
            id = db.insert(TABLE_NAME, null, cv)
        }
        db.close()
        return id
    }

    fun isPartnerExisting(partner: Partners): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '"
                + partner.partnerID + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getPartnersBySubCounty(subCountyUUID: String): List<Partners> {

        val db = readableDatabase
        val whereClause = "$SUBCOUNTY = ? "
        val whereArgs = arrayOf(subCountyUUID)

        val c = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        val partnersList = ArrayList<Partners>()
        c.moveToFirst()
        while (!c.isAfterLast) {
            val partners = Partners()
            partners.partnerID = c.getString(c.getColumnIndex(ID))
            partners.partnerName = c.getString(c.getColumnIndex(NAME))
            partners.contactPerson = c.getString(c.getColumnIndex(CONTACTPERSON))
            partners.contactPersonPhone = c.getString(c.getColumnIndex(CONTACTPERSONPHONE))
            partners.parent = c.getString(c.getColumnIndex(PARENT))
            partners.mappingId = c.getString(c.getColumnIndex(MAPPINGID))
            partners.country = c.getString(c.getColumnIndex(COUNTRY))
            partners.comment = c.getString(c.getColumnIndex(COMMENT))
            partners.isSynced = c.getInt(c.getColumnIndex(SYNCED)) == 1
            partners.isArchived = c.getInt(c.getColumnIndex(ARCHIVED)) == 1
            partners.dateAdded = c.getLong(c.getColumnIndex(DATEADDED))
            partners.addedBy = c.getLong(c.getColumnIndex(ADDEDBY))

            partnersList.add(partners)
            c.moveToNext()
        }
        db.close()
        return partnersList
    }

    fun getPartnerById(partnerUUID: String): Partners? {
        val db = readableDatabase

        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(partnerUUID)
        val c = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!c.moveToFirst() || c.count == 0) {
            return null
        } else {
            val partners = Partners()
            partners.partnerID = c.getString(c.getColumnIndex(ID))
            partners.partnerName = c.getString(c.getColumnIndex(NAME))
            partners.contactPerson = c.getString(c.getColumnIndex(CONTACTPERSON))
            partners.contactPersonPhone = c.getString(c.getColumnIndex(CONTACTPERSONPHONE))
            partners.parent = c.getString(c.getColumnIndex(PARENT))
            partners.mappingId = c.getString(c.getColumnIndex(MAPPINGID))
            partners.country = c.getString(c.getColumnIndex(COUNTRY))
            partners.comment = c.getString(c.getColumnIndex(COMMENT))
            partners.isSynced = c.getInt(c.getColumnIndex(SYNCED)) == 1
            partners.isArchived = c.getInt(c.getColumnIndex(ARCHIVED)) == 1
            partners.dateAdded = c.getLong(c.getColumnIndex(DATEADDED))
            partners.addedBy = c.getLong(c.getColumnIndex(ADDEDBY))
            return partners
        }

    }


    fun fromJson(jsonObject: JSONObject) {
        val partners = Partners()
        try {

            partners.partnerID = jsonObject.getString(ID)
            partners.partnerName = jsonObject.getString(NAME)
            partners.contactPerson = jsonObject.getString(CONTACTPERSON)
            partners.contactPersonPhone = jsonObject.getString(CONTACTPERSONPHONE)
            partners.parent = jsonObject.getString(PARENT)
            partners.mappingId = jsonObject.getString(MAPPINGID)
            partners.country = jsonObject.getString(COUNTRY)
            partners.comment = jsonObject.getString(COMMENT)
            partners.isSynced = jsonObject.getInt(SYNCED) == 1
            partners.isArchived = jsonObject.getInt(ARCHIVED) == 1
            partners.dateAdded = jsonObject.getLong(DATEADDED)
            partners.addedBy = jsonObject.getLong(ADDEDBY)

            addData(partners)
        } catch (e: Exception) {
            Log.d("Tremap", "+++++++++++++++++++++++++++++++++++++++")
            Log.d("Tremap", "CE ERROR " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "partners"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var integer_field = " integer default 0 "
        var text_field = " text "

        var JSON_ROOT = "community_unit"

        val ID = "id"
        val NAME = "name"
        val CONTACTPERSON = "contact_person"
        val CONTACTPERSONPHONE = "contact_person_phone"
        val PARENT = "parent"
        val MAPPINGID = "mapping_id"
        val DATEADDED = "client_time"
        val ADDEDBY = "added_by"
        val SYNCED = "synced"
        val COUNTRY = "country"
        val COUNTY = "county"
        val SUBCOUNTY = "subcounty"
        val COMMUNITYUNIT = "community_unit"
        val VILLAGE = "village"
        val COMMENT = "comment"
        val ARCHIVED = "archived"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ","
                + NAME + varchar_field + ","
                + CONTACTPERSON + varchar_field + ","
                + CONTACTPERSONPHONE + varchar_field + ","
                + PARENT + varchar_field + ","
                + MAPPINGID + varchar_field + ","
                + DATEADDED + integer_field + ","
                + ADDEDBY + integer_field + ","
                + SYNCED + integer_field + ","
                + COUNTRY + varchar_field + ","
                + COUNTY + varchar_field + ","
                + SUBCOUNTY + varchar_field + ","
                + COMMUNITYUNIT + varchar_field + ","
                + VILLAGE + varchar_field + ","
                + COMMENT + varchar_field + ","
                + ARCHIVED + integer_field + ");")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

