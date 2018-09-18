package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

/**
 * Created by kimaru on 5/23/17.
 */


class ParishTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    var columns = arrayOf(ID, PARISHNAME, COUNTRY, PARENT_LOCATION, MAPPINGID, ADDED_BY, CONTACTPERSON, CONTACTPERSONPHONE, COMMENT, SYNCED, DATE_ADDED)
    val parishData: List<Parish>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val parishList = ArrayList<Parish>()


            cursor.moveToFirst()
            while (!cursor.isAfterLast) {


                val parish = Parish()

                parish.id = cursor.getString(0)
                parish.name = cursor.getString(1)
                parish.country = cursor.getString(2)
                parish.parent = cursor.getString(3)
                parish.mapping = cursor.getString(4)
                parish.addedBy = cursor.getInt(5)
                parish.contactPerson = cursor.getString(6)
                parish.contactPersonPhone = cursor.getString(7)
                parish.comment = cursor.getString(8)
                parish.synced = cursor.getInt(9)
                parish.dateAdded = cursor.getLong(10)

                parishList.add(parish)
                cursor.moveToNext()
            }
            db.close()

            return parishList
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
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addData(parish: Parish): String {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, parish.id)
        cv.put(PARISHNAME, parish.name)
        cv.put(COUNTRY, parish.country)
        cv.put(PARENT_LOCATION, parish.parent)
        cv.put(MAPPINGID, parish.mapping)
        cv.put(ADDED_BY, parish.addedBy)
        cv.put(CONTACTPERSON, parish.contactPerson)
        cv.put(CONTACTPERSONPHONE, parish.contactPersonPhone)
        cv.put(COMMENT, parish.comment)
        cv.put(SYNCED, parish.synced)
        cv.put(DATE_ADDED, parish.dateAdded)
        val id: Long
        if (isExist(parish)) {
            id = db.update(TABLE_NAME, cv, ID + " = '" + parish.id + "'", null).toLong()
            Log.e("expansion parish table ", "Updated ID : " + parish.id.toString())
        } else {
            id = db.insert(TABLE_NAME, null, cv)
            Log.e("expansion parish ", "New record - ID is " + id.toString())
        }
        db.close()
        return id.toString()

    }

    fun isExist(parish: Parish): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + parish.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getParishById(uuid: String): Parish {
        val parish = Parish()
        val selection = arrayOf(uuid)
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, columns, ID, selection, null, null, null, null)
        parish.id = cursor.getString(0)
        parish.name = cursor.getString(1)
        parish.country = cursor.getString(2)
        parish.parent = cursor.getString(3)
        parish.mapping = cursor.getString(4)
        parish.addedBy = cursor.getInt(5)
        parish.contactPerson = cursor.getString(6)
        parish.contactPersonPhone = cursor.getString(7)
        parish.comment = cursor.getString(8)
        parish.synced = cursor.getInt(9)
        parish.dateAdded = cursor.getLong(10)
        return parish
    }

    fun getParishByCountry(countryCode: String): List<Parish> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$COUNTRY = ?"
        val whereArgs = arrayOf(countryCode)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val parishList = ArrayList<Parish>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val parish = Parish()
            parish.id = cursor.getString(0)
            parish.name = cursor.getString(1)
            parish.country = cursor.getString(2)
            parish.parent = cursor.getString(3)
            parish.mapping = cursor.getString(4)
            parish.addedBy = cursor.getInt(5)
            parish.contactPerson = cursor.getString(6)
            parish.contactPersonPhone = cursor.getString(7)
            parish.comment = cursor.getString(8)
            parish.synced = cursor.getInt(9)
            parish.dateAdded = cursor.getLong(10)

            parishList.add(parish)
            cursor.moveToNext()
        }
        db.close()

        return parishList
    }

    fun getParishByParent(parentUuid: String): List<Parish> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$PARENT_LOCATION = ?"
        val whereArgs = arrayOf(parentUuid)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val parishList = ArrayList<Parish>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val parish = Parish()
            parish.id = cursor.getString(0)
            parish.name = cursor.getString(1)
            parish.country = cursor.getString(2)
            parish.parent = cursor.getString(3)
            parish.mapping = cursor.getString(4)
            parish.addedBy = cursor.getInt(5)
            parish.contactPerson = cursor.getString(6)
            parish.contactPersonPhone = cursor.getString(7)
            parish.comment = cursor.getString(8)
            parish.synced = cursor.getInt(9)
            parish.dateAdded = cursor.getLong(10)

            parishList.add(parish)
            cursor.moveToNext()
        }
        db.close()

        return parishList
    }

    fun getParishByMapping(mappingId: String): List<Parish> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$MAPPINGID = ?"
        val whereArgs = arrayOf(mappingId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val parishList = ArrayList<Parish>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val parish = Parish()
            parish.id = cursor.getString(0)
            parish.name = cursor.getString(1)
            parish.country = cursor.getString(2)
            parish.parent = cursor.getString(3)
            parish.mapping = cursor.getString(4)
            parish.addedBy = cursor.getInt(5)
            parish.contactPerson = cursor.getString(6)
            parish.contactPersonPhone = cursor.getString(7)
            parish.comment = cursor.getString(8)
            parish.synced = cursor.getInt(9)
            parish.dateAdded = cursor.getLong(10)

            parishList.add(parish)
            cursor.moveToNext()
        }
        db.close()

        return parishList
    }

    fun fromJson(jsonObject: JSONObject) {
        val parish = Parish()
        try {

            if (!jsonObject.isNull(ID)) {
                parish.id = jsonObject.getString(ID)
            }
            if (!jsonObject.isNull(PARISHNAME)) {
                parish.name = jsonObject.getString(PARISHNAME)
            }
            if (!jsonObject.isNull(CONTACTPERSON)) {
                parish.contactPerson = jsonObject.getString(CONTACTPERSON)
            }
            if (!jsonObject.isNull(CONTACTPERSONPHONE)) {
                parish.contactPersonPhone = jsonObject.getString(CONTACTPERSONPHONE)
            }
            if (!jsonObject.isNull(PARENT_LOCATION)) {
                parish.parent = jsonObject.getString(PARENT_LOCATION)
            }
            if (!jsonObject.isNull(MAPPING_ID)) {
                parish.mapping = jsonObject.getString(MAPPING_ID)
            }
            if (!jsonObject.isNull(DATE_ADDED)) {
                //Wed, 31 Jan 2018 16:23:07
                val df = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z")
                val date = df.parse(jsonObject.getString(DATE_ADDED))
                val epoch = date.time
                parish.dateAdded = epoch
            }
            //parish.setDateAdded(jsonObject.getLong(DATE_ADDED));
            if (!jsonObject.isNull(ADDED_BY)) {
                parish.addedBy = jsonObject.getInt(ADDED_BY)
            }
            if (!jsonObject.isNull(SYNCED)) {
                parish.synced = jsonObject.getInt(SYNCED)
            }
            if (!jsonObject.isNull(COUNTRY)) {
                parish.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(COMMENT)) {
                parish.comment = jsonObject.getString(COMMENT)
            }
            addData(parish)
        } catch (e: Exception) {
            Log.d("Tremap ERR", "Parish from JSON " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {
        // add column
        db.execSQL(DB_UPDATE_V2)
    }

    companion object {

        val TABLE_NAME = "parish"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "id"
        val PARISHNAME = "name"
        val PARENT_LOCATION = "parent"
        val MAPPINGID = "mapping"
        val MAPPING_ID = "mapping_id"
        val ADDED_BY = "added_by"
        val CONTACTPERSON = "contact_person"
        val CONTACTPERSONPHONE = "phone"
        val COMMENT = "comment"
        val SYNCED = "synced"
        val COUNTRY = "country"
        val DATE_ADDED = "date_added"

        val JSON_ROOT = "parishes"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + PARISHNAME + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + PARENT_LOCATION + varchar_field + ", "
                + MAPPINGID + varchar_field + ", "
                + ADDED_BY + integer_field + ", "
                + CONTACTPERSON + varchar_field + ", "
                + CONTACTPERSONPHONE + varchar_field + ", "
                + COMMENT + text_field + ", "
                + SYNCED + integer_field + ", "
                + DATE_ADDED + integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
        val DB_UPDATE_V2 = CREATE_DATABASE
    }
}