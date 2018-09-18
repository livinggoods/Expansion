package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.other.FileUtils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.UUID

/**
 * Created by kimaru on 3/11/17.
 */


class RegistrationTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    val registrationDataCursor: Cursor
        get() {

            val db = readableDatabase
            val orderBy = "id desc"

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, orderBy, null)
            db.close()
            return cursor
        }

    val registrationData: List<Registration>
        get() {

            val db = readableDatabase
            val orderBy = "id desc"

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, orderBy, null)

            val registrationList = ArrayList<Registration>()


            cursor.moveToFirst()
            while (!cursor.isAfterLast) {


                val registration = Registration()

                registration.id = cursor.getString(0)
                registration.name = cursor.getString(1)
                registration.phone = cursor.getString(2)
                registration.gender = cursor.getString(3)
                registration.dob = cursor.getLong(4)
                registration.district = cursor.getString(5)
                registration.subcounty = cursor.getString(6)
                registration.division = cursor.getString(7)
                registration.village = cursor.getString(8)
                registration.mark = cursor.getString(9)
                registration.readEnglish = cursor.getInt(10)
                registration.dateMoved = cursor.getLong(11)
                registration.langs = cursor.getString(12)
                registration.brac = cursor.getInt(13)
                registration.bracChp = cursor.getInt(14)
                registration.education = cursor.getString(15)
                registration.occupation = cursor.getString(16)
                registration.community = cursor.getInt(17)
                registration.addedBy = cursor.getInt(18)
                registration.comment = cursor.getString(19)
                registration.proceed = cursor.getInt(20)
                registration.dateAdded = cursor.getLong(21)
                registration.synced = cursor.getInt(22)
                registration.recruitment = cursor.getString(23)
                registration.country = cursor.getString(24)
                registration.chewName = cursor.getString(25)
                registration.chewNumber = cursor.getString(26)
                registration.ward = cursor.getString(27)
                registration.cuName = cursor.getString(28)
                registration.linkFacility = cursor.getString(29)
                registration.noOfHouseholds = cursor.getLong(30)
                registration.otherTrainings = cursor.getString(31)
                registration.isChv = cursor.getInt(32) == 1
                registration.isGokTrained = cursor.getInt(33) == 1
                registration.referralName = cursor.getString(34)
                registration.referralPhone = cursor.getString(35)
                registration.referralTitle = cursor.getString(36)
                registration.isVht = cursor.getInt(37) == 1
                registration.parish = cursor.getString(38)
                registration.isAccounts = cursor.getInt(39) == 1
                registration.recruitmentTransportCost = cursor.getLong(40)
                registration.transportCostToBranch = cursor.getLong(41)
                registration.picture = ""
                registration.chewUuid = cursor.getString(42)
                registration.maritalStatus = cursor.getString(cursor.getColumnIndex(MARITAL_STATUS))
                registrationList.add(registration)
                cursor.moveToNext()
            }
            db.close()
            return registrationList
        }


    val registrationCount: Long
        get() {
            val db = this.readableDatabase
            val cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME)
            db.close()
            return cnt
        }

    val registrationJson: JSONObject
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
    val registrationToSyncAsJson: JSONObject
        get() {

            val db = readableDatabase
            val whereClause = "$SYNCED = ?"
            val whereArgs = arrayOf("0")

            val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

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
        // need to sequentially upgrade the database
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
        //if (oldVersion < 3){
        //    upgradeVersion3(db);
        //}
        //if (oldVersion < 3){
        //    upgradeVersion3(db);
        //}
    }

    fun addData(registration: Registration): Long {

        val db = writableDatabase

        val cv = ContentValues()
        cv.put(ID, registration.id)
        cv.put(NAME, registration.name)
        cv.put(PHONE, registration.phone)
        cv.put(GENDER, registration.gender)
        cv.put(DOB, registration.dob)
        cv.put(COUNTRY, registration.country)
        cv.put(RECRUITMENT, registration.recruitment)
        cv.put(DISTRICT, registration.district)
        cv.put(SUB_COUNTY, registration.subcounty)
        cv.put(DIVISION, registration.division)
        cv.put(VILLAGE, registration.village)
        cv.put(MARK, registration.mark)
        cv.put(READ_ENGLISH, registration.readEnglish)
        cv.put(DATE_MOVED, registration.dateMoved)
        cv.put(LANGS, registration.langs)
        cv.put(BRAC, registration.brac)
        cv.put(BRAC_CHP, registration.bracChp)
        cv.put(EDUCATION, registration.education)
        cv.put(OCCUPATION, registration.occupation)
        cv.put(COMMUNITY, registration.community)
        cv.put(ADDED_BY, registration.addedBy)
        cv.put(COMMENT, registration.comment)
        cv.put(PROCEED, registration.proceed)
        cv.put(DATE_ADDED, registration.dateAdded)
        cv.put(SYNCED, registration.synced)
        cv.put(CHEW_NAME, registration.chewName)
        cv.put(CHEW_NUMBER, registration.chewNumber)
        cv.put(WARD, registration.ward)
        cv.put(CU_NAME, registration.cuName)
        cv.put(LINK_FACILITY, registration.linkFacility)
        cv.put(HOUSEHOLDS, registration.noOfHouseholds)
        cv.put(TRAININGS, registration.otherTrainings)
        cv.put(CHV, if (registration.isChv) 1 else 0)
        cv.put(GOK_TRAINED, if (registration.isGokTrained) 1 else 0)
        cv.put(REFERRAL_NAME, registration.referralName)
        cv.put(REFERRAL_NUMBER, registration.referralPhone)
        cv.put(REFERRAL_TITLE, registration.referralTitle)
        cv.put(VHT, if (registration.isVht) 1 else 0)
        cv.put(PARISH, registration.parish)
        cv.put(ACCOUNTS, if (registration.isAccounts) 1 else 0)
        cv.put(REC_TRANSPORT, registration.recruitmentTransportCost)
        cv.put(BRANCH_TRANPORT, registration.transportCostToBranch)
        cv.put(CHEW_ID, registration.chewUuid)
        cv.put(MARITAL_STATUS, registration.maritalStatus)

        val id: Long
        if (isExist(registration)) {
            cv.put(SYNCED, 0)
            id = db.update(TABLE_NAME, cv, ID + "='" + registration.id + "'", null).toLong()
        } else {
            id = db.insert(TABLE_NAME, null, cv)
        }
        db.close()
        return id

    }


    fun searchRegistrations(recruitment: Recruitment, query: String): List<Registration> {

        val db = readableDatabase
        val orderBy = "id desc"
        val whereClause = NAME + " LIKE ? OR " +
                VILLAGE + " LIKE ? OR " +
                WARD + " LIKE ? OR " +
                COMMUNITY + " LIKE ? " +
                "AND " + RECRUITMENT + " = ? "
        val whereArgs = arrayOf("%$query%", "%$query%", "%$query%", "%$query%", recruitment.id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        val registrationList = ArrayList<Registration>()


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val registration = Registration()

            registration.id = cursor.getString(0)
            registration.name = cursor.getString(1)
            registration.phone = cursor.getString(2)
            registration.gender = cursor.getString(3)
            registration.dob = cursor.getLong(4)
            registration.district = cursor.getString(5)
            registration.subcounty = cursor.getString(6)
            registration.division = cursor.getString(7)
            registration.village = cursor.getString(8)
            registration.mark = cursor.getString(9)
            registration.readEnglish = cursor.getInt(10)
            registration.dateMoved = cursor.getLong(11)
            registration.langs = cursor.getString(12)
            registration.brac = cursor.getInt(13)
            registration.bracChp = cursor.getInt(14)
            registration.education = cursor.getString(15)
            registration.occupation = cursor.getString(16)
            registration.community = cursor.getInt(17)
            registration.addedBy = cursor.getInt(18)
            registration.comment = cursor.getString(19)
            registration.proceed = cursor.getInt(20)
            registration.dateAdded = cursor.getLong(21)
            registration.synced = cursor.getInt(22)
            registration.recruitment = cursor.getString(23)
            registration.country = cursor.getString(24)
            registration.chewName = cursor.getString(25)
            registration.chewNumber = cursor.getString(26)
            registration.ward = cursor.getString(27)
            registration.cuName = cursor.getString(28)
            registration.linkFacility = cursor.getString(29)
            registration.noOfHouseholds = cursor.getLong(30)
            registration.otherTrainings = cursor.getString(31)
            registration.isChv = cursor.getInt(32) == 1
            registration.isGokTrained = cursor.getInt(33) == 1
            registration.referralName = cursor.getString(34)
            registration.referralPhone = cursor.getString(35)
            registration.referralTitle = cursor.getString(36)
            registration.isVht = cursor.getInt(37) == 1
            registration.parish = cursor.getString(38)
            registration.isAccounts = cursor.getInt(39) == 1
            registration.recruitmentTransportCost = cursor.getLong(40)
            registration.transportCostToBranch = cursor.getLong(41)
            registration.chewUuid = cursor.getString(42)
            registration.maritalStatus = cursor.getString(cursor.getColumnIndex(MARITAL_STATUS))
            registration.picture = ""
            registrationList.add(registration)
            cursor.moveToNext()
        }
        db.close()
        return registrationList
    }

    fun getRegistrationById(registrationUuid: String): Registration? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(registrationUuid)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val registration = Registration()

            registration.id = cursor.getString(0)
            registration.name = cursor.getString(1)
            registration.phone = cursor.getString(2)
            registration.gender = cursor.getString(3)
            registration.dob = cursor.getLong(4)
            registration.district = cursor.getString(5)
            registration.subcounty = cursor.getString(6)
            registration.division = cursor.getString(7)
            registration.village = cursor.getString(8)
            registration.mark = cursor.getString(9)
            registration.readEnglish = cursor.getInt(10)
            registration.dateMoved = cursor.getLong(11)
            registration.langs = cursor.getString(12)
            registration.brac = cursor.getInt(13)
            registration.bracChp = cursor.getInt(14)
            registration.education = cursor.getString(15)
            registration.occupation = cursor.getString(16)
            registration.community = cursor.getInt(17)
            registration.addedBy = cursor.getInt(18)
            registration.comment = cursor.getString(19)
            registration.proceed = cursor.getInt(20)
            registration.dateAdded = cursor.getLong(21)
            registration.synced = cursor.getInt(22)
            registration.recruitment = cursor.getString(23)
            registration.country = cursor.getString(24)
            registration.chewName = cursor.getString(25)
            registration.chewNumber = cursor.getString(26)
            registration.ward = cursor.getString(27)
            registration.cuName = cursor.getString(28)
            registration.linkFacility = cursor.getString(29)
            registration.noOfHouseholds = cursor.getLong(30)
            registration.otherTrainings = cursor.getString(31)
            registration.isChv = cursor.getInt(32) == 1
            registration.isGokTrained = cursor.getInt(33) == 1
            registration.referralName = cursor.getString(34)
            registration.referralPhone = cursor.getString(35)
            registration.referralTitle = cursor.getString(36)
            registration.isVht = cursor.getInt(37) == 1
            registration.parish = cursor.getString(38)
            registration.isAccounts = cursor.getInt(39) == 1
            registration.recruitmentTransportCost = cursor.getLong(40)
            registration.transportCostToBranch = cursor.getLong(41)
            registration.chewUuid = cursor.getString(42)
            registration.maritalStatus = cursor.getString(cursor.getColumnIndex(MARITAL_STATUS))
            registration.picture = ""
            db.close()
            return registration
        }

    }


    fun isExist(registration: Registration): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + ID + " = '" + registration.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist
    }

    fun getPassedRegistrations(recruitment: Recruitment, passed: Boolean): List<Registration> {

        val db = readableDatabase
        val orderBy = "$DATE_ADDED desc"
        val whereClause = "$PROCEED = ? AND $RECRUITMENT = ? "

        val cursor: Cursor
        if (passed) {
            val whereArgs = arrayOf("1", recruitment.id)
            cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        } else {
            val whereArgs = arrayOf("0", recruitment.id)
            cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        }

        val registrationList = ArrayList<Registration>()


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {

            val registration = Registration()

            registration.id = cursor.getString(0)
            registration.name = cursor.getString(1)
            registration.phone = cursor.getString(2)
            registration.gender = cursor.getString(3)
            registration.dob = cursor.getLong(4)
            registration.district = cursor.getString(5)
            registration.subcounty = cursor.getString(6)
            registration.division = cursor.getString(7)
            registration.village = cursor.getString(8)
            registration.mark = cursor.getString(9)
            registration.readEnglish = cursor.getInt(10)
            registration.dateMoved = cursor.getLong(11)
            registration.langs = cursor.getString(12)
            registration.brac = cursor.getInt(13)
            registration.bracChp = cursor.getInt(14)
            registration.education = cursor.getString(15)
            registration.occupation = cursor.getString(16)
            registration.community = cursor.getInt(17)
            registration.addedBy = cursor.getInt(18)
            registration.comment = cursor.getString(19)
            registration.proceed = cursor.getInt(20)
            registration.dateAdded = cursor.getLong(21)
            registration.synced = cursor.getInt(22)
            registration.recruitment = cursor.getString(23)
            registration.country = cursor.getString(24)
            registration.chewName = cursor.getString(25)
            registration.chewNumber = cursor.getString(26)
            registration.ward = cursor.getString(27)
            registration.cuName = cursor.getString(28)
            registration.linkFacility = cursor.getString(29)
            registration.noOfHouseholds = cursor.getLong(30)
            registration.otherTrainings = cursor.getString(31)
            registration.isChv = cursor.getInt(32) == 1
            registration.isGokTrained = cursor.getInt(33) == 1
            registration.referralName = cursor.getString(34)
            registration.referralPhone = cursor.getString(35)
            registration.referralTitle = cursor.getString(36)
            registration.isVht = cursor.getInt(37) == 1
            registration.parish = cursor.getString(38)
            registration.isAccounts = cursor.getInt(39) == 1
            registration.recruitmentTransportCost = cursor.getLong(40)
            registration.transportCostToBranch = cursor.getLong(41)
            registration.chewUuid = cursor.getString(42)
            registration.maritalStatus = cursor.getString(cursor.getColumnIndex(MARITAL_STATUS))
            registration.picture = ""
            registrationList.add(registration)
            cursor.moveToNext()
        }
        db.close()
        return registrationList
    }

    fun getRegistrationsByRecruitment(recruitment: Recruitment): List<Registration> {

        val db = readableDatabase
        val orderBy = "$NAME asc,$DATE_ADDED desc"
        val whereClause = "$RECRUITMENT = ?"
        val whereArgs = arrayOf(recruitment.id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

        val registrationList = ArrayList<Registration>()


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {


            val registration = Registration()

            registration.id = cursor.getString(0)
            registration.name = cursor.getString(1)
            registration.phone = cursor.getString(2)
            registration.gender = cursor.getString(3)
            registration.dob = cursor.getLong(4)
            registration.district = cursor.getString(5)
            registration.subcounty = cursor.getString(6)
            registration.division = cursor.getString(7)
            registration.village = cursor.getString(8)
            registration.mark = cursor.getString(9)
            registration.readEnglish = cursor.getInt(10)
            registration.dateMoved = cursor.getLong(11)
            registration.langs = cursor.getString(12)
            registration.brac = cursor.getInt(13)
            registration.bracChp = cursor.getInt(14)
            registration.education = cursor.getString(15)
            registration.occupation = cursor.getString(16)
            registration.community = cursor.getInt(17)
            registration.addedBy = cursor.getInt(18)
            registration.comment = cursor.getString(19)
            registration.proceed = cursor.getInt(20)
            registration.dateAdded = cursor.getLong(21)
            registration.synced = cursor.getInt(22)
            registration.recruitment = cursor.getString(23)
            registration.country = cursor.getString(24)
            registration.chewName = cursor.getString(25)
            registration.chewNumber = cursor.getString(26)
            registration.ward = cursor.getString(27)
            registration.cuName = cursor.getString(28)
            registration.linkFacility = cursor.getString(29)
            registration.noOfHouseholds = cursor.getLong(30)
            registration.otherTrainings = cursor.getString(31)
            registration.isChv = cursor.getInt(32) == 1
            registration.isGokTrained = cursor.getInt(33) == 1
            registration.referralName = cursor.getString(34)
            registration.referralPhone = cursor.getString(35)
            registration.referralTitle = cursor.getString(36)
            registration.isVht = cursor.getInt(37) == 1
            registration.parish = cursor.getString(38)
            registration.isAccounts = cursor.getInt(39) == 1
            registration.recruitmentTransportCost = cursor.getLong(40)
            registration.transportCostToBranch = cursor.getLong(41)
            registration.chewUuid = cursor.getString(42)
            registration.maritalStatus = cursor.getString(cursor.getColumnIndex(MARITAL_STATUS))
            registration.picture = ""
            registrationList.add(registration)
            cursor.moveToNext()
        }
        db.close()
        return registrationList
    }

    fun getRegistrationsByRecruitmentAndCommunityUnit(
            recruitment: Recruitment, communityUnit: CommunityUnit): List<Registration> {

        val db = readableDatabase
        val orderBy = "$NAME asc,$DATE_ADDED desc"
        val whereClause = "$RECRUITMENT = ? AND $CU_NAME = ? "
        val whereArgs = arrayOf(recruitment.id, communityUnit.id)

        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)
        val registrationList = ArrayList<Registration>()


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {


            val registration = Registration()

            registration.id = cursor.getString(0)
            registration.name = cursor.getString(1)
            registration.phone = cursor.getString(2)
            registration.gender = cursor.getString(3)
            registration.dob = cursor.getLong(4)
            registration.district = cursor.getString(5)
            registration.subcounty = cursor.getString(6)
            registration.division = cursor.getString(7)
            registration.village = cursor.getString(8)
            registration.mark = cursor.getString(9)
            registration.readEnglish = cursor.getInt(10)
            registration.dateMoved = cursor.getLong(11)
            registration.langs = cursor.getString(12)
            registration.brac = cursor.getInt(13)
            registration.bracChp = cursor.getInt(14)
            registration.education = cursor.getString(15)
            registration.occupation = cursor.getString(16)
            registration.community = cursor.getInt(17)
            registration.addedBy = cursor.getInt(18)
            registration.comment = cursor.getString(19)
            registration.proceed = cursor.getInt(20)
            registration.dateAdded = cursor.getLong(21)
            registration.synced = cursor.getInt(22)
            registration.recruitment = cursor.getString(23)
            registration.country = cursor.getString(24)
            registration.chewName = cursor.getString(25)
            registration.chewNumber = cursor.getString(26)
            registration.ward = cursor.getString(27)
            registration.cuName = cursor.getString(28)
            registration.linkFacility = cursor.getString(29)
            registration.noOfHouseholds = cursor.getLong(30)
            registration.otherTrainings = cursor.getString(31)
            registration.isChv = cursor.getInt(32) == 1
            registration.isGokTrained = cursor.getInt(33) == 1
            registration.referralName = cursor.getString(34)
            registration.referralPhone = cursor.getString(35)
            registration.referralTitle = cursor.getString(36)
            registration.isVht = cursor.getInt(37) == 1
            registration.parish = cursor.getString(38)
            registration.isAccounts = cursor.getInt(39) == 1
            registration.recruitmentTransportCost = cursor.getLong(40)
            registration.transportCostToBranch = cursor.getLong(41)
            registration.chewUuid = cursor.getString(42)
            registration.maritalStatus = cursor.getString(cursor.getColumnIndex(MARITAL_STATUS))
            registration.picture = ""
            registrationList.add(registration)
            cursor.moveToNext()
        }
        db.close()
        return registrationList
    }

    fun getRegistrationsByChewReferral(chewReferral: ChewReferral): List<Registration> {

        val db = readableDatabase
        val orderBy = "$NAME asc,$DATE_ADDED desc"
        val whereClause = "$RECRUITMENT = ?"
        val whereArgs = arrayOf(chewReferral.id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null)

        val registrationList = ArrayList<Registration>()


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {


            val registration = Registration()

            registration.id = cursor.getString(0)
            registration.name = cursor.getString(1)
            registration.phone = cursor.getString(2)
            registration.gender = cursor.getString(3)
            registration.dob = cursor.getLong(4)
            registration.district = cursor.getString(5)
            registration.subcounty = cursor.getString(6)
            registration.division = cursor.getString(7)
            registration.village = cursor.getString(8)
            registration.mark = cursor.getString(9)
            registration.readEnglish = cursor.getInt(10)
            registration.dateMoved = cursor.getLong(11)
            registration.langs = cursor.getString(12)
            registration.brac = cursor.getInt(13)
            registration.bracChp = cursor.getInt(14)
            registration.education = cursor.getString(15)
            registration.occupation = cursor.getString(16)
            registration.community = cursor.getInt(17)
            registration.addedBy = cursor.getInt(18)
            registration.comment = cursor.getString(19)
            registration.proceed = cursor.getInt(20)
            registration.dateAdded = cursor.getLong(21)
            registration.synced = cursor.getInt(22)
            registration.recruitment = cursor.getString(23)
            registration.country = cursor.getString(24)
            registration.chewName = cursor.getString(25)
            registration.chewNumber = cursor.getString(26)
            registration.ward = cursor.getString(27)
            registration.cuName = cursor.getString(28)
            registration.linkFacility = cursor.getString(29)
            registration.noOfHouseholds = cursor.getLong(30)
            registration.otherTrainings = cursor.getString(31)
            registration.isChv = cursor.getInt(32) == 1
            registration.isGokTrained = cursor.getInt(33) == 1
            registration.referralName = cursor.getString(34)
            registration.referralPhone = cursor.getString(35)
            registration.referralTitle = cursor.getString(36)
            registration.isVht = cursor.getInt(37) == 1
            registration.parish = cursor.getString(38)
            registration.isAccounts = cursor.getInt(39) == 1
            registration.recruitmentTransportCost = cursor.getLong(40)
            registration.transportCostToBranch = cursor.getLong(41)
            registration.chewUuid = cursor.getString(42)
            registration.maritalStatus = cursor.getString(cursor.getColumnIndex(MARITAL_STATUS))
            registration.picture = ""
            registrationList.add(registration)
            cursor.moveToNext()
        }
        db.close()
        return registrationList
    }


    fun fromJsonObject(jsonObject: JSONObject) {
        try {
            val registration = Registration()
            registration.id = jsonObject.getString(ID)
            registration.name = jsonObject.getString(NAME)
            registration.phone = jsonObject.getString(PHONE)
            registration.gender = jsonObject.getString(GENDER)
            registration.dob = jsonObject.getLong(DOB)
            registration.district = jsonObject.getString(DISTRICT)
            registration.country = jsonObject.getString(COUNTRY)
            registration.recruitment = jsonObject.getString(RECRUITMENT)
            registration.subcounty = jsonObject.getString(SUB_COUNTY)
            registration.division = jsonObject.getString(DIVISION)
            registration.village = jsonObject.getString(VILLAGE)
            registration.mark = jsonObject.getString(MARK)
            registration.readEnglish = jsonObject.getInt(READ_ENGLISH)
            registration.dateMoved = jsonObject.getLong(DATE_MOVED)
            registration.langs = jsonObject.getString(LANGS)
            registration.brac = jsonObject.getInt(BRAC)
            registration.bracChp = jsonObject.getInt(BRAC_CHP)
            registration.education = jsonObject.getString(EDUCATION)
            registration.occupation = jsonObject.getString(OCCUPATION)
            registration.community = jsonObject.getInt(COMMUNITY)
            registration.addedBy = jsonObject.getInt(ADDED_BY)
            registration.comment = jsonObject.getString(COMMENT)
            registration.proceed = jsonObject.getInt(PROCEED)
            registration.dateAdded = jsonObject.getLong(DATE_ADDED)
            registration.synced = jsonObject.getInt(SYNCED)
            registration.chewName = jsonObject.getString(CHEW_NAME)
            registration.chewNumber = jsonObject.getString(CHEW_NUMBER)
            registration.ward = jsonObject.getString(WARD)
            registration.cuName = jsonObject.getString(CU_NAME)
            registration.linkFacility = jsonObject.getString(LINK_FACILITY)
            registration.noOfHouseholds = jsonObject.getLong(HOUSEHOLDS)
            registration.otherTrainings = jsonObject.getString(TRAININGS)
            registration.isChv = jsonObject.getInt(CHV) == 1
            registration.isGokTrained = jsonObject.getInt(GOK_TRAINED) == 1
            registration.referralName = jsonObject.getString(REFERRAL_NAME)
            registration.referralPhone = jsonObject.getString(REFERRAL_NUMBER)
            registration.referralTitle = jsonObject.getString(REFERRAL_TITLE)
            registration.isVht = jsonObject.getInt(VHT) == 1
            registration.parish = jsonObject.getString(PARISH)
            registration.isAccounts = jsonObject.getInt(ACCOUNTS) == 1
            registration.recruitmentTransportCost = jsonObject.getLong(REC_TRANSPORT)
            registration.transportCostToBranch = jsonObject.getLong(BRANCH_TRANPORT)
            registration.chewUuid = jsonObject.getString(CHEW_ID)
            registration.maritalStatus = jsonObject.getString(MARITAL_STATUS)
            registration.picture = ""
            this.addData(registration)
        } catch (e: Exception) {
        }

    }

    //I will keep migration code in functions
    private fun upgradeVersion2(db: SQLiteDatabase) {
        db.execSQL(DB_UPDATE_V2)

        val columns = arrayOf(ID, NAME, PHONE, GENDER, DOB, DISTRICT, SUB_COUNTY, DIVISION, VILLAGE, MARK, READ_ENGLISH, DATE_MOVED, LANGS, BRAC, BRAC_CHP, EDUCATION, OCCUPATION, COMMUNITY, ADDED_BY, COMMENT, PROCEED, DATE_ADDED, SYNCED, RECRUITMENT, COUNTRY, CHEW_NAME, CHEW_NUMBER, WARD, CU_NAME, LINK_FACILITY, HOUSEHOLDS, TRAININGS, CHV, GOK_TRAINED, REFERRAL_NAME, REFERRAL_NUMBER, REFERRAL_TITLE, VHT, PARISH, ACCOUNTS, REC_TRANSPORT, BRANCH_TRANPORT)
        val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
        val chewReferralTable = ChewReferralTable(context)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            // with this selection, let us extract the details
            //
            // IF CHEW EXISTS DO NOT CREATE< JUST UPDATE THE REGISTRATION
            //
            //getChewByPhone();
            //getChewbyName();
            val country = cursor.getString(24)
            if (country.equals("UG", ignoreCase = true)) {
                var id: String
                var name: String
                var phone: String
                var title: String
                var recruitmentID: String
                id = UUID.randomUUID().toString()
                name = cursor.getString(34)
                phone = cursor.getString(35)
                title = cursor.getString(36)
                recruitmentID = cursor.getString(23)
                // check if the referral is already in the DB
                val chews = chewReferralTable.getChewReferralByPhone(phone)
                val savedChews = chews.size
                val cv = ContentValues()
                if (savedChews == 0) {
                    //does not exist
                    val chew = ChewReferral(id, name, phone, title, country, recruitmentID, 0,
                            "", "", "", "", "", "", "", "", "")
                    chewReferralTable.addChewReferral(chew)
                    cv.put(CHEW_ID, id)
                    db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)

                } else if (savedChews == 1) {
                    //exists, and it is the only one, get it and
                    val c = chews[0]
                    cv.put(CHEW_ID, c.id)
                    db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)

                } else {
                    // it is more than one
                    // we compare the names, if match, we update, if not, we create a new one
                    for (c in chews) {
                        if (c.name.equals(name, ignoreCase = true)) {
                            // we have a match
                            id = c.id
                            name = c.name
                            phone = c.phone
                            title = c.title
                            recruitmentID = c.recruitmentId
                            break
                        }
                    }
                    val chew = ChewReferral(id, name, phone, title, country, recruitmentID, 0,
                            "", "", "", "", "", "", "", "", "")
                    chewReferralTable.addChewReferral(chew)
                    cv.put(CHEW_ID, id)
                    db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                }

            } else {
                //we call them CHEW
                var id: String
                var name: String
                var phone: String
                var title: String
                var recruitmentID: String
                val synced: String
                id = UUID.randomUUID().toString()
                name = cursor.getString(25)
                phone = cursor.getString(26)
                title = "CHEW"
                recruitmentID = cursor.getString(23)
                val chews = chewReferralTable.getChewReferralByPhone(phone)
                val savedChews = chews.size
                val cv = ContentValues()
                if (savedChews == 0) {
                    val chew = ChewReferral(id, name, phone, title, country, recruitmentID, 0,
                            "", "", "", "", "", "", "", "", "")
                    chewReferralTable.addChewReferral(chew)
                    cv.put(CHEW_ID, id)
                    db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                } else if (savedChews == 1) {
                    //exists, and it is the only one, get it and
                    val c = chews[0]
                    cv.put(CHEW_ID, c.id)
                    db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                } else {
                    // it is more than one
                    // we compare the names, if match, we update, if not, we create a new one
                    for (c in chews) {
                        if (c.name.equals(name, ignoreCase = true)) {
                            // we have a match
                            id = c.id
                            name = c.name
                            phone = c.phone
                            title = c.title
                            recruitmentID = c.recruitmentId
                            break
                        }
                    }
                    val chew = ChewReferral(id, name, phone, title, country, recruitmentID, 0,
                            "", "", "", "", "", "", "", "", "")
                    chewReferralTable.addChewReferral(chew)
                    cv.put(CHEW_ID, id)
                    db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                }
                // Update the Community Unit.
                // Update the Link Facility

                // Check if there is a Link Facility
                val lFTbl = LinkFacilityTable(context)
                val cuTbl = CommunityUnitTable(context)
                val linkFacilityUuid = UUID.randomUUID().toString()
                val communityUnitUuid = UUID.randomUUID().toString()
                if (!cursor.getString(cursor.getColumnIndex(LINK_FACILITY)).equals("", ignoreCase = true)) {
                    val linkFacility = lFTbl.getLinkFacilityByName(cursor.getString(cursor.getColumnIndex(LINK_FACILITY)))
                    if (linkFacility != null) {
                        cv.put(LINK_FACILITY, linkFacility.id)
                        db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                    } else {
                        // create the Link Facility
                        // For some registration, Subcounty is missing.
                        val rctbl = RecruitmentTable(context)
                        val registrationRecruitment = rctbl.getRecruitmentById(cursor
                                .getString(cursor.getColumnIndex(RECRUITMENT)))
                        val newLinkFacility = LinkFacility()
                        newLinkFacility.id = linkFacilityUuid
                        newLinkFacility.facilityName = cursor.getString(29)
                        newLinkFacility.mappingId = cursor.getString(23)
                        newLinkFacility.lat = 0.0
                        newLinkFacility.lon = 0.0
                        newLinkFacility.subCountyId = registrationRecruitment!!.subcounty
                        newLinkFacility.dateAdded = cursor.getLong(21)
                        newLinkFacility.addedBy = cursor.getInt(18)
                        newLinkFacility.setMrdtLevels(0)
                        newLinkFacility.setActLevels(0)
                        newLinkFacility.country = cursor.getString(24)
                        lFTbl.addData(newLinkFacility)
                        cv.put(LINK_FACILITY, linkFacilityUuid)
                        db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                    }
                }
                if (!cursor.getString(cursor.getColumnIndex(COMMUNITY)).equals("", ignoreCase = true)) {
                    val communityUnit = cuTbl.getCommunityUnitByName(cursor
                            .getString(cursor.getColumnIndex(CU_NAME)))
                    if (communityUnit != null) {
                        cv.put(CU_NAME, communityUnit.id)
                        db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                    } else {
                        // create the Link Facility
                        val recruitmentTable = RecruitmentTable(context)

                        val recruitment = recruitmentTable.getRecruitmentById(recruitmentID)
                        val newCommunityUnit = CommunityUnit()
                        newCommunityUnit.id = communityUnitUuid
                        newCommunityUnit.communityUnitName = cursor.getString(cursor.getColumnIndex(CU_NAME))
                        newCommunityUnit.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                        newCommunityUnit.subCountyId = recruitment!!.subcounty
                        newCommunityUnit.linkFacilityId = linkFacilityUuid
                        newCommunityUnit.dateAdded = cursor.getLong(cursor.getColumnIndex(DATE_ADDED))
                        newCommunityUnit.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY)).toLong()
                        cuTbl.addCommunityUnitData(newCommunityUnit)

                        cv.put(CU_NAME, communityUnitUuid)
                        cv.put(SUB_COUNTY, recruitment.subcounty)
                        db.update(TABLE_NAME, cv, ID + "='" + cursor.getString(0) + "'", null)
                    }
                }
            }
            cursor.moveToNext()
        }
        // we can remove the unnecessary fields
        // but SQLite does not allow deleting the rows, too bad
        // THe hack that is common is to copy the table onto another one, and deleting the old one
        // with unwanted fields.
    }

    companion object {

        val TABLE_NAME = "registration"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION
        val JSON_ROOT = "registrations"

        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "

        val ID = "id"
        val NAME = "name"
        val PHONE = "phone"
        val GENDER = "gender"
        val RECRUITMENT = "recruitment"
        val COUNTRY = "country"
        val DOB = "dob"
        val DISTRICT = "district"
        val SUB_COUNTY = "subcounty"
        val DIVISION = "division"
        val VILLAGE = "village"
        val MARK = "feature"
        val READ_ENGLISH = "english"
        val DATE_MOVED = "date_moved"
        val LANGS = "languages"
        val BRAC = "brac"
        val BRAC_CHP = "brac_chp"
        val EDUCATION = "education"
        val OCCUPATION = "occupation"
        val COMMUNITY = "community_membership"
        val ADDED_BY = "added_by"
        val COMMENT = "comment"
        val PROCEED = "proceed"
        val DATE_ADDED = "client_time"
        val SYNCED = "synced"
        val CHEW_NAME = "chew_name"
        val CHEW_NUMBER = "chew_number"
        val CHEW_ID = "chew_id"
        val WARD = "ward"
        val CU_NAME = "cu_name"
        val LINK_FACILITY = "link_facility"
        val HOUSEHOLDS = "households"
        val TRAININGS = "trainings"
        val CHV = "is_chv"
        val GOK_TRAINED = "is_gok_trained"
        val REFERRAL_NAME = "referral"
        val REFERRAL_NUMBER = "referral_number"
        val REFERRAL_TITLE = "referral_title"
        val VHT = "vht"
        val PARISH = "parish"
        //public static final String PARISH_ID = "parish_id";
        val ACCOUNTS = "financial_accounts"
        val REC_TRANSPORT = "recruitment_transport"
        val BRANCH_TRANPORT = "branch_transport"
        val MARITAL_STATUS = "marital_status"

        val columns = arrayOf(ID, NAME, PHONE, GENDER, DOB, DISTRICT, SUB_COUNTY, DIVISION, VILLAGE, MARK, READ_ENGLISH, DATE_MOVED, LANGS, BRAC, BRAC_CHP, EDUCATION, OCCUPATION, COMMUNITY, ADDED_BY, COMMENT, PROCEED, DATE_ADDED, SYNCED, RECRUITMENT, COUNTRY, CHEW_NAME, CHEW_NUMBER, WARD, CU_NAME, LINK_FACILITY, HOUSEHOLDS, TRAININGS, CHV, GOK_TRAINED, REFERRAL_NAME, REFERRAL_NUMBER, REFERRAL_TITLE, VHT, PARISH, ACCOUNTS, REC_TRANSPORT, BRANCH_TRANPORT, CHEW_ID, MARITAL_STATUS)

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + varchar_field + ", "
                + NAME + varchar_field + ", "
                + PHONE + varchar_field + ", "
                + GENDER + varchar_field + ", "
                + DOB + integer_field + ", "
                + DISTRICT + varchar_field + ", "
                + SUB_COUNTY + varchar_field + ", "
                + RECRUITMENT + varchar_field + ", "
                + COUNTRY + varchar_field + ", "
                + DIVISION + varchar_field + ", "
                + VILLAGE + varchar_field + ", "
                + MARK + text_field + ", "
                + READ_ENGLISH + integer_field + ", "
                + DATE_MOVED + integer_field + ", "
                + LANGS + varchar_field + ", "
                + BRAC + integer_field + ", "
                + BRAC_CHP + integer_field + ", "
                + EDUCATION + text_field + ", "
                + OCCUPATION + text_field + ", "
                + COMMUNITY + integer_field + ", "
                + ADDED_BY + integer_field + ", "
                + COMMENT + text_field + ", "
                + PROCEED + integer_field + ", "
                + DATE_ADDED + integer_field + ", "
                + CHEW_NAME + varchar_field + ", "
                + CHEW_NUMBER + varchar_field + ", "
                + WARD + varchar_field + ", "
                + CU_NAME + varchar_field + ", "
                + LINK_FACILITY + varchar_field + ", "
                + HOUSEHOLDS + integer_field + ", "
                + TRAININGS + text_field + ", "
                + CHV + integer_field + ", "
                + GOK_TRAINED + integer_field + ", "
                + REFERRAL_NAME + varchar_field + ", "
                + REFERRAL_TITLE + varchar_field + ", "
                + REFERRAL_NUMBER + varchar_field + ", "
                + VHT + integer_field + ", "
                + PARISH + varchar_field + ", "
                + ACCOUNTS + integer_field + ", "
                + REC_TRANSPORT + integer_field + ", "
                + BRANCH_TRANPORT + integer_field + ", "
                + CHEW_ID + varchar_field + ", "
                + MARITAL_STATUS + varchar_field + ", "
                + SYNCED + integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
        val DB_UPDATE_V2 = "ALTER TABLE " + TABLE_NAME +
                "  ADD " + CHEW_ID + varchar_field + ";"
    }
}
