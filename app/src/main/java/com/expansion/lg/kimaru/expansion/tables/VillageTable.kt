package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.Village
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.VillageTable.Companion.TABLE_NAME
import com.expansion.lg.kimaru.expansion.tables.VillageTable.Companion.columns

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 2/28/17.
 */

class VillageTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    val villageData: List<Village>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val villages = ArrayList<Village>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                villages.add(cursorToVillage(cursor))
                cursor.moveToNext()
            }
            db.close()
            return villages
        }

    val villageDataCursor: Cursor
        get() {
            val db = readableDatabase
            return db.query(TABLE_NAME, columns, null, null, null, null, null, null)
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
        Log.w("VillageTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)

        }
    }

    fun addData(village: Village): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, village.id)
        cv.put(VILLAGENAME, village.villageName)
        cv.put(MAPPINGID, village.mappingId)
        cv.put(LAT, village.lat)
        cv.put(LON, village.lon)
        cv.put(COUNTRY, village.country)
        cv.put(DISTRICT, village.district)
        cv.put(COUNTY, village.county)
        cv.put(SUBCOUNTYID, village.subCountyId)
        cv.put(PARISH, village.parish)
        cv.put(COMMUNITY_UNIT, village.communityUnit)
        cv.put(WARD, village.ward)
        cv.put(LINKFACILITYID, village.linkFacilityId)
        cv.put(AREACHIEFNAME, village.areaChiefName)
        cv.put(AREACHIEFPHONE, village.areaChiefPhone)
        cv.put(DISTANCETOBRANCH, village.distanceToBranch)
        cv.put(TRANSPORTCOST, village.transportCost)
        cv.put(DISTANCETOMAINROAD, village.distanceToMainRoad)
        cv.put(NOOFHOUSEHOLDS, village.noOfHouseholds)
        cv.put(MOHPOPLATIONDENSITY, village.mohPoplationDensity)
        cv.put(ESTIMATEDPOPULATIONDENSITY, village.estimatedPopulationDensity)
        cv.put(ECONOMICSTATUS, village.economicStatus)
        cv.put(DISTANCETONEARESTHEALTHFACILITY, village.distanceToNearestHealthFacility)
        cv.put(ACTLEVELS, village.actLevels)
        cv.put(ACTPRICE, village.actPrice)
        cv.put(MRDTLEVELS, village.mrdtLevels)
        cv.put(MRDTPRICE, village.mrdtPrice)
        cv.put(PRESENCEOFHOSTELS, village.presenceOfHostels)
        cv.put(PRESENCEOFESTATES, village.presenceOfEstates)
        cv.put(NUMBEROFFACTORIES, village.numberOfFactories)
        cv.put(PRESENCEOFDISTRIBUTORS, village.presenceOfDistributors)
        cv.put(DISTRIBUTORSINTHEAREA, village.distributorsInTheArea)
        cv.put(TRADERMARKET, village.traderMarket)
        cv.put(LARGESUPERMARKET, village.largeSupermarket)
        cv.put(NGOSGIVINGFREEDRUGS, village.ngosGivingFreeDrugs)
        cv.put(NGODOINGICCM, village.ngoDoingIccm)
        cv.put(NGODOINGMHEALTH, village.ngoDoingMhealth)
        cv.put(NAMEOFNGODOINGICCM, village.nameOfNgoDoingIccm)
        cv.put(NAMEOFNGODOINGMHEALTH, village.nameOfNgoDoingMhealth)
        cv.put(PRIVATEFACILITYFORACT, village.privateFacilityForAct)
        cv.put(PRIVATEFACILITYFORMRDT, village.privateFacilityForMrdt)
        cv.put(DATEADDED, village.dateAdded)
        cv.put(ADDEDBY, village.addedBy)
        cv.put(COMMENT, village.comment)
        cv.put(SYNCED, village.isSynced)
        cv.put(CHVS_TRAINED, village.chvsTrained)
        cv.put(BRAC_OPERATING, if (village.isBracOperating) 1 else 0)
        cv.put(SAFARICOM, village.safaricomSignalStrength)
        cv.put(MTN, village.mtnSignalStrength)
        cv.put(AIRTEL, village.airtelSignalStrength)
        cv.put(ORANGE, village.orangeSignalStrength)
        cv.put(ACTSTOCK, if (village.actStock!!) 1 else 0)

        val id: Long
        if (isExist(village)) {
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + village.id + "'", null).toLong()
            Log.d("Tremap DB Op", "Village updated")
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
            Log.d("Tremap DB Op", "Village created with id " + village.id)
        }
        db.close()
        return id

    }

    fun isExist(village: Village): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + village.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }


    fun getVillagesByLinkFacility(linkFacilityId: String): List<Village> {
        val db = readableDatabase
        val orderBy = "$DATEADDED desc"
        val whereClause = "$LINKFACILITYID = ?"
        val whereArgs = arrayOf(linkFacilityId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val villages = ArrayList<Village>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            villages.add(cursorToVillage(cursor))
            cursor.moveToNext()
        }
        db.close()
        return villages
    }


    fun getVillageDataByParishId(parishUUID: String): List<Village> {
        val db = readableDatabase
        val orderBy = "$DATEADDED desc"
        val whereClause = "$PARISH = ?"
        val whereArgs = arrayOf(parishUUID)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

        val villages = ArrayList<Village>()


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            villages.add(cursorToVillage(cursor))
            cursor.moveToNext()
        }
        db.close()

        return villages
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {
        if (!isFieldExist(db, ACTSTOCK)) {
            db.execSQL(DB_UPDATE_V2)
        }
    }

    fun isFieldExist(db: SQLiteDatabase, fieldName: String): Boolean {
        var isExist = false
        var res: Cursor? = null
        try {
            res = db.rawQuery("Select * from $TABLE_NAME limit 1", null)
            val colIndex = res!!.getColumnIndex(fieldName)
            if (colIndex != -1) {
                isExist = true
            }

        } catch (e: Exception) {
        } finally {
            try {
                res?.close()
            } catch (e1: Exception) {
            }

        }
        return isExist
    }

    fun fromJson(jsonObject: JSONObject) {
        val village = Village()
        try {
            village.id = jsonObject.getString(ID)
            if (!jsonObject.isNull(VILLAGENAME)) {
                village.villageName = jsonObject.getString(VILLAGENAME)
            }
            if (!jsonObject.isNull(MAPPINGID)) {
                village.mappingId = jsonObject.getString(MAPPINGID)
            }
            if (!jsonObject.isNull(LAT)) {
                village.lat = jsonObject.getDouble(LAT)
            }
            if (!jsonObject.isNull(LON)) {
                village.lon = jsonObject.getDouble(LON)
            }
            if (!jsonObject.isNull(COUNTRY)) {
                village.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(DISTRICT)) {
                village.district = jsonObject.getString(DISTRICT)
            }
            if (!jsonObject.isNull(COUNTY)) {
                village.county = jsonObject.getString(COUNTY)
            }
            if (!jsonObject.isNull(SUBCOUNTYID)) {
                village.subCountyId = jsonObject.getString(SUBCOUNTYID)
            }
            if (!jsonObject.isNull(PARISH)) {
                village.parish = jsonObject.getString(PARISH)
            }
            if (!jsonObject.isNull(COMMUNITY_UNIT)) {
                village.communityUnit = jsonObject.getString(COMMUNITY_UNIT)
            }
            if (!jsonObject.isNull(WARD)) {
                village.ward = jsonObject.getString(WARD)
            }
            if (!jsonObject.isNull(LINKFACILITYID)) {
                village.linkFacilityId = jsonObject.getString(LINKFACILITYID)
            }
            if (!jsonObject.isNull(AREACHIEFNAME)) {
                village.areaChiefName = jsonObject.getString(AREACHIEFNAME)
            }
            if (!jsonObject.isNull(AREACHIEFPHONE)) {
                village.areaChiefPhone = jsonObject.getString(AREACHIEFPHONE)
            }
            if (!jsonObject.isNull(DISTANCETOBRANCH)) {
                village.distanceToBranch = jsonObject.getLong(DISTANCETOBRANCH)
            }
            if (!jsonObject.isNull(TRANSPORTCOST)) {
                village.transportCost = jsonObject.getLong(TRANSPORTCOST)
            }
            if (!jsonObject.isNull(DISTANCETOMAINROAD)) {
                village.distanceToMainRoad = jsonObject.getLong(DISTANCETOMAINROAD)
            }
            if (!jsonObject.isNull(NOOFHOUSEHOLDS)) {
                village.noOfHouseholds = jsonObject.getLong(NOOFHOUSEHOLDS)
            }
            if (!jsonObject.isNull(MOHPOPLATIONDENSITY)) {
                village.mohPoplationDensity = jsonObject.getLong(MOHPOPLATIONDENSITY)
            }
            if (!jsonObject.isNull(ESTIMATEDPOPULATIONDENSITY)) {
                village.estimatedPopulationDensity = jsonObject.getLong(ESTIMATEDPOPULATIONDENSITY)
            }
            if (!jsonObject.isNull(ECONOMICSTATUS)) {
                village.economicStatus = jsonObject.getString(ECONOMICSTATUS)
            }
            if (!jsonObject.isNull(DISTANCETONEARESTHEALTHFACILITY)) {
                village.distanceToNearestHealthFacility = jsonObject.getLong(DISTANCETONEARESTHEALTHFACILITY)
            }
            if (!jsonObject.isNull(ACTLEVELS)) {
                village.actLevels = jsonObject.getLong(ACTLEVELS)
            }
            if (!jsonObject.isNull(ACTPRICE)) {
                village.actPrice = jsonObject.getLong(ACTPRICE)
            }
            if (!jsonObject.isNull(MRDTLEVELS)) {
                village.mrdtLevels = jsonObject.getLong(MRDTLEVELS)
            }
            if (!jsonObject.isNull(MRDTPRICE)) {
                village.mrdtPrice = jsonObject.getLong(MRDTPRICE)
            }
            if (!jsonObject.isNull(PRESENCEOFHOSTELS)) {
                village.presenceOfHostels = jsonObject.getInt(PRESENCEOFHOSTELS) == 1
            }
            if (!jsonObject.isNull(PRESENCEOFESTATES)) {
                village.presenceOfEstates = jsonObject.getInt(PRESENCEOFESTATES) == 1
            }
            if (!jsonObject.isNull(NUMBEROFFACTORIES)) {
                village.numberOfFactories = jsonObject.getInt(NUMBEROFFACTORIES)
            }
            if (!jsonObject.isNull(PRESENCEOFDISTRIBUTORS)) {
                village.presenceOfDistributors = jsonObject.getInt(PRESENCEOFDISTRIBUTORS) == 1
            }
            if (!jsonObject.isNull(DISTRIBUTORSINTHEAREA)) {
                village.distributorsInTheArea = jsonObject.getString(DISTRIBUTORSINTHEAREA)
            }
            if (!jsonObject.isNull(TRADERMARKET)) {
                village.traderMarket = jsonObject.getInt(TRADERMARKET) == 1
            }
            if (!jsonObject.isNull(LARGESUPERMARKET)) {
                village.largeSupermarket = jsonObject.getInt(LARGESUPERMARKET) == 1
            }
            if (!jsonObject.isNull(NGOSGIVINGFREEDRUGS)) {
                village.ngosGivingFreeDrugs = jsonObject.getInt(NGOSGIVINGFREEDRUGS) == 1
            }
            if (!jsonObject.isNull(NGODOINGICCM)) {
                village.ngoDoingIccm = jsonObject.getInt(NGODOINGICCM) == 1
            }
            if (!jsonObject.isNull(NGODOINGMHEALTH)) {
                village.ngoDoingMhealth = jsonObject.getInt(NGODOINGMHEALTH) == 1
            }
            if (!jsonObject.isNull(NAMEOFNGODOINGICCM)) {
                village.nameOfNgoDoingIccm = jsonObject.getString(NAMEOFNGODOINGICCM)
            }
            if (!jsonObject.isNull(NAMEOFNGODOINGMHEALTH)) {
                village.nameOfNgoDoingMhealth = jsonObject.getString(NAMEOFNGODOINGMHEALTH)
            }
            if (!jsonObject.isNull(PRIVATEFACILITYFORACT)) {
                village.privateFacilityForAct = jsonObject.getString(PRIVATEFACILITYFORACT)
            }
            if (!jsonObject.isNull(PRIVATEFACILITYFORMRDT)) {
                village.privateFacilityForMrdt = jsonObject.getString(PRIVATEFACILITYFORMRDT)
            }
            if (!jsonObject.isNull(DATEADDED)) {
                village.dateAdded = jsonObject.getLong(DATEADDED)
            }
            if (!jsonObject.isNull(ADDEDBY)) {
                village.addedBy = jsonObject.getInt(ADDEDBY)
            }
            if (!jsonObject.isNull(COMMENT)) {
                village.comment = jsonObject.getString(COMMENT)
            }
            if (!jsonObject.isNull(SYNCED)) {
                village.isSynced = jsonObject.getInt(SYNCED) == 1
            }
            if (!jsonObject.isNull(CHVS_TRAINED)) {
                village.chvsTrained = jsonObject.getInt(CHVS_TRAINED) == 1
            }
            if (!jsonObject.isNull(BRAC_OPERATING)) {
                village.isBracOperating = jsonObject.getInt(BRAC_OPERATING) == 1
            }
            if (!jsonObject.isNull(SAFARICOM)) {
                village.safaricomSignalStrength = jsonObject.getInt(SAFARICOM)
            }
            if (!jsonObject.isNull(MTN)) {
                village.mtnSignalStrength = jsonObject.getInt(MTN)
            }
            if (!jsonObject.isNull(AIRTEL)) {
                village.airtelSignalStrength = jsonObject.getInt(AIRTEL)
            }
            if (!jsonObject.isNull(ORANGE)) {
                village.orangeSignalStrength = jsonObject.getInt(ORANGE)
            }
            if (!jsonObject.isNull(ACTSTOCK)) {
                village.actStock = jsonObject.getInt(ACTSTOCK) == 1
            }
            this.addData(village)
        } catch (e: Exception) {
            Log.d("Tremap", e.message)
        }

    }

    private fun cursorToVillage(cursor: Cursor): Village {
        val village = Village()
        village.id = cursor.getString(cursor.getColumnIndex(ID))
        village.villageName = cursor.getString(cursor.getColumnIndex(VILLAGENAME))
        village.mappingId = cursor.getString(cursor.getColumnIndex(MAPPINGID))
        village.lat = cursor.getDouble(cursor.getColumnIndex(LAT))
        village.lon = cursor.getDouble(cursor.getColumnIndex(LON))
        village.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
        village.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
        village.county = cursor.getString(cursor.getColumnIndex(COUNTY))
        village.subCountyId = cursor.getString(cursor.getColumnIndex(SUBCOUNTYID))
        village.parish = cursor.getString(cursor.getColumnIndex(PARISH))
        village.communityUnit = cursor.getString(cursor.getColumnIndex(COMMUNITY_UNIT))
        village.ward = cursor.getString(cursor.getColumnIndex(WARD))
        village.linkFacilityId = cursor.getString(cursor.getColumnIndex(LINKFACILITYID))
        village.areaChiefName = cursor.getString(cursor.getColumnIndex(AREACHIEFNAME))
        village.areaChiefPhone = cursor.getString(cursor.getColumnIndex(AREACHIEFPHONE))
        village.distanceToBranch = cursor.getLong(cursor.getColumnIndex(DISTANCETOBRANCH))
        village.transportCost = cursor.getLong(cursor.getColumnIndex(TRANSPORTCOST))
        village.distanceToMainRoad = cursor.getLong(cursor.getColumnIndex(DISTANCETOMAINROAD))
        village.noOfHouseholds = cursor.getLong(cursor.getColumnIndex(NOOFHOUSEHOLDS))
        village.mohPoplationDensity = cursor.getLong(cursor.getColumnIndex(MOHPOPLATIONDENSITY))
        village.estimatedPopulationDensity = cursor.getLong(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY))
        village.economicStatus = cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS))
        village.distanceToNearestHealthFacility = cursor.getLong(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY))
        village.actLevels = cursor.getLong(cursor.getColumnIndex(ACTLEVELS))
        village.actPrice = cursor.getLong(cursor.getColumnIndex(ACTPRICE))
        village.mrdtLevels = cursor.getLong(cursor.getColumnIndex(MRDTLEVELS))
        village.mrdtPrice = cursor.getLong(cursor.getColumnIndex(MRDTPRICE))
        village.presenceOfHostels = cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS)) == 1
        village.presenceOfEstates = cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES)) == 1
        village.numberOfFactories = cursor.getInt(cursor.getColumnIndex(NUMBEROFFACTORIES))
        village.presenceOfDistributors = cursor.getInt(cursor.getColumnIndex(PRESENCEOFDISTRIBUTORS)) == 1
        village.distributorsInTheArea = cursor.getString(cursor.getColumnIndex(DISTRIBUTORSINTHEAREA))
        village.traderMarket = cursor.getInt(cursor.getColumnIndex(TRADERMARKET)) == 1
        village.largeSupermarket = cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET)) == 1
        village.ngosGivingFreeDrugs = cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS)) == 1
        village.ngoDoingIccm = cursor.getInt(cursor.getColumnIndex(NGODOINGICCM)) == 1
        village.ngoDoingMhealth = cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH)) == 1
        village.nameOfNgoDoingIccm = cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM))
        village.nameOfNgoDoingMhealth = cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH))
        village.privateFacilityForAct = cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT))
        village.privateFacilityForMrdt = cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT))
        village.dateAdded = cursor.getLong(cursor.getColumnIndex(DATEADDED))
        village.addedBy = cursor.getInt(cursor.getColumnIndex(ADDEDBY))
        village.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
        village.isSynced = cursor.getInt(cursor.getColumnIndex(SYNCED)) == 1
        village.chvsTrained = cursor.getInt(cursor.getColumnIndex(CHVS_TRAINED)) == 1
        village.isBracOperating = cursor.getInt(cursor.getColumnIndex(BRAC_OPERATING)) == 1
        village.safaricomSignalStrength = cursor.getInt(cursor.getColumnIndex(SAFARICOM))
        village.mtnSignalStrength = cursor.getInt(cursor.getColumnIndex(MTN))
        village.airtelSignalStrength = cursor.getInt(cursor.getColumnIndex(AIRTEL))
        village.orangeSignalStrength = cursor.getInt(cursor.getColumnIndex(ORANGE))
        village.actStock = cursor.getInt(cursor.getColumnIndex(ACTSTOCK)) == 1
        return village
    }

    companion object {
        val TABLE_NAME = "village"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        var varchar_field = " varchar(512) "
        var real_field = " REAL "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "id"
        val VILLAGENAME = "village_name"
        val MAPPINGID = "mapping_id"
        val LAT = "lat"
        val LON = "lon"
        val COUNTRY = "country"
        val DISTRICT = "district"
        val COUNTY = "county"
        val SUBCOUNTYID = "sub_county_id"
        val PARISH = "parish"
        val COMMUNITY_UNIT = "community_unit"
        val WARD = "ward"
        val LINKFACILITYID = "link_facility_id"
        val AREACHIEFNAME = "area_chief_name"
        val AREACHIEFPHONE = "area_chief_phone"
        val DISTANCETOBRANCH = "distancetobranch"
        val TRANSPORTCOST = "transportcost"
        val DISTANCETOMAINROAD = "distancetomainroad"
        val NOOFHOUSEHOLDS = "noofhouseholds"
        val MOHPOPLATIONDENSITY = "mohpoplationdensity"
        val ESTIMATEDPOPULATIONDENSITY = "estimatedpopulationdensity"
        val ECONOMICSTATUS = "economic_status"
        val DISTANCETONEARESTHEALTHFACILITY = "distancetonearesthealthfacility"
        val ACTLEVELS = "actlevels"
        val ACTPRICE = "actprice"
        val MRDTLEVELS = "mrdtlevels"
        val MRDTPRICE = "mrdtprice"
        val PRESENCEOFHOSTELS = "presenceofhostels"
        val PRESENCEOFESTATES = "presenceofestates"
        val NUMBEROFFACTORIES = "number_of_factories"
        val PRESENCEOFDISTRIBUTORS = "presenceofdistibutors"
        val DISTRIBUTORSINTHEAREA = "name_of_distibutors"
        val TRADERMARKET = "tradermarket"
        val LARGESUPERMARKET = "largesupermarket"
        val NGOSGIVINGFREEDRUGS = "ngosgivingfreedrugs"
        val NGODOINGICCM = "ngodoingiccm"
        val NGODOINGMHEALTH = "ngodoingmhealth"
        val NAMEOFNGODOINGICCM = "nameofngodoingiccm"
        val NAMEOFNGODOINGMHEALTH = "nameofngodoingmhealth"
        val PRIVATEFACILITYFORACT = "privatefacilityforact"
        val PRIVATEFACILITYFORMRDT = "privatefacilityformrdt"
        val SYNCED = "synced"
        val CHVS_TRAINED = "chvs_trained"
        val DATEADDED = "dateadded"
        val ADDEDBY = "addedby"
        val COMMENT = "comment"
        val BRAC_OPERATING = "brac_operating"
        val MTN = "mtn_signal"
        val SAFARICOM = "safaricom_signal"
        val AIRTEL = "airtel_signal"
        val ORANGE = "orange_signal"
        val ACTSTOCK = "act_stock"
        val JSON_ROOT = "villages"


        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ","
                + VILLAGENAME + varchar_field + ","
                + MAPPINGID + varchar_field + ","
                + LAT + real_field + ","
                + LON + real_field + ","
                + COUNTRY + varchar_field + ","
                + DISTRICT + varchar_field + ","
                + COUNTY + varchar_field + ","
                + SUBCOUNTYID + varchar_field + ","
                + PARISH + varchar_field + ","
                + COMMUNITY_UNIT + varchar_field + ","
                + WARD + varchar_field + ","
                + LINKFACILITYID + varchar_field + ","
                + AREACHIEFNAME + varchar_field + ","
                + AREACHIEFPHONE + varchar_field + ","
                + DISTANCETOBRANCH + varchar_field + ","
                + TRANSPORTCOST + varchar_field + ","
                + DISTANCETOMAINROAD + varchar_field + ","
                + NOOFHOUSEHOLDS + text_field + ","
                + MOHPOPLATIONDENSITY + text_field + ","
                + ESTIMATEDPOPULATIONDENSITY + integer_field + ","
                + ECONOMICSTATUS + varchar_field + ","
                + DISTANCETONEARESTHEALTHFACILITY + integer_field + ","
                + ACTLEVELS + integer_field + ","
                + ACTPRICE + integer_field + ","
                + MRDTLEVELS + integer_field + ","
                + MRDTPRICE + integer_field + ","
                + PRESENCEOFHOSTELS + integer_field + ","
                + PRESENCEOFESTATES + integer_field + ","
                + NUMBEROFFACTORIES + integer_field + ","
                + PRESENCEOFDISTRIBUTORS + integer_field + ","
                + DISTRIBUTORSINTHEAREA + text_field + ","
                + TRADERMARKET + integer_field + ","
                + LARGESUPERMARKET + integer_field + ","
                + NGOSGIVINGFREEDRUGS + integer_field + ","
                + NGODOINGICCM + integer_field + ","
                + NGODOINGMHEALTH + integer_field + ","
                + NAMEOFNGODOINGICCM + integer_field + ","
                + NAMEOFNGODOINGMHEALTH + integer_field + ","
                + PRIVATEFACILITYFORACT + integer_field + ","
                + PRIVATEFACILITYFORMRDT + integer_field + ","
                + DATEADDED + integer_field + ","
                + ADDEDBY + integer_field + ","
                + SYNCED + integer_field + ","
                + CHVS_TRAINED + integer_field + ","
                + BRAC_OPERATING + integer_field + ","
                + MTN + integer_field + ","
                + SAFARICOM + integer_field + ","
                + ORANGE + integer_field + ","
                + AIRTEL + integer_field + ","
                + ACTSTOCK + integer_field + ","
                + COMMENT + text_field + ");")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
        val columns = arrayOf(ID, VILLAGENAME, MAPPINGID, LAT, LON, COUNTRY, DISTRICT, COUNTY, SUBCOUNTYID, PARISH, COMMUNITY_UNIT, WARD, LINKFACILITYID, AREACHIEFNAME, AREACHIEFPHONE, DISTANCETOBRANCH, TRANSPORTCOST, DISTANCETOMAINROAD, NOOFHOUSEHOLDS, MOHPOPLATIONDENSITY, ESTIMATEDPOPULATIONDENSITY, ECONOMICSTATUS, DISTANCETONEARESTHEALTHFACILITY, ACTLEVELS, ACTPRICE, MRDTLEVELS, MRDTPRICE, PRESENCEOFHOSTELS, PRESENCEOFESTATES, NUMBEROFFACTORIES, PRESENCEOFDISTRIBUTORS, DISTRIBUTORSINTHEAREA, TRADERMARKET, LARGESUPERMARKET, NGOSGIVINGFREEDRUGS, NGODOINGICCM, NGODOINGMHEALTH, NAMEOFNGODOINGICCM, NAMEOFNGODOINGMHEALTH, PRIVATEFACILITYFORACT, PRIVATEFACILITYFORMRDT, DATEADDED, ADDEDBY, COMMENT, SYNCED, CHVS_TRAINED, BRAC_OPERATING, SAFARICOM, MTN, AIRTEL, ORANGE, ACTSTOCK)

        val DB_UPDATE_V2 = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + ACTSTOCK + integer_field + ";"
    }
}

