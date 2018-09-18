package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.TrainingRole
import com.expansion.lg.kimaru.expansion.other.Constants

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class TrainingRolesTable(internal var context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {


    internal var columns = arrayOf(ID, ROLE_NAME, ARCHIVED, READONLY, COUNTRY, CLIENT_TIME, CREATED_BY)
    val trainingRoles: List<TrainingRole>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val trainingRoles = ArrayList<TrainingRole>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val trainingRole = TrainingRole()
                trainingRole.id = cursor.getInt(cursor.getColumnIndex(ID))
                trainingRole.roleName = cursor.getString(cursor.getColumnIndex(ROLE_NAME))
                trainingRole.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
                trainingRole.isReadOnly = cursor.getInt(cursor.getColumnIndex(READONLY)) == 1
                trainingRole.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                trainingRole.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
                trainingRole.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
                trainingRoles.add(trainingRole)
                cursor.moveToNext()
            }
            cursor.close()
            return trainingRoles
        }

    val trainingRoleJson: JSONObject
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

    fun addData(trainingRole: TrainingRole): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, trainingRole.id)
        cv.put(ROLE_NAME, trainingRole.roleName)
        cv.put(ARCHIVED, if (trainingRole.isArchived) 1 else 0)
        cv.put(READONLY, if (trainingRole.isReadOnly) 1 else 0)
        cv.put(COUNTRY, trainingRole.country)
        cv.put(CLIENT_TIME, trainingRole.clientTime)
        cv.put(CREATED_BY, trainingRole.createdBy)

        val id: Long
        if (isExist(trainingRole)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + trainingRole.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        db.close()
        return id

    }

    fun fromJson(jsonObject: JSONObject) {
        try {
            val trainingRole = TrainingRole()
            trainingRole.id = jsonObject.getInt(ID)
            trainingRole.roleName = jsonObject.getString(ROLE_NAME)
            trainingRole.isArchived = jsonObject.getInt(ARCHIVED) == 1
            trainingRole.isReadOnly = jsonObject.getInt(READONLY) == 1
            trainingRole.country = jsonObject.getString(COUNTRY)
            trainingRole.clientTime = jsonObject.getLong(CLIENT_TIME)
            trainingRole.createdBy = jsonObject.getInt(CREATED_BY)
            this.addData(trainingRole)
        } catch (e: Exception) {
        }

    }

    fun getTrainingRoleById(id: Int?): TrainingRole? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id!!.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val trainingRole = TrainingRole()
            trainingRole.id = cursor.getInt(cursor.getColumnIndex(ID))
            trainingRole.roleName = cursor.getString(cursor.getColumnIndex(ROLE_NAME))
            trainingRole.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
            trainingRole.isReadOnly = cursor.getInt(cursor.getColumnIndex(READONLY)) == 1
            trainingRole.country = cursor.getString(cursor.getColumnIndex(COUNTRY))
            trainingRole.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENT_TIME))
            trainingRole.createdBy = cursor.getInt(cursor.getColumnIndex(CREATED_BY))
            return trainingRole
        }

    }

    fun isExist(trainingRole: TrainingRole): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + trainingRole.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "training_roles"
        val JSON_ROOT = "training_roles"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION


        val ID = "id"
        val ROLE_NAME = "role_name"
        val ARCHIVED = "archived"
        val READONLY = "readonly"
        val COUNTRY = "country"
        val CLIENT_TIME = "client_time"
        val CREATED_BY = "created_by"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + Constants.integer_field + ", "
                + ROLE_NAME + Constants.varchar_field + ", "
                + ARCHIVED + Constants.varchar_field + ", "
                + READONLY + Constants.varchar_field + ", "
                + COUNTRY + Constants.real_field + ", "
                + CLIENT_TIME + Constants.real_field + ", "
                + CREATED_BY + Constants.integer_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

