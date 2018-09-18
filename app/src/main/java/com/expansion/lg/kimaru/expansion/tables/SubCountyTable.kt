package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.Date


/**
 * Created by kimaru on 2/28/17.
 */


class SubCountyTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    internal var columns = arrayOf(ID, SUBCOUNTYNAME, COUNTYID, COUNTRY, MAPPINGID, LAT, LON, CONTACTPERSON, CONTACTPERSONPHONE, MAINTOWN, COUNTYSUPPORT, SUBCOUNTYSUPPORT, CHVACTIVITYLEVEL, COUNTYPOPULATION, SUBCOUNTYPOPULATION, NOOFVILLAGES, MAINTOWNPOPULATION, SERVICEPOPULATION, POPULATIONDENSITY, TRANSPORTCOST, MAJORROADS, HEALTFACILITIES, PRIVATECLINICSINTOWN, PRIVATECLINICSINRADIUS, COMMUNITYUNITS, MAINSUPERMARKETS, MAINBANKS, ANYMAJORBUSINESS, COMMENTS, RECOMMENDATION, DATEADDED, ADDEDBY)


    val subCountyData: List<SubCounty>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val subCounties = ArrayList<SubCounty>()


            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val subCounty = SubCounty()

                subCounty.id = cursor.getString(0)
                subCounty.subCountyName = cursor.getString(1)
                subCounty.countyID = cursor.getString(2)
                subCounty.country = cursor.getString(3)
                subCounty.mappingId = cursor.getString(4)
                subCounty.lat = cursor.getString(5)
                subCounty.lon = cursor.getString(6)
                subCounty.contactPerson = cursor.getString(7)
                subCounty.contactPersonPhone = cursor.getString(8)
                subCounty.mainTown = cursor.getString(9)
                subCounty.countySupport = cursor.getString(10)
                subCounty.subcountySupport = cursor.getString(11)
                subCounty.chvActivityLevel = cursor.getString(12)
                subCounty.countyPopulation = cursor.getString(13)
                subCounty.subCountyPopulation = cursor.getString(14)
                subCounty.noOfVillages = cursor.getString(15)
                subCounty.mainTownPopulation = cursor.getString(16)
                subCounty.servicePopulation = cursor.getString(17)
                subCounty.populationDensity = cursor.getString(18)
                subCounty.transportCost = cursor.getString(19)
                subCounty.majorRoads = cursor.getString(20)
                subCounty.healtFacilities = cursor.getString(21)
                subCounty.privateClinicsInTown = cursor.getString(22)
                subCounty.privateClinicsInRadius = cursor.getString(23)
                subCounty.communityUnits = cursor.getString(24)
                subCounty.mainSupermarkets = cursor.getString(25)
                subCounty.mainBanks = cursor.getString(26)
                subCounty.anyMajorBusiness = cursor.getString(27)
                subCounty.comments = cursor.getString(28)
                subCounty.isRecommended = cursor.getInt(29) == 1
                subCounty.dateAdded = cursor.getLong(30)
                subCounty.addedBy = cursor.getInt(31)

                subCounties.add(subCounty)
                cursor.moveToNext()
            }
            db.close()

            return subCounties
        }

    val subCountyCursor: Cursor
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            db.close()
            return cursor
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
        Log.w("VillageTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addData(subCounty: SubCounty): Long {

        val db = writableDatabase
        val cv = ContentValues()

        cv.put(ID, subCounty.id)
        cv.put(SUBCOUNTYNAME, subCounty.subCountyName)
        cv.put(COUNTYID, subCounty.countyID)
        cv.put(COUNTRY, subCounty.country)
        cv.put(MAPPINGID, subCounty.mappingId)
        cv.put(LAT, subCounty.lat)
        cv.put(LON, subCounty.lon)
        cv.put(CONTACTPERSON, subCounty.contactPerson)
        cv.put(CONTACTPERSONPHONE, subCounty.contactPersonPhone)
        cv.put(MAINTOWN, subCounty.mainTown)
        cv.put(COUNTYSUPPORT, subCounty.countySupport)
        cv.put(SUBCOUNTYSUPPORT, subCounty.subcountySupport)
        cv.put(CHVACTIVITYLEVEL, subCounty.chvActivityLevel)
        cv.put(COUNTYPOPULATION, subCounty.countyPopulation)
        cv.put(SUBCOUNTYPOPULATION, subCounty.subCountyPopulation)
        cv.put(NOOFVILLAGES, subCounty.noOfVillages)
        cv.put(MAINTOWNPOPULATION, subCounty.mainTownPopulation)
        cv.put(SERVICEPOPULATION, subCounty.servicePopulation)
        cv.put(POPULATIONDENSITY, subCounty.populationDensity)
        cv.put(TRANSPORTCOST, subCounty.transportCost)
        cv.put(MAJORROADS, subCounty.majorRoads)
        cv.put(HEALTFACILITIES, subCounty.healtFacilities)
        cv.put(PRIVATECLINICSINTOWN, subCounty.privateClinicsInTown)
        cv.put(PRIVATECLINICSINRADIUS, subCounty.privateClinicsInRadius)
        cv.put(COMMUNITYUNITS, subCounty.communityUnits)
        cv.put(MAINSUPERMARKETS, subCounty.mainSupermarkets)
        cv.put(MAINBANKS, subCounty.mainBanks)
        cv.put(ANYMAJORBUSINESS, subCounty.anyMajorBusiness)
        cv.put(COMMENTS, subCounty.comments)
        cv.put(RECOMMENDATION, if (subCounty.isRecommended) 1 else 0)
        cv.put(DATEADDED, subCounty.dateAdded)
        cv.put(ADDEDBY, subCounty.addedBy)
        Log.d("Tremap ", "Subcounty Try savig or updating")
        val id: Long
        if (isExist(subCounty)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + subCounty.id + "'", null).toLong()
            Log.d("Tremap DB Op", "Sub County updated")
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Tremap DB Op", "SubCounty Created")
        }
        Log.d("Tremap ", "Closing Connection")
        db.close()
        return id

    }

    fun isExist(subCounty: SubCounty): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + subCounty.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    //editData
    fun editData(subCounty: SubCounty): Long {

        val db = writableDatabase
        val cv = ContentValues()

        cv.put(ID, subCounty.id)
        cv.put(SUBCOUNTYNAME, subCounty.subCountyName)
        cv.put(COUNTYID, subCounty.countyID)
        cv.put(COUNTRY, subCounty.country)
        cv.put(MAPPINGID, subCounty.mappingId)
        cv.put(LAT, subCounty.lat)
        cv.put(LON, subCounty.lon)
        cv.put(CONTACTPERSON, subCounty.contactPerson)
        cv.put(CONTACTPERSONPHONE, subCounty.contactPersonPhone)
        cv.put(MAINTOWN, subCounty.mainTown)
        cv.put(COUNTYSUPPORT, subCounty.countySupport)
        cv.put(SUBCOUNTYSUPPORT, subCounty.subcountySupport)
        cv.put(CHVACTIVITYLEVEL, subCounty.chvActivityLevel)
        cv.put(COUNTYPOPULATION, subCounty.countyPopulation)
        cv.put(SUBCOUNTYPOPULATION, subCounty.subCountyPopulation)
        cv.put(NOOFVILLAGES, subCounty.noOfVillages)
        cv.put(MAINTOWNPOPULATION, subCounty.mainTownPopulation)
        cv.put(SERVICEPOPULATION, subCounty.servicePopulation)
        cv.put(POPULATIONDENSITY, subCounty.populationDensity)
        cv.put(TRANSPORTCOST, subCounty.transportCost)
        cv.put(MAJORROADS, subCounty.majorRoads)
        cv.put(HEALTFACILITIES, subCounty.healtFacilities)
        cv.put(PRIVATECLINICSINTOWN, subCounty.privateClinicsInTown)
        cv.put(PRIVATECLINICSINRADIUS, subCounty.privateClinicsInRadius)
        cv.put(COMMUNITYUNITS, subCounty.communityUnits)
        cv.put(MAINSUPERMARKETS, subCounty.mainSupermarkets)
        cv.put(MAINBANKS, subCounty.mainBanks)
        cv.put(ANYMAJORBUSINESS, subCounty.anyMajorBusiness)
        cv.put(COMMENTS, subCounty.comments)
        cv.put(RECOMMENDATION, if (subCounty.isRecommended) 1 else 0)
        cv.put(DATEADDED, subCounty.dateAdded)
        cv.put(ADDEDBY, subCounty.addedBy)

        val id = db.update(TABLE_NAME, cv, ID + "='" + subCounty.id + "'", null).toLong()
        db.close()
        return id
    }

    fun getSubCountyByCountyAndName(countyId: String, country: String, subCountyName: String): SubCounty? {

        val db = readableDatabase

        val whereClause = COUNTYID + " = ? AND " +
                COUNTRY + " = ? AND " +
                SUBCOUNTYNAME + " = ? "

        val whereArgs = arrayOf(countyId, country, subCountyName)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            db.close()
            return null
        } else {
            val subCounty = SubCounty()

            subCounty.id = cursor.getString(0)
            subCounty.subCountyName = cursor.getString(1)
            subCounty.countyID = cursor.getString(2)
            subCounty.country = cursor.getString(3)
            subCounty.mappingId = cursor.getString(4)
            subCounty.lat = cursor.getString(5)
            subCounty.lon = cursor.getString(6)
            subCounty.contactPerson = cursor.getString(7)
            subCounty.contactPersonPhone = cursor.getString(8)
            subCounty.mainTown = cursor.getString(9)
            subCounty.countySupport = cursor.getString(10)
            subCounty.subcountySupport = cursor.getString(11)
            subCounty.chvActivityLevel = cursor.getString(12)
            subCounty.countyPopulation = cursor.getString(13)
            subCounty.subCountyPopulation = cursor.getString(14)
            subCounty.noOfVillages = cursor.getString(15)
            subCounty.mainTownPopulation = cursor.getString(16)
            subCounty.servicePopulation = cursor.getString(17)
            subCounty.populationDensity = cursor.getString(18)
            subCounty.transportCost = cursor.getString(19)
            subCounty.majorRoads = cursor.getString(20)
            subCounty.healtFacilities = cursor.getString(21)
            subCounty.privateClinicsInTown = cursor.getString(22)
            subCounty.privateClinicsInRadius = cursor.getString(23)
            subCounty.communityUnits = cursor.getString(24)
            subCounty.mainSupermarkets = cursor.getString(25)
            subCounty.mainBanks = cursor.getString(26)
            subCounty.anyMajorBusiness = cursor.getString(27)
            subCounty.comments = cursor.getString(28)
            subCounty.isRecommended = cursor.getInt(29) == 1
            subCounty.dateAdded = cursor.getLong(30)
            subCounty.addedBy = cursor.getInt(31)
            db.close()
            return subCounty
        }
    }

    fun getSubCountiesByCounty(countyId: Int?): List<SubCounty> {
        val db = readableDatabase
        val whereClause = "$COUNTYID = ? "
        val whereArgs = arrayOf(countyId.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val subCounties = ArrayList<SubCounty>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val subCounty = SubCounty()

            subCounty.id = cursor.getString(0)
            subCounty.subCountyName = cursor.getString(1)
            subCounty.countyID = cursor.getString(2)
            subCounty.country = cursor.getString(3)
            subCounty.mappingId = cursor.getString(4)
            subCounty.lat = cursor.getString(5)
            subCounty.lon = cursor.getString(6)
            subCounty.contactPerson = cursor.getString(7)
            subCounty.contactPersonPhone = cursor.getString(8)
            subCounty.mainTown = cursor.getString(9)
            subCounty.countySupport = cursor.getString(10)
            subCounty.subcountySupport = cursor.getString(11)
            subCounty.chvActivityLevel = cursor.getString(12)
            subCounty.countyPopulation = cursor.getString(13)
            subCounty.subCountyPopulation = cursor.getString(14)
            subCounty.noOfVillages = cursor.getString(15)
            subCounty.mainTownPopulation = cursor.getString(16)
            subCounty.servicePopulation = cursor.getString(17)
            subCounty.populationDensity = cursor.getString(18)
            subCounty.transportCost = cursor.getString(19)
            subCounty.majorRoads = cursor.getString(20)
            subCounty.healtFacilities = cursor.getString(21)
            subCounty.privateClinicsInTown = cursor.getString(22)
            subCounty.privateClinicsInRadius = cursor.getString(23)
            subCounty.communityUnits = cursor.getString(24)
            subCounty.mainSupermarkets = cursor.getString(25)
            subCounty.mainBanks = cursor.getString(26)
            subCounty.anyMajorBusiness = cursor.getString(27)
            subCounty.comments = cursor.getString(28)
            subCounty.isRecommended = cursor.getInt(29) == 1
            subCounty.dateAdded = cursor.getLong(30)
            subCounty.addedBy = cursor.getInt(31)

            subCounties.add(subCounty)
            cursor.moveToNext()
        }
        db.close()

        return subCounties
    }

    fun getSubCountyById(subCountyId: String): SubCounty? {

        val db = readableDatabase

        val whereClause = "$ID = ? "

        val whereArgs = arrayOf(subCountyId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            db.close()
            return null
        } else {
            val subCounty = SubCounty()
            subCounty.id = cursor.getString(0)
            subCounty.subCountyName = cursor.getString(1)
            subCounty.countyID = cursor.getString(2)
            subCounty.country = cursor.getString(3)
            subCounty.mappingId = cursor.getString(4)
            subCounty.lat = cursor.getString(5)
            subCounty.lon = cursor.getString(6)
            subCounty.contactPerson = cursor.getString(7)
            subCounty.contactPersonPhone = cursor.getString(8)
            subCounty.mainTown = cursor.getString(9)
            subCounty.countySupport = cursor.getString(10)
            subCounty.subcountySupport = cursor.getString(11)
            subCounty.chvActivityLevel = cursor.getString(12)
            subCounty.countyPopulation = cursor.getString(13)
            subCounty.subCountyPopulation = cursor.getString(14)
            subCounty.noOfVillages = cursor.getString(15)
            subCounty.mainTownPopulation = cursor.getString(16)
            subCounty.servicePopulation = cursor.getString(17)
            subCounty.populationDensity = cursor.getString(18)
            subCounty.transportCost = cursor.getString(19)
            subCounty.majorRoads = cursor.getString(20)
            subCounty.healtFacilities = cursor.getString(21)
            subCounty.privateClinicsInTown = cursor.getString(22)
            subCounty.privateClinicsInRadius = cursor.getString(23)
            subCounty.communityUnits = cursor.getString(24)
            subCounty.mainSupermarkets = cursor.getString(25)
            subCounty.mainBanks = cursor.getString(26)
            subCounty.anyMajorBusiness = cursor.getString(27)
            subCounty.comments = cursor.getString(28)
            subCounty.isRecommended = cursor.getInt(29) == 1
            subCounty.dateAdded = cursor.getLong(30)
            subCounty.addedBy = cursor.getInt(31)
            db.close()
            return subCounty
        }
    }

    fun fromJson(jsonObject: JSONObject) {
        Log.d("Tremap", "Subcounty Creating from JSON")
        val subCounty = SubCounty()
        try {
            subCounty.id = jsonObject.getString(ID)
            var name = jsonObject.getString(SUBCOUNTYNAME)
            if (!jsonObject.getString(SUBCOUNTYNAME).equals("", ignoreCase = true)) {
                name = name.substring(0, 1).toUpperCase() + name.substring(1)
            }
            subCounty.subCountyName = name
            subCounty.countyID = jsonObject.getString(COUNTYID)
            var country = jsonObject.getString(COUNTRY)
            if (!jsonObject.getString(COUNTRY).equals("", ignoreCase = true)) {
                country = country.substring(0, 1).toUpperCase() + country.substring(1)
            }
            subCounty.country = country

            subCounty.mappingId = jsonObject.getString(MAPPINGID)
            subCounty.lat = jsonObject.getString(LAT)
            subCounty.lon = jsonObject.getString(LON)

            var contactPerson = jsonObject.getString(CONTACTPERSON)
            if (!jsonObject.getString(CONTACTPERSON).equals("", ignoreCase = true)) {
                contactPerson = contactPerson.substring(0, 1).toUpperCase() + contactPerson.substring(1)
            }
            subCounty.contactPerson = contactPerson

            var mainTown = jsonObject.getString(MAINTOWN)
            if (!jsonObject.getString(MAINTOWN).equals("", ignoreCase = true)) {
                mainTown = mainTown.substring(0, 1).toUpperCase() + mainTown.substring(1)
            }
            subCounty.mainTown = mainTown
            subCounty.countySupport = jsonObject.getString(COUNTYSUPPORT)
            subCounty.subcountySupport = jsonObject.getString(SUBCOUNTYSUPPORT)
            subCounty.chvActivityLevel = jsonObject.getString(CHVACTIVITYLEVEL)
            subCounty.countyPopulation = jsonObject.getString(COUNTYPOPULATION)
            subCounty.subCountyPopulation = jsonObject.getString(SUBCOUNTYPOPULATION)
            subCounty.noOfVillages = jsonObject.getString(NOOFVILLAGES)
            subCounty.mainTownPopulation = jsonObject.getString(MAINTOWNPOPULATION)
            subCounty.servicePopulation = jsonObject.getString(SERVICEPOPULATION)
            subCounty.populationDensity = jsonObject.getString(POPULATIONDENSITY)
            subCounty.transportCost = jsonObject.getString(TRANSPORTCOST)
            subCounty.majorRoads = jsonObject.getString(MAJORROADS)
            subCounty.healtFacilities = jsonObject.getString(HEALTFACILITIES)
            subCounty.privateClinicsInTown = jsonObject.getString(PRIVATECLINICSINTOWN)
            subCounty.privateClinicsInRadius = jsonObject.getString(PRIVATECLINICSINRADIUS)
            subCounty.communityUnits = jsonObject.getString(COMMUNITYUNITS)
            subCounty.mainSupermarkets = jsonObject.getString(MAINSUPERMARKETS)
            subCounty.mainBanks = jsonObject.getString(MAINBANKS)
            subCounty.anyMajorBusiness = jsonObject.getString(ANYMAJORBUSINESS)
            subCounty.comments = jsonObject.getString(COMMENTS)
            subCounty.isRecommended = jsonObject.getInt(RECOMMENDATION) == 1
            try {
                val added = jsonObject.getString(DATEADDED)
                if (added.equals("", ignoreCase = true) || added.isEmpty()) {
                    subCounty.dateAdded = Date().time
                } else {
                    subCounty.dateAdded = jsonObject.getLong(DATEADDED)
                }
            } catch (e: Exception) {
                subCounty.dateAdded = Date().time
                Log.e("Tremap Error", "=======================================")
                Log.e("Tremap Error", "SUBCOUNTY E " + e.message)
                Log.e("Tremap Error", "Using the current time instead")
            }

            try {
                val by = jsonObject.getString(ADDEDBY)
                if (by.equals("", ignoreCase = true)) {
                    subCounty.addedBy = 1
                } else {
                    subCounty.addedBy = jsonObject.getInt(ADDEDBY)
                }
            } catch (e: Exception) {
                subCounty.addedBy = 1
                Log.e("Tremap Error", "=======================================")
                Log.e("Tremap Error", "SUBCOUNTY E " + e.message)
                Log.e("Tremap Error", "Using 1 instead")
            }

            addData(subCounty)
            Log.d("Tremap", "Subcounty Object from JSON")
        } catch (e: Exception) {
            Log.e("Tremap Error", "=======================================")
            Log.e("Tremap Error", "SUBCOUNTY E " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "sub_county"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val JSON_ROOT = "subcounties"


        val ID = "id"
        val SUBCOUNTYNAME = "name"
        val COUNTYID = "countyID"
        val COUNTRY = "country"
        val MAPPINGID = "mappingId"
        val LAT = "lat"
        val LON = "lon"
        val CONTACTPERSON = "contactPerson"
        val CONTACTPERSONPHONE = "contactPersonPhone"
        val MAINTOWN = "mainTown"
        val COUNTYSUPPORT = "countySupport"
        val SUBCOUNTYSUPPORT = "subcountySupport"
        val CHVACTIVITYLEVEL = "chv_activity_level"
        val COUNTYPOPULATION = "countyPopulation"
        val SUBCOUNTYPOPULATION = "subCountyPopulation"
        val NOOFVILLAGES = "noOfVillages"
        val MAINTOWNPOPULATION = "mainTownPopulation"
        val SERVICEPOPULATION = "servicePopulation"
        val POPULATIONDENSITY = "populationDensity"
        val TRANSPORTCOST = "transportCost"
        val MAJORROADS = "majorRoads"
        val HEALTFACILITIES = "healtFacilities"
        val PRIVATECLINICSINTOWN = "privateClinicsInTown"
        val PRIVATECLINICSINRADIUS = "privateClinicsInRadius"
        val COMMUNITYUNITS = "communityUnits"
        val MAINSUPERMARKETS = "mainSupermarkets"
        val MAINBANKS = "mainBanks"
        val ANYMAJORBUSINESS = "anyMajorBusiness"
        val COMMENTS = "comments"
        val RECOMMENDATION = "recommendation"
        val DATEADDED = "dateAdded"
        val ADDEDBY = "addedBy"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ","
                + SUBCOUNTYNAME + varchar_field + ","
                + COUNTYID + varchar_field + ","
                + COUNTRY + varchar_field + ","
                + MAPPINGID + varchar_field + ","
                + LAT + varchar_field + ","
                + LON + varchar_field + ","
                + CONTACTPERSON + varchar_field + ","
                + CONTACTPERSONPHONE + varchar_field + ","
                + MAINTOWN + varchar_field + ","
                + COUNTYSUPPORT + varchar_field + ","
                + SUBCOUNTYSUPPORT + varchar_field + ","
                + CHVACTIVITYLEVEL + varchar_field + ","
                + COUNTYPOPULATION + varchar_field + ","
                + SUBCOUNTYPOPULATION + varchar_field + ","
                + NOOFVILLAGES + varchar_field + ","
                + MAINTOWNPOPULATION + varchar_field + ","
                + SERVICEPOPULATION + varchar_field + ","
                + POPULATIONDENSITY + varchar_field + ","
                + TRANSPORTCOST + varchar_field + ","
                + MAJORROADS + varchar_field + ","
                + HEALTFACILITIES + varchar_field + ","
                + PRIVATECLINICSINTOWN + varchar_field + ","
                + PRIVATECLINICSINRADIUS + varchar_field + ","
                + COMMUNITYUNITS + varchar_field + ","
                + MAINSUPERMARKETS + varchar_field + ","
                + MAINBANKS + varchar_field + ","
                + ANYMAJORBUSINESS + varchar_field + ","
                + COMMENTS + varchar_field + ","
                + RECOMMENDATION + integer_field + ","
                + DATEADDED + integer_field + ","
                + ADDEDBY + varchar_field + ")")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

