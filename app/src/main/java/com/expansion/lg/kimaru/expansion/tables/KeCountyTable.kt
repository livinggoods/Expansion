package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.KeCounty
import com.expansion.lg.kimaru.expansion.other.Constants

import java.util.ArrayList
import java.util.Date

/**
 * Created by kimaru on 5/18/17.
 */

class KeCountyTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    internal var columns = arrayOf(ID, COUNTYNAME, COUNTRY, LAT, LON, CONTACTPERSON, COUNTYCODE, CONTACTPERSONPHONE, MAINTOWN, COUNTYSUPPORT, CHVACTIVITY, CHVACTIVITYLEVEL, COUNTYPOPULATION, NOOFVILLAGES, MAINTOWNPOPULATION, SERVICEPOPULATION, POPULATIONDENSITY, TRANSPORTCOST, MAJORROADS, HEALTFACILITIES, PRIVATECLINICSINTOWN, PRIVATECLINICSINRADIUS, COMMUNITYUNITS, MAINSUPERMARKETS, MAINBANKS, ANYMAJORBUSINESS, COMMENTS, RECOMMENDED, DATEADDED, ADDEDBY, LGPRESENT, SYNCED)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_DATABASE)
        createKeCounties(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("referral", "upgrading database from" + oldVersion + "to" + newVersion)

        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addKeCounty(keCounty: KeCounty): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, keCounty.id)
        cv.put(COUNTYNAME, keCounty.countyName)
        cv.put(COUNTRY, keCounty.country)
        cv.put(LAT, keCounty.lat)
        cv.put(LON, keCounty.lon)
        cv.put(CONTACTPERSON, keCounty.contactPerson)
        cv.put(COUNTYCODE, keCounty.countyCode)
        cv.put(CONTACTPERSONPHONE, keCounty.contactPersonPhone)
        cv.put(MAINTOWN, keCounty.mainTown)
        cv.put(COUNTYSUPPORT, keCounty.countySupport)
        cv.put(CHVACTIVITY, if (keCounty.isChvActivity) 1 else 0)
        cv.put(CHVACTIVITYLEVEL, keCounty.chvActivityLevel)
        cv.put(COUNTYPOPULATION, keCounty.populationDensity)
        cv.put(NOOFVILLAGES, keCounty.noOfVillages)
        cv.put(MAINTOWNPOPULATION, keCounty.mainTownPopulation)
        cv.put(SERVICEPOPULATION, keCounty.servicePopulation)
        cv.put(POPULATIONDENSITY, keCounty.populationDensity)
        cv.put(TRANSPORTCOST, keCounty.transportCost)
        cv.put(MAJORROADS, keCounty.majorRoads)
        cv.put(HEALTFACILITIES, keCounty.healtFacilities)
        cv.put(PRIVATECLINICSINTOWN, keCounty.privateClinicsInTown)
        cv.put(PRIVATECLINICSINRADIUS, keCounty.privateClinicsInRadius)
        cv.put(COMMUNITYUNITS, keCounty.communityUnits)
        cv.put(MAINSUPERMARKETS, keCounty.mainSupermarkets)
        cv.put(MAINBANKS, keCounty.mainBanks)
        cv.put(ANYMAJORBUSINESS, keCounty.anyMajorBusiness)
        cv.put(COMMENTS, keCounty.comments)
        cv.put(RECOMMENDED, if (keCounty.isRecommended) 1 else 0)
        cv.put(DATEADDED, keCounty.dateAdded)
        cv.put(ADDEDBY, keCounty.addedBy)
        cv.put(LGPRESENT, if (keCounty.isLgPresent) 1 else 0)
        val id: Long
        if (isExist(keCounty)) {
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + keCounty.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()
        return id

    }

    fun isExist(keCounty: KeCounty): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + keCounty.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    fun getCounties(): List<KeCounty> {

        val db = readableDatabase

        val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

        val keCountyList = ArrayList<KeCounty>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val keCounty = KeCounty()
            keCounty.id = cursor.getInt(0)
            keCounty.countyName = cursor.getString(1)
            keCounty.country = cursor.getString(2)
            keCounty.lat = cursor.getDouble(3)
            keCounty.lon = cursor.getDouble(4)
            keCounty.contactPerson = cursor.getString(5)
            keCounty.countyCode = cursor.getString(6)
            keCounty.contactPersonPhone = cursor.getString(7)
            keCounty.mainTown = cursor.getString(8)
            keCounty.countySupport = cursor.getString(9)
            keCounty.chvActivityLevel = cursor.getString(11)
            keCounty.countyPopulation = cursor.getString(12)
            keCounty.noOfVillages = cursor.getLong(13)
            keCounty.mainTownPopulation = cursor.getLong(14)
            keCounty.servicePopulation = cursor.getLong(15)
            keCounty.populationDensity = cursor.getLong(16)
            keCounty.transportCost = cursor.getInt(17)
            keCounty.majorRoads = cursor.getString(18)
            keCounty.healtFacilities = cursor.getString(19)
            keCounty.privateClinicsInTown = cursor.getString(20)
            keCounty.privateClinicsInRadius = cursor.getString(21)
            keCounty.communityUnits = cursor.getString(22)
            keCounty.mainSupermarkets = cursor.getString(23)
            keCounty.mainBanks = cursor.getString(24)
            keCounty.anyMajorBusiness = cursor.getInt(25)
            keCounty.comments = cursor.getString(26)
            keCounty.isRecommended = cursor.getInt(27) == 1
            keCounty.dateAdded = cursor.getLong(28)
            keCounty.addedBy = cursor.getInt(29)
            keCounty.isLgPresent = cursor.getInt(30) == 1
            keCounty.isSynced = cursor.getInt(31) == 1
            keCountyList.add(keCounty)
            cursor.moveToNext()
        }
        db.close()
        return keCountyList
    }

    fun getCountyById(id: Int): KeCounty? {

        val db = readableDatabase
        val whereClause = "$ID = ? "
        val whereArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val keCounty = KeCounty()

            keCounty.id = cursor.getInt(0)
            keCounty.countyName = cursor.getString(1)
            keCounty.country = cursor.getString(2)
            keCounty.lat = cursor.getDouble(3)
            keCounty.lon = cursor.getDouble(4)
            keCounty.contactPerson = cursor.getString(5)
            keCounty.countyCode = cursor.getString(6)
            keCounty.contactPersonPhone = cursor.getString(7)
            keCounty.mainTown = cursor.getString(8)
            keCounty.countySupport = cursor.getString(9)
            keCounty.chvActivityLevel = cursor.getString(11)
            keCounty.countyPopulation = cursor.getString(12)
            keCounty.noOfVillages = cursor.getLong(13)
            keCounty.mainTownPopulation = cursor.getLong(14)
            keCounty.servicePopulation = cursor.getLong(15)
            keCounty.populationDensity = cursor.getLong(16)
            keCounty.transportCost = cursor.getInt(17)
            keCounty.majorRoads = cursor.getString(18)
            keCounty.healtFacilities = cursor.getString(19)
            keCounty.privateClinicsInTown = cursor.getString(20)
            keCounty.privateClinicsInRadius = cursor.getString(21)
            keCounty.communityUnits = cursor.getString(22)
            keCounty.mainSupermarkets = cursor.getString(23)
            keCounty.mainBanks = cursor.getString(24)
            keCounty.anyMajorBusiness = cursor.getInt(25)
            keCounty.comments = cursor.getString(26)
            keCounty.isRecommended = cursor.getInt(27) == 1
            keCounty.dateAdded = cursor.getLong(28)
            keCounty.addedBy = cursor.getInt(29)
            keCounty.isLgPresent = cursor.getInt(30) == 1
            keCounty.isSynced = cursor.getInt(31) == 1
            db.close()
            return keCounty
        }
    }

    fun getKeCountyByName(countyName: String): KeCounty? {

        val db = readableDatabase
        val whereClause = "$COUNTYNAME = ?"
        val whereArgs = arrayOf(countyName)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val keCounty = KeCounty()

            keCounty.id = cursor.getInt(0)
            keCounty.countyName = cursor.getString(1)
            keCounty.country = cursor.getString(2)
            keCounty.lat = cursor.getDouble(3)
            keCounty.lon = cursor.getDouble(4)
            keCounty.contactPerson = cursor.getString(5)
            keCounty.countyCode = cursor.getString(6)
            keCounty.contactPersonPhone = cursor.getString(7)
            keCounty.mainTown = cursor.getString(8)
            keCounty.countySupport = cursor.getString(9)
            keCounty.chvActivityLevel = cursor.getString(11)
            keCounty.countyPopulation = cursor.getString(12)
            keCounty.noOfVillages = cursor.getLong(13)
            keCounty.mainTownPopulation = cursor.getLong(14)
            keCounty.servicePopulation = cursor.getLong(15)
            keCounty.populationDensity = cursor.getLong(16)
            keCounty.transportCost = cursor.getInt(17)
            keCounty.majorRoads = cursor.getString(18)
            keCounty.healtFacilities = cursor.getString(19)
            keCounty.privateClinicsInTown = cursor.getString(20)
            keCounty.privateClinicsInRadius = cursor.getString(21)
            keCounty.communityUnits = cursor.getString(22)
            keCounty.mainSupermarkets = cursor.getString(23)
            keCounty.mainBanks = cursor.getString(24)
            keCounty.anyMajorBusiness = cursor.getInt(25)
            keCounty.comments = cursor.getString(26)
            keCounty.isRecommended = cursor.getInt(27) == 1
            keCounty.dateAdded = cursor.getLong(28)
            keCounty.addedBy = cursor.getInt(29)
            keCounty.isLgPresent = cursor.getInt(30) == 1
            keCounty.isSynced = cursor.getInt(31) == 1
            db.close()
            return keCounty
        }
    }


    fun createKeCounties(db: SQLiteDatabase) {
        // this should be called once during the installation process.
        var x = 1
        val currentDate = Date().time
        for (county in counties) {
            val countyDetails = KeCounty(x, county, x.toString(), "KE", 0.0, 0.0, "", "", "", "", "", "", 0L, 0L, 0L, 0L, 0, "", "", "", "", "", "", "", 0, "", false, currentDate, 1, true, false)
            val contentValues = ContentValues()
            contentValues.put(ID, x)
            contentValues.put(COUNTYNAME, county)
            contentValues.put(COUNTYCODE, x)
            contentValues.put(DATEADDED, currentDate)
            val id = db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
            x++
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {
        createKeCounties(db)
    }

    companion object {
        val TABLE_NAME = "ke_counties"
        val JSON_ROOT = "ke_counties"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        var varchar_field = " varchar(512) "
        var primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "
        var real_field = " REAL "

        //the fields that I need
        var ID = "id"
        var COUNTYNAME = "county_name"
        var COUNTRY = "country"
        var LAT = "lat"
        var LON = "lon"
        var CONTACTPERSON = "contact_person"
        var COUNTYCODE = "county_code"
        var CONTACTPERSONPHONE = "contact_person_phone"
        var MAINTOWN = "main_town"
        var COUNTYSUPPORT = "county_support"
        var CHVACTIVITY = "chv_activity"
        var CHVACTIVITYLEVEL = "chv_activity_level"
        var COUNTYPOPULATION = "county_population"
        var NOOFVILLAGES = "no_of_villages"
        var MAINTOWNPOPULATION = "main_town_population"
        var SERVICEPOPULATION = "service_population"
        var POPULATIONDENSITY = "population_density"
        var TRANSPORTCOST = "transport_cost"
        var MAJORROADS = "major_roads"
        var HEALTFACILITIES = "health_facilities"
        var PRIVATECLINICSINTOWN = "private_clinics_in_town"
        var PRIVATECLINICSINRADIUS = "private_clinics"
        var COMMUNITYUNITS = "community_units"
        var MAINSUPERMARKETS = "main_supermarkets"
        var MAINBANKS = "main_banks"
        var ANYMAJORBUSINESS = "any_major_business"
        var COMMENTS = "comments"
        var RECOMMENDED = "recommended"
        var DATEADDED = "date_added"
        var ADDEDBY = "added_by"
        var LGPRESENT = "lg_present"
        var SYNCED = "synced"

        var counties = arrayOf("Mombasa", "Kwale", "Kilifi", "Tana River", "Lamu", "Taita-Taveta", "Garissa", "Wajir", "Mandera", "Marsabit", "Isiolo", "Meru", "Tharaka Nithi", "Embu", "Kitui", "Machakos", "Makueni", "Nyandarua", "Nyeri", "Kirinyaga", "Murang'a", "Kiambu", "Turkana", "West Pokot", "Samburu", "Trans Nzoia", "Uasin Gishu", "Elgeyo Marakwet", "Nandi", "Baringo", "Laikipia", "Nakuru", "Narok", "Kajiado", "Kericho", "Bomet", "Kakamega", "Vihiga", "Bungoma", "Busia", "Siaya", "Kisumu", "Homa Bay", "Migori", "Kisii", "Nyamira", "Nairobi City")
        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + primary_field + ", "
                + COUNTYNAME + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + LAT + real_field + ", "
                + LON + real_field + ", "
                + CONTACTPERSON + varchar_field + ", "
                + COUNTYCODE + varchar_field + ", "
                + CONTACTPERSONPHONE + varchar_field + ", "
                + MAINTOWN + varchar_field + ", "
                + COUNTYSUPPORT + varchar_field + ", "
                + CHVACTIVITY + varchar_field + ", "
                + CHVACTIVITYLEVEL + varchar_field + ", "
                + COUNTYPOPULATION + varchar_field + ", "
                + NOOFVILLAGES + real_field + ", "
                + MAINTOWNPOPULATION + real_field + ", "
                + SERVICEPOPULATION + varchar_field + ", "
                + POPULATIONDENSITY + real_field + ", "
                + TRANSPORTCOST + integer_field + ", "
                + MAJORROADS + varchar_field + ", "
                + HEALTFACILITIES + varchar_field + ", "
                + PRIVATECLINICSINTOWN + varchar_field + ", "
                + PRIVATECLINICSINRADIUS + varchar_field + ", "
                + COMMUNITYUNITS + varchar_field + ", "
                + MAINSUPERMARKETS + varchar_field + ", "
                + MAINBANKS + varchar_field + ", "
                + ANYMAJORBUSINESS + integer_field + ", "
                + COMMENTS + text_field + ", "
                + RECOMMENDED + integer_field + ", "
                + DATEADDED + real_field + ", "
                + ADDEDBY + integer_field + ", "
                + LGPRESENT + integer_field + ", "
                + SYNCED + integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}