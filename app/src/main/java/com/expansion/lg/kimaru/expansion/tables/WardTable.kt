package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.mzigos.Ward
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 7/21/17.
 */

class WardTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    internal var columns = arrayOf(ID, NAME, COUNTY, SUBCOUNTY, ARCHIVED)

    val allWardsData: List<Ward>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val wardList = ArrayList<Ward>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val ward = Ward()

                ward.id = cursor.getString(cursor.getColumnIndex(ID))
                ward.name = cursor.getString(cursor.getColumnIndex(NAME))
                ward.county = cursor.getInt(cursor.getColumnIndex(COUNTY))
                ward.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
                ward.archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED))
                wardList.add(ward)
                cursor.moveToNext()
            }
            db.close()
            return wardList
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

        Log.w("Link Facility", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
        if (oldVersion < 3) {
            upgradeVersion3(db)
        }
    }


    fun addData(ward: Ward): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, ward.id)
        cv.put(NAME, ward.name)
        cv.put(COUNTY, ward.county)
        cv.put(SUBCOUNTY, ward.subCounty)
        cv.put(ARCHIVED, ward.archived)

        val id: Long
        if (isExist(ward)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + ward.id + "'", null).toLong()
            Log.d("Tremap DB Op", "Ward updated")
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Tremap DB Op", "Ward Created")
        }
        db.close()
        return id

    }

    fun isExist(ward: Ward): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + ward.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getWardsbySubCounty(subCounty: SubCounty): List<Ward> {

        val db = readableDatabase

        val whereClause = "$SUBCOUNTY = ? "

        val whereArgs = arrayOf(subCounty.id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val wardList = ArrayList<Ward>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val ward = Ward()
            ward.id = cursor.getString(cursor.getColumnIndex(ID))
            ward.name = cursor.getString(cursor.getColumnIndex(NAME))
            ward.county = cursor.getInt(cursor.getColumnIndex(COUNTY))
            ward.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
            ward.archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED))
            wardList.add(ward)
            cursor.moveToNext()
        }
        db.close()
        return wardList
    }

    fun getWardsbyCounty(countyId: Int?): List<Ward> {

        val db = readableDatabase

        val whereClause = "$COUNTY = ? "

        val whereArgs = arrayOf(countyId!!.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val wardList = ArrayList<Ward>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val ward = Ward()
            ward.id = cursor.getString(cursor.getColumnIndex(ID))
            ward.name = cursor.getString(cursor.getColumnIndex(NAME))
            ward.county = cursor.getInt(cursor.getColumnIndex(COUNTY))
            ward.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
            ward.archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED))
            wardList.add(ward)
            cursor.moveToNext()
        }
        db.close()
        return wardList
    }

    fun getWardById(uuid: String): Ward? {

        val db = readableDatabase

        val whereClause = "$ID = ? "

        val whereArgs = arrayOf(uuid)

        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val ward = Ward()
            ward.id = cursor.getString(cursor.getColumnIndex(ID))
            ward.name = cursor.getString(cursor.getColumnIndex(NAME))
            ward.county = cursor.getInt(cursor.getColumnIndex(COUNTY))
            ward.subCounty = cursor.getString(cursor.getColumnIndex(SUBCOUNTY))
            ward.archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED))
            db.close()
            return ward
        }
    }

    fun fromJson(jsonObject: JSONObject) {
        val ward = Ward()
        try {

            ward.id = jsonObject.getString(ID)
            var name = jsonObject.getString(NAME)
            if (!jsonObject.getString(NAME).equals("", ignoreCase = true)) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1)
            }
            ward.name = name
            ward.county = jsonObject.getInt(COUNTY)
            ward.subCounty = jsonObject.getString(SUBCOUNTY)
            ward.archived = jsonObject.getInt(ARCHIVED)
            addData(ward)
        } catch (e: Exception) {
            Log.d("Tremap ERR", "Ward from JSON " + e.message)
        }

    }

    fun toJson(cursor: Cursor): JSONObject {
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
        return results
    }


    fun upgradeVersion2(db: SQLiteDatabase) {}
    fun upgradeVersion3(db: SQLiteDatabase) {}

    companion object {
        val TABLE_NAME = "wards"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        var JSON_ROOT = "wards"

        val ID = "id"
        val NAME = "name"
        val COUNTY = "county"
        val SUBCOUNTY = "sub_county"
        val ARCHIVED = "archived"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + NAME + varchar_field + ", "
                + COUNTY + integer_field + ", "
                + SUBCOUNTY + varchar_field + ", "
                + ARCHIVED + integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}
