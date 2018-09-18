package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.sync.ApiClient

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class CountyLocationTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    internal var columns = arrayOf(ID, NAME, ADMIN_NAME, CODE, COUNTRY, LAT, LON, META, PARENT, POLYGON, ARCHIVED)
    val locationData: List<CountyLocation>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val countyLocationList = ArrayList<CountyLocation>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {

                val countyLocation = CountyLocation()

                countyLocation.id = cursor.getInt(0)
                countyLocation.name = cursor.getString(1)
                countyLocation.adminName = cursor.getString(2)
                countyLocation.code = cursor.getString(3)
                countyLocation.country = cursor.getString(4)
                countyLocation.lat = cursor.getString(5)
                countyLocation.lon = cursor.getString(6)
                countyLocation.meta = cursor.getString(7)
                countyLocation.parent = cursor.getInt(8)
                countyLocation.polygon = cursor.getString(9)
                countyLocation.archived = cursor.getInt(10)

                countyLocationList.add(countyLocation)
                cursor.moveToNext()
            }
            db.close()

            return countyLocationList
        }

    val counties: List<CountyLocation>
        get() {
            val db = readableDatabase
            val orderBy = "$NAME asc"
            val whereClause = "$ADMIN_NAME = ?"
            val whereArgs = arrayOf("County")
            val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

            val countyLocationList = ArrayList<CountyLocation>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val countyLocation = CountyLocation()

                countyLocation.id = cursor.getInt(0)
                countyLocation.name = cursor.getString(1)
                countyLocation.adminName = cursor.getString(2)
                countyLocation.code = cursor.getString(3)
                countyLocation.country = cursor.getString(4)
                countyLocation.lat = cursor.getString(5)
                countyLocation.lon = cursor.getString(6)
                countyLocation.meta = cursor.getString(7)
                countyLocation.parent = cursor.getInt(8)
                countyLocation.polygon = cursor.getString(9)
                countyLocation.archived = cursor.getInt(10)

                countyLocationList.add(countyLocation)
                cursor.moveToNext()
            }
            db.close()

            return countyLocationList
        }


    val regions: List<CountyLocation>
        get() {
            val db = readableDatabase
            val orderBy = "$NAME asc"
            val whereClause = "$ADMIN_NAME = ?"
            val whereArgs = arrayOf("Region")
            val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

            val countyLocationList = ArrayList<CountyLocation>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                countyLocationList.add(cursorToLocation(cursor))
                cursor.moveToNext()
            }
            db.close()
            return countyLocationList
        }

    val countiesAndDistricts: List<CountyLocation>
        get() {
            val db = readableDatabase
            val orderBy = "$NAME asc"
            val whereClause = ADMIN_NAME + " = ? OR " +
                    ADMIN_NAME + " = ? "
            val whereArgs = arrayOf("County", "District")
            val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

            val countyLocationList = ArrayList<CountyLocation>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val countyLocation = CountyLocation()

                countyLocation.id = cursor.getInt(0)
                countyLocation.name = cursor.getString(1)
                countyLocation.adminName = cursor.getString(2)
                countyLocation.code = cursor.getString(3)
                countyLocation.country = cursor.getString(4)
                countyLocation.lat = cursor.getString(5)
                countyLocation.lon = cursor.getString(6)
                countyLocation.meta = cursor.getString(7)
                countyLocation.parent = cursor.getInt(8)
                countyLocation.polygon = cursor.getString(9)
                countyLocation.archived = cursor.getInt(10)

                countyLocationList.add(countyLocation)
                cursor.moveToNext()
            }
            db.close()

            return countyLocationList
        }
    val profilesCount: Long
        get() {
            val db = this.readableDatabase
            val cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME)
            db.close()
            return cnt
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

    fun reCreateDb() {
        val db = writableDatabase
        db.execSQL(DATABASE_DROP)
        db.execSQL(CREATE_DATABASE)
    }

    fun addData(countyLocation: CountyLocation): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, countyLocation.id)
        cv.put(NAME, countyLocation.name)
        cv.put(ADMIN_NAME, countyLocation.adminName)
        cv.put(CODE, countyLocation.country)
        cv.put(COUNTRY, countyLocation.code)
        cv.put(LAT, countyLocation.lat)
        cv.put(LON, countyLocation.lon)
        cv.put(META, countyLocation.meta)
        cv.put(PARENT, countyLocation.parent)
        cv.put(POLYGON, countyLocation.polygon)
        cv.put(ARCHIVED, countyLocation.archived)

        val id: Long
        if (isExist(countyLocation)) {
            id = db.update(TABLE_NAME, cv, ID + " = '" + countyLocation.id + "'", null).toLong()
            Log.e("expansion ugcountytable", "Updated ID : " + countyLocation.id.toString())
        } else {
            id = db.insert(TABLE_NAME, null, cv)
            Log.e("expansion ugcountytable", "New record - ID is " + id.toString())
        }
        db.close()
        return id

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val countyLocation = CountyLocation()
            countyLocation.id = jsonObject.getInt(ID)
            countyLocation.name = jsonObject.getString(NAME)
            countyLocation.adminName = jsonObject.getString(ADMIN_NAME)
            countyLocation.code = jsonObject.getString(CODE)
            countyLocation.country = jsonObject.getString(COUNTRY)
            countyLocation.lon = jsonObject.getString(LON)
            countyLocation.lat = jsonObject.getString(LAT)
            countyLocation.meta = jsonObject.getString(META)
            countyLocation.parent = jsonObject.getInt(PARENT)
            countyLocation.polygon = jsonObject.getString(POLYGON)
            countyLocation.archived = jsonObject.getInt(ARCHIVED)
            this.addData(countyLocation)
        } catch (e: Exception) {
        }

    }

    fun getLocationById(id: String): CountyLocation? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val columns = arrayOf(ID, NAME, ADMIN_NAME, CODE, COUNTRY, LAT, LON, META, PARENT, POLYGON, ARCHIVED)
            val countyLocation = CountyLocation()
            countyLocation.id = cursor.getInt(0)
            countyLocation.name = cursor.getString(1)
            countyLocation.adminName = cursor.getString(2)
            countyLocation.code = cursor.getString(3)
            countyLocation.country = cursor.getString(4)
            countyLocation.lat = cursor.getString(5)
            countyLocation.lon = cursor.getString(6)
            countyLocation.meta = cursor.getString(7)
            countyLocation.parent = cursor.getInt(8)
            countyLocation.polygon = cursor.getString(9)
            countyLocation.archived = cursor.getInt(10)
            cursor.close()
            return countyLocation
        }

    }

    fun getDistrictByName(name: String): CountyLocation? {
        val db = readableDatabase
        val whereClause = NAME + " = ? AND (" +
                ADMIN_NAME + " = ? OR " +
                ADMIN_NAME + " = ? ) "
        val whereArgs = arrayOf(name, "District", "County")
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val columns = arrayOf(ID, NAME, ADMIN_NAME, CODE, COUNTRY, LAT, LON, META, PARENT, POLYGON, ARCHIVED)
            val countyLocation = CountyLocation()
            countyLocation.id = cursor.getInt(0)
            countyLocation.name = cursor.getString(1)
            countyLocation.adminName = cursor.getString(2)
            countyLocation.code = cursor.getString(3)
            countyLocation.country = cursor.getString(4)
            countyLocation.lat = cursor.getString(5)
            countyLocation.lon = cursor.getString(6)
            countyLocation.meta = cursor.getString(7)
            countyLocation.parent = cursor.getInt(8)
            countyLocation.polygon = cursor.getString(9)
            countyLocation.archived = cursor.getInt(10)
            cursor.close()
            return countyLocation
        }

    }

    fun getCountyOrDistrictByName(name: String): CountyLocation? {
        val db = readableDatabase
        val whereClause = NAME + " = ? AND (" +
                ADMIN_NAME + " = ? OR " +
                ADMIN_NAME + " = ? ) "
        val whereArgs = arrayOf(name, "County", "DISTRICT")
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val columns = arrayOf(ID, NAME, ADMIN_NAME, CODE, COUNTRY, LAT, LON, META, PARENT, POLYGON, ARCHIVED)
            val countyLocation = CountyLocation()
            countyLocation.id = cursor.getInt(0)
            countyLocation.name = cursor.getString(1)
            countyLocation.adminName = cursor.getString(2)
            countyLocation.code = cursor.getString(3)
            countyLocation.country = cursor.getString(4)
            countyLocation.lat = cursor.getString(5)
            countyLocation.lon = cursor.getString(6)
            countyLocation.meta = cursor.getString(7)
            countyLocation.parent = cursor.getInt(8)
            countyLocation.polygon = cursor.getString(9)
            countyLocation.archived = cursor.getInt(10)
            cursor.close()
            return countyLocation
        }

    }

    fun getCountyByName(name: String): CountyLocation? {
        val db = readableDatabase
        val whereClause = NAME + " = ? AND " +
                ADMIN_NAME + " = ? "
        val whereArgs = arrayOf(name, "County")
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val columns = arrayOf(ID, NAME, ADMIN_NAME, CODE, COUNTRY, LAT, LON, META, PARENT, POLYGON, ARCHIVED)
            val countyLocation = CountyLocation()
            countyLocation.id = cursor.getInt(0)
            countyLocation.name = cursor.getString(1)
            countyLocation.adminName = cursor.getString(2)
            countyLocation.code = cursor.getString(3)
            countyLocation.country = cursor.getString(4)
            countyLocation.lat = cursor.getString(5)
            countyLocation.lon = cursor.getString(6)
            countyLocation.meta = cursor.getString(7)
            countyLocation.parent = cursor.getInt(8)
            countyLocation.polygon = cursor.getString(9)
            countyLocation.archived = cursor.getInt(10)
            cursor.close()
            return countyLocation
        }

    }

    fun getChildrenLocations(parentId: String): List<CountyLocation> {
        val db = readableDatabase
        val orderBy = "$NAME asc"
        val whereClause = "$PARENT = ?"
        val whereArgs = arrayOf(parentId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

        val countyLocationList = ArrayList<CountyLocation>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val countyLocation = CountyLocation()

            countyLocation.id = cursor.getInt(0)
            countyLocation.name = cursor.getString(1)
            countyLocation.adminName = cursor.getString(2)
            countyLocation.code = cursor.getString(3)
            countyLocation.country = cursor.getString(4)
            countyLocation.lat = cursor.getString(5)
            countyLocation.lon = cursor.getString(6)
            countyLocation.meta = cursor.getString(7)
            countyLocation.parent = cursor.getInt(8)
            countyLocation.polygon = cursor.getString(9)
            countyLocation.archived = cursor.getInt(10)

            countyLocationList.add(countyLocation)
            cursor.moveToNext()
        }
        db.close()

        return countyLocationList
    }

    fun getChildrenLocations(parentCountyLocation: CountyLocation): List<CountyLocation> {
        val db = readableDatabase
        val orderBy = "$NAME asc"
        val whereClause = "$PARENT = ?"
        val whereArgs = arrayOf(parentCountyLocation.id.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

        val countyLocationList = ArrayList<CountyLocation>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val countyLocation = CountyLocation()

            countyLocation.id = cursor.getInt(0)
            countyLocation.name = cursor.getString(1)
            countyLocation.adminName = cursor.getString(2)
            countyLocation.code = cursor.getString(3)
            countyLocation.country = cursor.getString(4)
            countyLocation.lat = cursor.getString(5)
            countyLocation.lon = cursor.getString(6)
            countyLocation.meta = cursor.getString(7)
            countyLocation.parent = cursor.getInt(8)
            countyLocation.polygon = cursor.getString(9)
            countyLocation.archived = cursor.getInt(10)

            countyLocationList.add(countyLocation)
            cursor.moveToNext()
        }
        db.close()

        return countyLocationList
    }


    fun isExist(countyLocation: CountyLocation): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + countyLocation.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun nextIncrementValue(): Long {
        val db = readableDatabase
        var id: Long? = 0L
        val c = db.rawQuery("select seq from sqlite_sequence WHERE name = '$TABLE_NAME'", null)
        if (c.moveToFirst()) {
            do {
                id = c.getLong(0)
            } while (c.moveToNext())
        }
        return id!! + 1
    }

    fun insertRawSql(name: String, admin_name: String, code: String, country: String, lat: String,
                     lon: String, meta: String, parent: Long?, polygon: String) {
        val db = readableDatabase
        db.rawQuery("INSERT INTO `location`(`id`,`name`,`admin_name`,`code`,`country`," +
                "`lat`,`lon`,`meta`,`parent`,`polygon`) VALUES (NULL,'" + name + "','" + admin_name + "','"
                + code + "','" + country + "','" + lat + "','" + lon + "','" + meta + "','" + parent + "','" + polygon + "');", null)
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {
        createLocations()
    }

    fun createLocations() {
        if (profilesCount < 1) {

        }

    }

    private fun cursorToLocation(cursor: Cursor): CountyLocation {
        val countyLocation = CountyLocation()

        countyLocation.id = cursor.getInt(cursor.getColumnIndex(ID))
        countyLocation.name = cursor.getString(cursor.getColumnIndex(NAME))
        countyLocation.adminName = cursor.getString(cursor.getColumnIndex(ADMIN_NAME))
        countyLocation.code = cursor.getString(cursor.getColumnIndex(CODE))
        countyLocation.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
        countyLocation.lat = cursor.getString(cursor.getColumnIndex(LAT))
        countyLocation.lon = cursor.getString(cursor.getColumnIndex(LON))
        countyLocation.meta = cursor.getString(cursor.getColumnIndex(META))
        countyLocation.parent = cursor.getInt(cursor.getColumnIndex(PARENT))
        countyLocation.polygon = cursor.getString(cursor.getColumnIndex(POLYGON))
        countyLocation.archived = cursor.getInt(cursor.getColumnIndex(ARCHIVED))
        return countyLocation
    }

    companion object {
        val TABLE_NAME = "location"
        val JSON_ROOT = "locations"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var real_field = " REAL "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "id"
        val ADMIN_NAME = "admin_name"
        val ARCHIVED = "archived"
        val CODE = "code"
        val COUNTRY = "country"
        val LAT = "lat"
        val LON = "lon"
        val META = "meta"
        val NAME = "name"
        val PARENT = "parent"
        val POLYGON = "polygon"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL " + ", "
                + NAME + varchar_field + ", "
                + ADMIN_NAME + varchar_field + ", "
                + CODE + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + LAT + varchar_field + ", "
                + LON + varchar_field + ", "
                + META + text_field + ", "
                + PARENT + real_field + ", "
                + POLYGON + text_field + ", "
                + ARCHIVED + integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

}

