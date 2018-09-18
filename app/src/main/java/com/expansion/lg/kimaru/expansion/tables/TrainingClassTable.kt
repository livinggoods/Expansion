package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.TrainingClass
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

/**
 * Created by kimaru on 9/4/17.
 */


class TrainingClassTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    var columns = arrayOf(ID, TRAINING_ID, CLASS_NAME, CREATED_BY, CLIENT_TIME, ARCHIVED, COUNTRY)


    val trainingClasses: List<TrainingClass>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val trainingClasses = ArrayList<TrainingClass>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val trainingClass = TrainingClass()
                trainingClass.id = cursor.getInt(cursor.getColumnIndex(ID))
                trainingClass.className = cursor.getString(cursor.getColumnIndex(CLASS_NAME))
                trainingClass.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
                trainingClass.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
                trainingClass.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                trainingClass.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                trainingClass.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
                trainingClasses.add(trainingClass)
                cursor.moveToNext()
            }
            db.close()
            return trainingClasses
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

    fun addTrainingClass(trainingClass: TrainingClass): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, trainingClass.id)
        cv.put(CLASS_NAME, trainingClass.className)
        cv.put(TRAINING_ID, trainingClass.trainingId)
        cv.put(CREATED_BY, trainingClass.createdBy)
        cv.put(CLIENT_TIME, trainingClass.clientTime)
        cv.put(COUNTRY, trainingClass.country)
        cv.put(ARCHIVED, if (trainingClass.isArchived) 1 else 0)

        val id: Long
        if (isExist(trainingClass)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + trainingClass.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()
        return id

    }

    fun isExist(trainingClass: TrainingClass): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + trainingClass.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    fun getTrainingClassesByTraining(trainingId: String): List<TrainingClass>? {

        val db = readableDatabase
        val whereClause = "$TRAINING_ID = ?"
        val whereArgs = arrayOf(trainingId)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingClasses = ArrayList<TrainingClass>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val trainingClass = TrainingClass()
                trainingClass.id = cursor.getInt(cursor.getColumnIndex(ID))
                trainingClass.className = cursor.getString(cursor.getColumnIndex(CLASS_NAME))
                trainingClass.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
                trainingClass.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
                trainingClass.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                trainingClass.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                trainingClass.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
                trainingClasses.add(trainingClass)
                cursor.moveToNext()
            }
            db.close()
            return trainingClasses
        }

    }

    fun getTrainingTraineeById(id: String): TrainingClass? {

        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingClass = TrainingClass()
            trainingClass.id = cursor.getInt(cursor.getColumnIndex(ID))
            trainingClass.className = cursor.getString(cursor.getColumnIndex(CLASS_NAME))
            trainingClass.trainingId = cursor.getString(cursor.getColumnIndex(TRAINING_ID))
            trainingClass.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
            trainingClass.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
            trainingClass.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            trainingClass.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
            db.close()

            return trainingClass
        }

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val trainingClass = TrainingClass()
            trainingClass.id = jsonObject.getInt(ID)
            if (!jsonObject.isNull(CLASS_NAME)) {
                trainingClass.className = jsonObject.getString(CLASS_NAME)
            }
            if (!jsonObject.isNull(TRAINING_ID)) {
                trainingClass.trainingId = jsonObject.getString(TRAINING_ID)
            }
            if (!jsonObject.isNull(CREATED_BY)) {
                trainingClass.createdBy = jsonObject.getInt(CREATED_BY)
            }
            if (!jsonObject.isNull(CLIENT_TIME)) {
                trainingClass.clientTime = jsonObject.getLong(CLIENT_TIME)
            }
            if (!jsonObject.isNull(COUNTRY)) {
                trainingClass.country = jsonObject.getString(COUNTRY)
            }
            if (!jsonObject.isNull(ARCHIVED)) {
                trainingClass.isArchived = jsonObject.getInt(ARCHIVED) == 1
            }

            addTrainingClass(trainingClass)
        } catch (e: Exception) {
            Log.d("Tremap ERR", "Training class From Json : " + e.message)
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "training_class"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        var JSON_ROOT = "training_classes"


        val ID = "id"
        val TRAINING_ID = "training_id"
        val CLASS_NAME = "class_name"
        val CREATED_BY = "created_by"
        val CLIENT_TIME = "client_time"
        val ARCHIVED = "archived"
        val COUNTRY = "country"
        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + Constants.integer_field + ","
                + TRAINING_ID + Constants.varchar_field + ", "
                + CLASS_NAME + Constants.varchar_field + ", "
                + CREATED_BY + Constants.integer_field + ", "
                + CLIENT_TIME + Constants.real_field + ", "
                + ARCHIVED + Constants.integer_field + ", "
                + COUNTRY + Constants.varchar_field + ");")


        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}


