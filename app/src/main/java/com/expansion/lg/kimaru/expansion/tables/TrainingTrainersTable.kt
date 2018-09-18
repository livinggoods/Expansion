package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainer
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class TrainingTrainersTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {


    internal var columns = arrayOf(ID, TRAINING_ID, CLASS_ID, TRAINER_ID, COUNTRY, CLIENT_TIME, CREATED_BY, ARCHIVED, TRAINING_ROLE_ID)
    val trainers: List<TrainingTrainer>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val trainingTrainerList = ArrayList<TrainingTrainer>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {

                val trainingTrainer = TrainingTrainer()

                trainingTrainer.id = cursor.getInt(cursor.getColumnIndex(ID))
                trainingTrainer.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
                trainingTrainer.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                trainingTrainer.trainerId = cursor.getInt(cursor.getColumnIndex(TRAINER_ID))
                trainingTrainer.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                trainingTrainer.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                trainingTrainer.createdby = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
                trainingTrainer.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
                trainingTrainer.trainingRoleId = cursor.getInt(cursor.getColumnIndex(TRAINING_ROLE_ID))
                trainingTrainerList.add(trainingTrainer)
                cursor.moveToNext()
            }
            cursor.close()
            return trainingTrainerList
        }

    val trainingTrainerJson: JSONObject
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

    fun addData(trainingTrainer: TrainingTrainer): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, trainingTrainer.id)
        cv.put(TRAINING_ID, trainingTrainer.trainingId)
        cv.put(CLASS_ID, trainingTrainer.classId)
        cv.put(TRAINER_ID, trainingTrainer.trainerId)
        cv.put(COUNTRY, trainingTrainer.country)
        cv.put(CLIENT_TIME, trainingTrainer.clientTime)
        cv.put(CREATED_BY, trainingTrainer.createdby)
        cv.put(ARCHIVED, if (trainingTrainer.isArchived) 1 else 0)
        cv.put(TRAINING_ROLE_ID, trainingTrainer.trainingRoleId)

        val id: Long
        if (isExist(trainingTrainer)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + trainingTrainer.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()
        return id

    }

    fun getTrainingTrainersByTrainingId(trainingId: String): List<TrainingTrainer>? {
        val db = readableDatabase
        val whereClause = "$TRAINING_ID = ?"
        val whereArgs = arrayOf(trainingId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingTrainerList = ArrayList<TrainingTrainer>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val trainingTrainer = TrainingTrainer()
                trainingTrainer.id = cursor.getInt(cursor.getColumnIndex(ID))
                trainingTrainer.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
                trainingTrainer.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
                trainingTrainer.trainerId = cursor.getInt(cursor.getColumnIndex(TRAINER_ID))
                trainingTrainer.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                trainingTrainer.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                trainingTrainer.createdby = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
                trainingTrainer.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
                trainingTrainer.trainingRoleId = cursor.getInt(cursor.getColumnIndex(TRAINING_ROLE_ID))
                trainingTrainerList.add(trainingTrainer)
                cursor.moveToNext()
            }
            cursor.close()
            return trainingTrainerList
        }

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val trainingTrainer = TrainingTrainer()
            trainingTrainer.id = jsonObject.getInt(ID)
            trainingTrainer.trainingId = jsonObject.getString(TRAINER_ID)
            trainingTrainer.classId = jsonObject.getString(CLASS_ID)
            trainingTrainer.trainerId = jsonObject.getInt(TRAINER_ID)
            trainingTrainer.country = jsonObject.getString(COUNTRY)
            trainingTrainer.clientTime = jsonObject.getLong(CLIENT_TIME)
            trainingTrainer.createdby = jsonObject.getInt(CREATED_BY)
            trainingTrainer.isArchived = jsonObject.getInt(ARCHIVED) == 1
            trainingTrainer.trainingRoleId = jsonObject.getInt(TRAINING_ROLE_ID)
            this.addData(trainingTrainer)
        } catch (e: Exception) {
        }

    }

    fun getTrainingTrainerById(id: Int?): TrainingTrainer? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id!!.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingTrainer = TrainingTrainer()
            trainingTrainer.id = cursor.getInt(cursor.getColumnIndex(ID))
            trainingTrainer.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
            trainingTrainer.classId = cursor.getString(cursor.getColumnIndex(CLASS_ID))
            trainingTrainer.trainerId = cursor.getInt(cursor.getColumnIndex(TRAINER_ID))
            trainingTrainer.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            trainingTrainer.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
            trainingTrainer.createdby = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
            trainingTrainer.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
            trainingTrainer.trainingRoleId = cursor.getInt(cursor.getColumnIndex(TRAINING_ROLE_ID))
            return trainingTrainer
        }

    }

    fun isExist(trainingTrainer: TrainingTrainer): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + trainingTrainer.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "training_trainers"
        val JSON_ROOT = "training_trainers"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        val ID = "id"
        val TRAINING_ID = "training_id"
        val CLASS_ID = "class_id"
        val TRAINER_ID = "trainer_id"
        val COUNTRY = "country"
        val CLIENT_TIME = "client_time"
        val CREATED_BY = "created_by"
        val ARCHIVED = "archived"
        val TRAINING_ROLE_ID = "training_role_id"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + Constants.integer_field + ", "
                + TRAINING_ID + Constants.varchar_field + ", "
                + CLASS_ID + Constants.integer_field + ", "
                + TRAINER_ID + Constants.integer_field + ", "
                + COUNTRY + Constants.varchar_field + ", "
                + CLIENT_TIME + Constants.real_field + ", "
                + CREATED_BY + Constants.integer_field + ", "
                + ARCHIVED + Constants.integer_field + ", "
                + TRAINING_ROLE_ID + Constants.integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

