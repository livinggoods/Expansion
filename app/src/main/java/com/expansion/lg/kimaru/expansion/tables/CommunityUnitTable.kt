package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.Partners
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable.Companion.CU_PARTNERS_TABLE
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable.Companion.TABLE_NAME

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class CommunityUnitTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    var columns = arrayOf(ID, NAME, MAPPINGID, LAT, LON, COUNTRY, SUBCOUNTYID, LINKFACILITYID, AREACHIEFNAME, WARD, ECONOMICSTATUS, PRIVATEFACILITYFORACT, PRIVATEFACILITYFORMRDT, NAMEOFNGODOINGICCM, NAMEOFNGODOINGMHEALTH, DATEADDED, ADDEDBY, NUMBEROFCHVS, HOUSEHOLDPERCHV, NUMBEROFVILLAGES, DISTANCETOBRANCH, TRANSPORTCOST, DISTANCETOMAINROAD, NOOFHOUSEHOLDS, MOHPOPLATIONDENSITY, ESTIMATEDPOPULATIONDENSITY, DISTANCETONEARESTHEALTHFACILITY, ACTLEVELS, ACTPRICE, MRDTLEVELS, MRDTPRICE, NOOFDISTIBUTORS, CHVSTRAINED, PRESENCEOFESTATES, PRESENCEOFFACTORIES, PRESENCEOFHOSTELS, TRADERMARKET, LARGESUPERMARKET, NGOSGIVINGFREEDRUGS, NGODOINGICCM, NGODOINGMHEALTH, CHVS_HOUSEHOLDS_AS_PER_CHIEF, POPULATION_AS_PER_CHIEF, COMMENT)

    internal var partnerColumns = arrayOf(ID, NAME, ICCM, ICCMCOMPONENT, MHEALTH, COMMENT, DATEADDED, ADDEDBY)
    internal var partnerCuColumns = arrayOf(ID, PARTNERID, CUID)

    val communityUnitData: List<CommunityUnit>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val communityUnitList = ArrayList<CommunityUnit>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                communityUnitList.add(cursorToCommunityUnit(cursor))
                cursor.moveToNext()
            }
            db.close()

            return communityUnitList
        }


    val communityUnitDataCursor: Cursor
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
                    results.put(CU_JSON_ROOT, resultSet)
                } catch (e: JSONException) {

                }

                cursor.moveToNext()
            }
            cursor.close()
            db.close()
            return results
        }

    fun addChiefsFields() {
        val db = readableDatabase
        db.execSQL(ADD_CHIEF_POP)
        db.execSQL(ADD_CHIEF_HOUSEHOLDS_PER_CHV)
    }

    fun addCommentField() {
        val db = readableDatabase
        db.execSQL(ADD_COMMENT_FIELD)
    }


    init {

        if (!isFieldExist(POPULATION_AS_PER_CHIEF)) {
            this.addChiefsFields()
        }
        if (!isFieldExist(COMMENT)) {
            this.addCommentField()
        }
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_DATABASE)
        db.execSQL(CREATE_PARTNERS)
        db.execSQL(CREATE_CU_PARTNERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addCommunityUnitData(communityUnit: CommunityUnit): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, communityUnit.id)
        cv.put(NAME, communityUnit.communityUnitName)
        cv.put(MAPPINGID, communityUnit.mappingId)
        cv.put(LAT, communityUnit.lat)
        cv.put(LON, communityUnit.lon)
        cv.put(COUNTRY, communityUnit.country)
        cv.put(SUBCOUNTYID, communityUnit.subCountyId)
        cv.put(LINKFACILITYID, communityUnit.linkFacilityId)
        cv.put(AREACHIEFNAME, communityUnit.areaChiefName)
        cv.put(WARD, communityUnit.ward)
        cv.put(ECONOMICSTATUS, communityUnit.economicStatus)
        cv.put(PRIVATEFACILITYFORACT, communityUnit.privateFacilityForAct)
        cv.put(PRIVATEFACILITYFORMRDT, communityUnit.privateFacilityForMrdt)
        cv.put(NAMEOFNGODOINGICCM, communityUnit.nameOfNgoDoingIccm)
        cv.put(NAMEOFNGODOINGMHEALTH, communityUnit.nameOfNgoDoingMhealth)
        cv.put(DATEADDED, communityUnit.dateAdded)
        cv.put(ADDEDBY, communityUnit.addedBy)
        cv.put(NUMBEROFCHVS, communityUnit.numberOfChvs)
        cv.put(HOUSEHOLDPERCHV, communityUnit.householdPerChv)
        cv.put(NUMBEROFVILLAGES, communityUnit.numberOfVillages)
        cv.put(DISTANCETOBRANCH, communityUnit.distanceToBranch)
        cv.put(TRANSPORTCOST, communityUnit.transportCost)
        cv.put(DISTANCETOMAINROAD, communityUnit.distanceTOMainRoad)
        cv.put(NOOFHOUSEHOLDS, communityUnit.noOfHouseholds)
        cv.put(MOHPOPLATIONDENSITY, communityUnit.mohPoplationDensity)
        cv.put(ESTIMATEDPOPULATIONDENSITY, communityUnit.estimatedPopulationDensity)
        cv.put(DISTANCETONEARESTHEALTHFACILITY, communityUnit.distanceTONearestHealthFacility)
        cv.put(ACTLEVELS, communityUnit.actLevels)
        cv.put(ACTPRICE, communityUnit.actPrice)
        cv.put(MRDTLEVELS, communityUnit.mrdtLevels)
        cv.put(MRDTPRICE, communityUnit.mrdtPrice)
        cv.put(NOOFDISTIBUTORS, communityUnit.noOfDistibutors)
        cv.put(CHVSTRAINED, communityUnit.isChvsTrained)
        cv.put(PRESENCEOFESTATES, communityUnit.isPresenceOfEstates)
        cv.put(PRESENCEOFFACTORIES, communityUnit.isPresenceOfFactories)
        cv.put(PRESENCEOFHOSTELS, communityUnit.isPresenceOfHostels)
        cv.put(TRADERMARKET, communityUnit.isTraderMarket)
        cv.put(LARGESUPERMARKET, communityUnit.isLargeSupermarket)
        cv.put(NGOSGIVINGFREEDRUGS, communityUnit.isNgosGivingFreeDrugs)
        cv.put(NGODOINGICCM, communityUnit.isNgoDoingIccm)
        cv.put(NGODOINGMHEALTH, communityUnit.isNgoDoingMhealth)
        cv.put(CHVS_HOUSEHOLDS_AS_PER_CHIEF, communityUnit.populationAsPerChief)
        cv.put(POPULATION_AS_PER_CHIEF, communityUnit.chvsHouseholdsAsPerChief)
        cv.put(COMMENT, communityUnit.comment)

        val id: Long
        if (isCommunityUnitExisting(communityUnit)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + communityUnit.id + "'", null).toLong()
            Log.d("Tremap DB OP", "Community Unit updated")
        } else {
            id = db.insert(TABLE_NAME, null, cv)
            Log.d("Tremap DB OP", "Community Unit Created")
        }
        db.close()
        return id
    }

    fun isCommunityUnitExisting(communityUnit: CommunityUnit): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '"
                + communityUnit.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }


    fun addPartnerCUData(id: String, partner: Partners, communityUnit: CommunityUnit): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, id)
        cv.put(PARTNERID, partner.partnerID)
        cv.put(CUID, communityUnit.id)

        return db.insertWithOnConflict(CU_PARTNERS_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getCommunityUnitById(uuid: String): CommunityUnit? {

        val db = readableDatabase

        val whereClause = "$ID = ? "

        val whereArgs = arrayOf(uuid)

        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val communityUnit = cursorToCommunityUnit(cursor)
            db.close()
            return communityUnit
        }
    }

    fun getCommunityUnitByName(communityUnitName: String): CommunityUnit? {
        val db = readableDatabase
        val whereClause = "$NAME = ? "
        val whereArgs = arrayOf(communityUnitName)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val communityUnit = cursorToCommunityUnit(cursor)
            db.close()
            return communityUnit
        }
    }

    fun getCommunityUnitBySubCounty(subCountyUUID: String): List<CommunityUnit> {
        val db = readableDatabase
        val whereClause = "$SUBCOUNTYID = ? "
        val whereArgs = arrayOf(subCountyUUID)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        val communityUnitList = ArrayList<CommunityUnit>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val communityUnit = cursorToCommunityUnit(cursor)
            communityUnitList.add(communityUnit)
            cursor.moveToNext()
        }
        db.close()

        return communityUnitList
    }

    fun getCommunityUnitByLinkFacility(linkFacilityId: String): List<CommunityUnit> {
        val db = readableDatabase
        val whereClause = "$LINKFACILITYID = ? "
        val whereArgs = arrayOf(linkFacilityId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        val communityUnitList = ArrayList<CommunityUnit>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            communityUnitList.add(cursorToCommunityUnit(cursor))
            cursor.moveToNext()
        }
        db.close()
        return communityUnitList
    }

    fun deleteCommunityUnit(communityUnit: CommunityUnit) {
        val db = readableDatabase
        db.delete(TABLE_NAME, "$ID = ?", arrayOf(communityUnit.id))
    }


    fun isFieldExist(fieldName: String): Boolean {
        val db = readableDatabase
        var isExist = false
        var res: Cursor? = null
        try {
            res = db.rawQuery("Select * from $TABLE_NAME limit 1", null)
            val colIndex = res!!.getColumnIndex(fieldName)
            if (colIndex != -1) {
                isExist = true
            } else {
                Log.d("Tremap", "The col $fieldName is NOT found")
            }
        } catch (e: Exception) {
            Log.d("Tremap", "Error getting  $fieldName")
        } finally {
            try {
                res?.close()
            } catch (e1: Exception) {
            }

        }
        return isExist
    }

    fun CuFromJson(jsonObject: JSONObject) {
        Log.d("Tremap", "Trying to Save teh CU from JSON")
        val communityUnit = CommunityUnit()
        try {

            communityUnit.id = jsonObject.getString(ID)
            communityUnit.communityUnitName = jsonObject.getString(NAME)
            communityUnit.mappingId = jsonObject.getString(MAPPINGID)
            communityUnit.lat = jsonObject.getDouble(LAT)
            communityUnit.lon = jsonObject.getDouble(LON)
            communityUnit.country = jsonObject.getString(COUNTRY)
            communityUnit.subCountyId = jsonObject.getString(SUBCOUNTYID)
            communityUnit.linkFacilityId = jsonObject.getString(LINKFACILITYID)
            communityUnit.areaChiefName = jsonObject.getString(AREACHIEFNAME)
            communityUnit.ward = jsonObject.getString(WARD)
            communityUnit.economicStatus = jsonObject.getString(ECONOMICSTATUS)
            communityUnit.privateFacilityForAct = jsonObject.getString(PRIVATEFACILITYFORACT)
            communityUnit.privateFacilityForMrdt = jsonObject.getString(PRIVATEFACILITYFORMRDT)
            communityUnit.nameOfNgoDoingIccm = jsonObject.getString(NAMEOFNGODOINGICCM)
            communityUnit.nameOfNgoDoingMhealth = jsonObject.getString(NAMEOFNGODOINGMHEALTH)
            communityUnit.dateAdded = jsonObject.getLong(DATEADDED)
            communityUnit.addedBy = jsonObject.getInt(ADDEDBY).toLong()
            communityUnit.numberOfChvs = jsonObject.getLong(NUMBEROFCHVS)
            communityUnit.householdPerChv = jsonObject.getLong(HOUSEHOLDPERCHV)
            communityUnit.numberOfVillages = jsonObject.getLong(NUMBEROFVILLAGES)
            communityUnit.distanceToBranch = jsonObject.getLong(DISTANCETOBRANCH)
            communityUnit.transportCost = jsonObject.getLong(TRANSPORTCOST)
            communityUnit.distanceTOMainRoad = jsonObject.getLong(DISTANCETOMAINROAD)
            communityUnit.noOfHouseholds = jsonObject.getLong(NOOFHOUSEHOLDS)
            communityUnit.mohPoplationDensity = jsonObject.getLong(MOHPOPLATIONDENSITY)
            communityUnit.estimatedPopulationDensity = jsonObject.getLong(ESTIMATEDPOPULATIONDENSITY)
            communityUnit.distanceTONearestHealthFacility = jsonObject.getLong(DISTANCETONEARESTHEALTHFACILITY)
            communityUnit.actLevels = jsonObject.getLong(ACTLEVELS)
            communityUnit.actPrice = jsonObject.getLong(ACTPRICE)
            communityUnit.mrdtLevels = jsonObject.getLong(MRDTLEVELS)
            communityUnit.mrdtPrice = jsonObject.getLong(MRDTPRICE)
            communityUnit.noOfDistibutors = jsonObject.getLong(NOOFDISTIBUTORS)
            communityUnit.isChvsTrained = jsonObject.getString(CHVSTRAINED).equals("1", ignoreCase = true)
            communityUnit.isPresenceOfEstates = jsonObject.getString(PRESENCEOFESTATES).equals("1", ignoreCase = true)
            communityUnit.setPresenceOfFactories(jsonObject.getLong(PRESENCEOFFACTORIES))
            communityUnit.isPresenceOfHostels = jsonObject.getString(PRESENCEOFHOSTELS).equals("1", ignoreCase = true)
            communityUnit.isTraderMarket = jsonObject.getString(TRADERMARKET).equals("1", ignoreCase = true)
            communityUnit.isLargeSupermarket = jsonObject.getString(LARGESUPERMARKET).equals("1", ignoreCase = true)
            communityUnit.isNgosGivingFreeDrugs = jsonObject.getString(NGOSGIVINGFREEDRUGS).equals("1", ignoreCase = true)
            communityUnit.isNgoDoingIccm = jsonObject.getString(NGODOINGICCM).equals("1", ignoreCase = true)
            communityUnit.isNgoDoingMhealth = jsonObject.getString(NGODOINGMHEALTH).equals("1", ignoreCase = true)
            communityUnit.populationAsPerChief = jsonObject.getLong(POPULATION_AS_PER_CHIEF)
            communityUnit.chvsHouseholdsAsPerChief = jsonObject.getLong(CHVS_HOUSEHOLDS_AS_PER_CHIEF)
            communityUnit.comment = jsonObject.getString(COMMENT)

            addCommunityUnitData(communityUnit)
        } catch (e: Exception) {
            Log.d("Tremap", "+++++++++++++++++++++++++++++++++++++++")
            Log.d("Tremap", "CU ERROR IN CREATING CU FROM JSON")
            Log.d("Tremap", "CE ERROR " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    private fun cursorToCommunityUnit(cursor: Cursor): CommunityUnit {
        val communityUnit = CommunityUnit()
        communityUnit.id = cursor.getString(cursor.getColumnIndex(ID))
        communityUnit.communityUnitName = cursor.getString(cursor.getColumnIndex(NAME))
        communityUnit.mappingId = cursor.getString(cursor.getColumnIndex(MAPPINGID))
        communityUnit.lat = cursor.getDouble(cursor.getColumnIndex(LAT))
        communityUnit.lon = cursor.getDouble(cursor.getColumnIndex(LON))
        communityUnit.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
        communityUnit.subCountyId = cursor.getString(cursor.getColumnIndex(SUBCOUNTYID))
        communityUnit.linkFacilityId = cursor.getString(cursor.getColumnIndex(LINKFACILITYID))
        communityUnit.areaChiefName = cursor.getString(cursor.getColumnIndex(AREACHIEFNAME))
        communityUnit.ward = cursor.getString(cursor.getColumnIndex(WARD))
        communityUnit.economicStatus = cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS))
        communityUnit.privateFacilityForAct = cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT))
        communityUnit.privateFacilityForMrdt = cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT))
        communityUnit.nameOfNgoDoingIccm = cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM))
        communityUnit.nameOfNgoDoingMhealth = cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH))
        communityUnit.dateAdded = cursor.getInt(cursor.getColumnIndex(DATEADDED)).toLong()
        communityUnit.addedBy = cursor.getInt(cursor.getColumnIndex(ADDEDBY)).toLong()
        communityUnit.numberOfChvs = cursor.getInt(cursor.getColumnIndex(NUMBEROFCHVS)).toLong()
        communityUnit.householdPerChv = cursor.getInt(cursor.getColumnIndex(HOUSEHOLDPERCHV)).toLong()
        communityUnit.numberOfVillages = cursor.getInt(cursor.getColumnIndex(NUMBEROFVILLAGES)).toLong()
        communityUnit.distanceToBranch = cursor.getInt(cursor.getColumnIndex(DISTANCETOBRANCH)).toLong()
        communityUnit.transportCost = cursor.getInt(cursor.getColumnIndex(TRANSPORTCOST)).toLong()
        communityUnit.distanceTOMainRoad = cursor.getInt(cursor.getColumnIndex(DISTANCETOMAINROAD)).toLong()
        communityUnit.noOfHouseholds = cursor.getInt(cursor.getColumnIndex(NOOFHOUSEHOLDS)).toLong()
        communityUnit.mohPoplationDensity = cursor.getInt(cursor.getColumnIndex(MOHPOPLATIONDENSITY)).toLong()
        communityUnit.estimatedPopulationDensity = cursor.getInt(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY)).toLong()
        communityUnit.distanceTONearestHealthFacility = cursor.getInt(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY)).toLong()
        communityUnit.actLevels = cursor.getInt(cursor.getColumnIndex(ACTLEVELS)).toLong()
        communityUnit.actPrice = cursor.getInt(cursor.getColumnIndex(ACTPRICE)).toLong()
        communityUnit.mrdtLevels = cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)).toLong()
        communityUnit.mrdtPrice = cursor.getInt(cursor.getColumnIndex(MRDTPRICE)).toLong()
        communityUnit.noOfDistibutors = cursor.getInt(cursor.getColumnIndex(NOOFDISTIBUTORS)).toLong()
        communityUnit.isChvsTrained = cursor.getInt(cursor.getColumnIndex(CHVSTRAINED)) == 1
        communityUnit.isPresenceOfEstates = cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES)) == 1
        communityUnit.setPresenceOfFactories(cursor.getLong(cursor.getColumnIndex(PRESENCEOFFACTORIES)))
        communityUnit.isPresenceOfHostels = cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS)) == 1
        communityUnit.isTraderMarket = cursor.getInt(cursor.getColumnIndex(TRADERMARKET)) == 1
        communityUnit.isLargeSupermarket = cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET)) == 1
        communityUnit.isNgosGivingFreeDrugs = cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS)) == 1
        communityUnit.isNgoDoingIccm = cursor.getInt(cursor.getColumnIndex(NGODOINGICCM)) == 1
        communityUnit.isNgoDoingMhealth = cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH)) == 1
        communityUnit.populationAsPerChief = cursor.getLong(cursor.getColumnIndex(POPULATION_AS_PER_CHIEF))
        communityUnit.chvsHouseholdsAsPerChief = cursor.getLong(cursor.getColumnIndex(CHVS_HOUSEHOLDS_AS_PER_CHIEF))
        communityUnit.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
        return communityUnit
    }

    companion object {

        val TABLE_NAME = "community_unit"
        val PARTNERS_TABLE = "partners"
        val CU_PARTNERS_TABLE = "cu_partners"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var varchar_field = " varchar(512) "
        var integer_field = " integer default 0 "
        var text_field = " text "

        var CU_JSON_ROOT = "community_unit"

        val ID = "id"
        val NAME = "name"
        val MAPPINGID = "mappingid"
        val LAT = "lat"
        val LON = "lon"
        val COUNTRY = "country"
        val SUBCOUNTYID = "subcountyid"
        val LINKFACILITYID = "linkfacilityid"
        val AREACHIEFNAME = "areachiefname"
        val WARD = "ward"
        val ECONOMICSTATUS = "economicstatus"
        val PRIVATEFACILITYFORACT = "privatefacilityforact"
        val PRIVATEFACILITYFORMRDT = "privatefacilityformrdt"
        val NAMEOFNGODOINGICCM = "nameofngodoingiccm"
        val NAMEOFNGODOINGMHEALTH = "nameofngodoingmhealth"
        val DATEADDED = "dateadded"
        val ADDEDBY = "addedby"
        val NUMBEROFCHVS = "numberofchvs"
        val HOUSEHOLDPERCHV = "householdperchv"
        val NUMBEROFVILLAGES = "numberofvillages"
        val DISTANCETOBRANCH = "distancetobranch"
        val TRANSPORTCOST = "transportcost"
        val DISTANCETOMAINROAD = "distancetomainroad"
        val NOOFHOUSEHOLDS = "noofhouseholds"
        val MOHPOPLATIONDENSITY = "mohpoplationdensity"
        val ESTIMATEDPOPULATIONDENSITY = "estimatedpopulationdensity"
        val DISTANCETONEARESTHEALTHFACILITY = "distancetonearesthealthfacility"
        val ACTLEVELS = "actlevels"
        val ACTPRICE = "actprice"
        val MRDTLEVELS = "mrdtlevels"
        val MRDTPRICE = "mrdtprice"
        val NOOFDISTIBUTORS = "noofdistibutors"
        val CHVSTRAINED = "chvstrained"
        val PRESENCEOFESTATES = "presenceofestates"
        val PRESENCEOFFACTORIES = "presenceoffactories"
        val PRESENCEOFHOSTELS = "presenceofhostels"
        val TRADERMARKET = "tradermarket"
        val LARGESUPERMARKET = "largesupermarket"
        val NGOSGIVINGFREEDRUGS = "ngosgivingfreedrugs"
        val NGODOINGICCM = "ngodoingiccm"
        val NGODOINGMHEALTH = "ngodoingmhealth"
        val ICCM = "iccm"
        val ICCMCOMPONENT = "iccm_component"
        val MHEALTH = "mhealth"
        val COMMENT = "comment"
        val PARTNERID = "partner_id"
        val CUID = "cu_id"
        val CHVS_HOUSEHOLDS_AS_PER_CHIEF = "chief_households_per_chv"
        val POPULATION_AS_PER_CHIEF = "population_as_per_chief"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ","
                + NAME + varchar_field + ","
                + MAPPINGID + varchar_field + ","
                + LAT + varchar_field + ","
                + LON + varchar_field + ","
                + COUNTRY + varchar_field + ","
                + SUBCOUNTYID + varchar_field + ","
                + LINKFACILITYID + varchar_field + ","
                + AREACHIEFNAME + varchar_field + ","
                + WARD + varchar_field + ","
                + ECONOMICSTATUS + varchar_field + ","
                + PRIVATEFACILITYFORACT + varchar_field + ","
                + PRIVATEFACILITYFORMRDT + varchar_field + ","
                + NAMEOFNGODOINGICCM + text_field + ","
                + NAMEOFNGODOINGMHEALTH + text_field + ","
                + DATEADDED + integer_field + ","
                + ADDEDBY + varchar_field + ","
                + NUMBEROFCHVS + integer_field + ","
                + HOUSEHOLDPERCHV + integer_field + ","
                + NUMBEROFVILLAGES + integer_field + ","
                + DISTANCETOBRANCH + integer_field + ","
                + TRANSPORTCOST + integer_field + ","
                + DISTANCETOMAINROAD + integer_field + ","
                + NOOFHOUSEHOLDS + integer_field + ","
                + MOHPOPLATIONDENSITY + integer_field + ","
                + ESTIMATEDPOPULATIONDENSITY + integer_field + ","
                + DISTANCETONEARESTHEALTHFACILITY + integer_field + ","
                + ACTLEVELS + integer_field + ","
                + ACTPRICE + integer_field + ","
                + MRDTLEVELS + integer_field + ","
                + MRDTPRICE + integer_field + ","
                + COMMENT + text_field + ","
                + NOOFDISTIBUTORS + integer_field + ","
                + CHVSTRAINED + integer_field + ","
                + PRESENCEOFESTATES + integer_field + ","
                + PRESENCEOFFACTORIES + integer_field + ","
                + PRESENCEOFHOSTELS + integer_field + ","
                + TRADERMARKET + integer_field + ","
                + LARGESUPERMARKET + integer_field + ","
                + NGOSGIVINGFREEDRUGS + integer_field + ","
                + NGODOINGICCM + integer_field + ","
                + CHVS_HOUSEHOLDS_AS_PER_CHIEF + integer_field + ","
                + POPULATION_AS_PER_CHIEF + integer_field + ","
                + NGODOINGMHEALTH + integer_field + ");")

        val ADD_CHIEF_POP = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + POPULATION_AS_PER_CHIEF + integer_field + ";"

        val ADD_CHIEF_HOUSEHOLDS_PER_CHV = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + CHVS_HOUSEHOLDS_AS_PER_CHIEF + integer_field + ";"

        val ADD_COMMENT_FIELD = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + COMMENT + text_field + ";"


        val CREATE_PARTNERS = ("CREATE TABLE " + PARTNERS_TABLE + "("
                + ID + varchar_field + ","
                + NAME + varchar_field + ","
                + ICCM + varchar_field + ","
                + ICCMCOMPONENT + varchar_field + ","
                + MHEALTH + varchar_field + ","
                + COMMENT + text_field + ","
                + DATEADDED + integer_field + ","
                + ADDEDBY + integer_field + "); ")

        val CREATE_CU_PARTNERS = ("CREATE TABLE " + CU_PARTNERS_TABLE + "("
                + ID + varchar_field + ","
                + PARTNERID + varchar_field + ","
                + CUID + varchar_field + ");")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
        val PARTNERS_DROP = "DROP TABLE IF EXISTS$PARTNERS_TABLE"
        val PARTNERS_CU_DROP = "DROP TABLE IF EXISTS$CU_PARTNERS_TABLE"
    }
}

