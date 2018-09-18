package com.expansion.lg.kimaru.expansion.tables

/**
 * Created by kimaru on 8/18/17.
 */

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent
import com.expansion.lg.kimaru.expansion.other.Constants


import java.util.ArrayList


class IccmComponentTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    internal var columns = arrayOf(ID, COMPONENTNAME, ADDED_BY, COMMENT, CLIENTTIME, DATEADDED, ARCHIVED, STATUS)

    val iccmComponentData: List<IccmComponent>
        get() {
            val db = readableDatabase
            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)
            val componentsList = ArrayList<IccmComponent>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val component = IccmComponent()

                component.id = cursor.getInt(cursor.getColumnIndex(ID))
                component.componentName = cursor.getString(cursor.getColumnIndex(COMPONENTNAME))
                component.addedBy = cursor.getLong(cursor.getColumnIndex(ADDED_BY))
                component.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
                component.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENTTIME))
                component.dateAdded = cursor.getString(cursor.getColumnIndex(DATEADDED))
                component.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
                component.status = cursor.getString(cursor.getColumnIndex(STATUS))
                componentsList.add(component)
                cursor.moveToNext()
            }
            db.close()
            return componentsList
        }

    val iccmJson: JSONObject
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

        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion)
        if (oldVersion < 2) {
            upgradeVersion2(db)
        }
    }

    fun addData(component: IccmComponent): Long {

        val db = writableDatabase

        val cv = ContentValues()
        cv.put(ID, component.id)
        cv.put(COMPONENTNAME, component.componentName)
        cv.put(ADDED_BY, component.addedBy)
        cv.put(COMMENT, component.comment)
        cv.put(CLIENTTIME, component.clientTime)
        cv.put(DATEADDED, component.dateAdded)
        cv.put(ARCHIVED, if (component.isArchived) 1 else 0)
        cv.put(STATUS, component.status)

        val id: Long
        if (isExist(component)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + component.id + "'", null).toLong()
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }

        db.close()
        return id

    }

    fun isExist(component: IccmComponent): Boolean {
        val db = readableDatabase
        val cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + component.id + "'", null)
        val exist = cur.count > 0
        cur.close()
        return exist

    }


    fun getIccmComponentById(id: Int?): IccmComponent? {
        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)

        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val component = IccmComponent()
            component.id = cursor.getInt(cursor.getColumnIndex(ID))
            component.componentName = cursor.getString(cursor.getColumnIndex(COMPONENTNAME))
            component.addedBy = cursor.getLong(cursor.getColumnIndex(ADDED_BY))
            component.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
            component.clientTime = cursor.getLong(cursor.getColumnIndex(CLIENTTIME))
            component.dateAdded = cursor.getString(cursor.getColumnIndex(DATEADDED))
            component.isArchived = cursor.getInt(cursor.getColumnIndex(ARCHIVED)) == 1
            component.status = cursor.getString(cursor.getColumnIndex(STATUS))
            return component
        }
    }

    //getInterviewById

    fun fromJson(jsonObject: JSONObject) {
        val component = IccmComponent()
        try {
            component.id = jsonObject.getInt(ID)
            component.componentName = jsonObject.getString(COMPONENTNAME)
            component.addedBy = jsonObject.getLong(ADDED_BY)
            component.comment = jsonObject.getString(COMMENT)
            component.clientTime = jsonObject.getLong(CLIENTTIME)
            component.dateAdded = jsonObject.getString(DATEADDED)
            component.isArchived = jsonObject.getInt(ARCHIVED) == 1
            component.status = jsonObject.getString(STATUS)
            this.addData(component)
        } catch (e: Exception) {
        }

    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "iccm_components"
        val JSON_ROOT = "components"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        var varchar_field = " varchar(512) "
        var primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "
        var real_field = " REAL "


        //applicant, recruitment, motivation, community, mentality, selling, health, investment,
        // interpersonal, commitment, total, selected, addedBy,
        // dateAdded, synced
        val ID = "id"
        val COMPONENTNAME = "component_name"
        val ADDED_BY = "added_by"
        val COMMENT = "comment"
        val CLIENTTIME = "client_time"
        val DATEADDED = "date_added"
        val ARCHIVED = "archived"
        val STATUS = "status"

        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + integer_field + ", "
                + COMPONENTNAME + varchar_field + ", "
                + ADDED_BY + integer_field + ", "
                + COMMENT + text_field + ", "
                + CLIENTTIME + real_field + ", "
                + DATEADDED + varchar_field + ", "
                + ARCHIVED + integer_field + ", "
                + STATUS + varchar_field + ") ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

