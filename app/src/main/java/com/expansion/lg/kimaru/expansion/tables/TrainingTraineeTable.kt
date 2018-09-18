package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainee
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 9/4/17.
 */


class TrainingTraineeTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    var columns = arrayOf(ID, REGISTRATION_ID, CLASS_ID, TRAINING_ID, COUNTRY, ADDED_BY, CLIENT_TIME, BRANCH, COHORT, CHP_CODE)


    val trainingTraineeData: List<TrainingTrainee>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val trainingTrainees = ArrayList<TrainingTrainee>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val trainingTrainee = TrainingTrainee()
                trainingTrainee.id = cursor.getString(cursor.getColumnIndex(ID))
                trainingTrainee.registrationId = cursor.getString(cursor.getColumnIndex(REGISTRATION_ID))
                trainingTrainee.classId = cursor.getInt(cursor.getColumnIndex(CLASS_ID))
                trainingTrainee.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
                trainingTrainee.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                trainingTrainee.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
                trainingTrainee.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                trainingTrainee.branch = cursor.getInt(cursor.getColumnIndex(BRANCH))
                trainingTrainee.chpCode = cursor.getString(cursor.getColumnIndex(CHP_CODE))
                trainingTrainees.add(trainingTrainee)
                cursor.moveToNext()
            }
            db.close()
            return trainingTrainees
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
        Log.w("referral", "upgrading database from" + oldVersion + "to" + newVersion)

        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addTrainingTraineer(trainingTrainee: TrainingTrainee): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, trainingTrainee.id)
        cv.put(REGISTRATION_ID, trainingTrainee.registrationId)
        cv.put(CLASS_ID, trainingTrainee.classId)
        cv.put(TRAINING_ID, trainingTrainee.trainingId)
        cv.put(COUNTRY, trainingTrainee.country)
        cv.put(ADDED_BY, trainingTrainee.addedBy)
        cv.put(CLIENT_TIME, trainingTrainee.clientTime)
        cv.put(BRANCH, trainingTrainee.branch)
        cv.put(COHORT, trainingTrainee.cohort)
        cv.put(CHP_CODE, trainingTrainee.chpCode)
        val id: Long
        if (isExist(trainingTrainee)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + trainingTrainee.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()
        return id

    }

    fun isExist(trainingTrainee: TrainingTrainee): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + trainingTrainee.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    fun getTrainingByTraining(trainingId: String): List<TrainingTrainee>? {

        val db = readableDatabase
        val whereClause = "$TRAINING_ID = ?"
        val whereArgs = arrayOf(trainingId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingList = ArrayList<TrainingTrainee>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val trainingTrainee = TrainingTrainee()
                trainingTrainee.id = cursor.getString(cursor.getColumnIndex(ID))
                trainingTrainee.registrationId = cursor.getString(cursor.getColumnIndex(REGISTRATION_ID))
                trainingTrainee.classId = cursor.getInt(cursor.getColumnIndex(CLASS_ID))
                trainingTrainee.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
                trainingTrainee.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                trainingTrainee.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
                trainingTrainee.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                trainingTrainee.branch = cursor.getInt(cursor.getColumnIndex(BRANCH))
                trainingTrainee.chpCode = cursor.getString(cursor.getColumnIndex(CHP_CODE))

                trainingList.add(trainingTrainee)
                cursor.moveToNext()
            }
            db.close()
            return trainingList
        }

    }

    fun getTrainingTraineeById(id: String): TrainingTrainee? {

        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingTrainee = TrainingTrainee()
            trainingTrainee.id = cursor.getString(cursor.getColumnIndex(ID))
            trainingTrainee.registrationId = cursor.getString(cursor.getColumnIndex(REGISTRATION_ID))
            trainingTrainee.classId = cursor.getInt(cursor.getColumnIndex(CLASS_ID))
            trainingTrainee.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
            trainingTrainee.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            trainingTrainee.addedBy = cursor.getInt(cursor.getColumnIndex(ADDED_BY))
            trainingTrainee.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
            trainingTrainee.branch = cursor.getInt(cursor.getColumnIndex(BRANCH))
            trainingTrainee.chpCode = cursor.getString(cursor.getColumnIndex(CHP_CODE))

            db.close()

            /**
             * @TODO: Add all registrations referred by this user.
             * When one retrieves a referral, it would be good to get all the registrations that (s)he
             * has referred
             */
            return trainingTrainee
        }

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val trainingTrainee = TrainingTrainee()
            trainingTrainee.id = jsonObject.getString(ID)
            if (!jsonObject.isNull(REGISTRATION_ID)) {
                trainingTrainee.registrationId = jsonObject.getString(REGISTRATION_ID)
            }
            if (!jsonObject.isNull(CLASS_ID)) {
                trainingTrainee.classId = jsonObject.getInt(CLASS_ID)
            }
            if (!jsonObject.isNull(TRAINING_ID)) {
                trainingTrainee.trainingId = jsonObject.getString(TRAINING_ID)
            }
            if (!jsonObject.isNull(COUNTRY)) {
                trainingTrainee.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(ADDED_BY)) {
                trainingTrainee.addedBy = jsonObject.getInt(ADDED_BY)
            }
            if (!jsonObject.isNull(CLIENT_TIME)) {
                trainingTrainee.clientTime = jsonObject.getLong(CLIENT_TIME)
            }
            if (!jsonObject.isNull(BRANCH)) {
                trainingTrainee.branch = jsonObject.getInt(BRANCH)
            }
            if (!jsonObject.isNull(COHORT)) {
                trainingTrainee.cohort = jsonObject.getInt(COHORT)
            }
            if (!jsonObject.isNull(CHP_CODE)) {
                trainingTrainee.chpCode = jsonObject.getString(CHP_CODE)
            }
            addTrainingTraineer(trainingTrainee)
        } catch (e: Exception) {
            Log.d("Tremap Trainee ERR", "From Json : " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "training_trainees"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var JSON_ROOT = "training_trainees"


        val ID = "id"
        val REGISTRATION_ID = "registration_id"
        val CLASS_ID = "class_id"
        val TRAINING_ID = "training_id"
        val COUNTRY = "country"
        val ADDED_BY = "added_by"
        val CLIENT_TIME = "client_time"
        val BRANCH = "branch"
        val COHORT = "cohort"
        val CHP_CODE = "chp_code"
        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + Constants.varchar_field + ","
                + REGISTRATION_ID + Constants.varchar_field + ", "
                + CLASS_ID + Constants.integer_field + ", "
                + TRAINING_ID + Constants.varchar_field + ", "
                + COUNTRY + Constants.varchar_field + ", "
                + ADDED_BY + Constants.integer_field + ", "
                + CLIENT_TIME + Constants.real_field + ", "
                + BRANCH + Constants.varchar_field + ", "
                + COHORT + Constants.integer_field + ", "
                + CHP_CODE + Constants.varchar_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}


