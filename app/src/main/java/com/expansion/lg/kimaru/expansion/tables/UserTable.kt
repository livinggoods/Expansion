package com.expansion.lg.kimaru.expansion.tables

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.expansion.lg.kimaru.expansion.mzigos.User
import com.expansion.lg.kimaru.expansion.other.Constants
import com.expansion.lg.kimaru.expansion.tables.UserTable.Companion.TABLE_NAME

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList


/**
 * Created by kimaru on 2/28/17.
 */


class UserTable(context: Context) : SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {
    internal var columns = arrayOf(ID, EMAIL, USERNAME, PASSWORD, NAME, COUNTRY)
    val userData: List<User>
        get() {

            val db = readableDatabase

            val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null)

            val userList = ArrayList<User>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val user = User()

                user.id = cursor.getInt(0)
                user.email = cursor.getString(1)
                user.username = cursor.getString(2)
                user.password = cursor.getString(3)
                user.name = cursor.getString(4)
                user.country = cursor.getString(5)

                userList.add(user)
                cursor.moveToNext()
            }
            db.close()
            return userList
        }

    val userCursor: Cursor
        get() {

            val db = readableDatabase

            return db.query(TABLE_NAME, columns, null, null, null, null, null, null)
        }

    val usersJson: JSONObject
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

    fun addUser(user: User): Long {

        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ID, user.id)
        cv.put(EMAIL, user.email)
        cv.put(USERNAME, user.username)
        cv.put(PASSWORD, user.password)
        cv.put(NAME, user.name)
        cv.put(COUNTRY, user.country)

        // long id=db.insert(TABLE_NAME,null,cv);

        val id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)

        db.close()
        return id

    }

    fun getUserById(id: Int): User? {

        val db = readableDatabase
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null)
        if (!cursor.moveToFirst() || cursor.count == 0) {
            return null
        } else {
            val user = User()
            user.id = cursor.getInt(0)
            user.email = cursor.getString(1)
            user.username = cursor.getString(2)
            user.password = cursor.getString(3)
            user.name = cursor.getString(4)
            user.country = cursor.getString(5)
            db.close()
            return user
        }

    }


    fun fetchUser(username: String, password: String): Cursor? {
        val db = readableDatabase
        val myCursor = db.query(TABLE_NAME, columns, EMAIL + "='" + username +
                "' AND " + PASSWORD + " = '" + password + "'", null, null, null, null)
        myCursor?.moveToFirst()
        return myCursor
    }

    private fun upgradeVersion2(db: SQLiteDatabase) {}

    companion object {

        val TABLE_NAME = "user"
        val DATABASE_NAME = Constants.DATABASE_NAME
        val DATABASE_VERSION = Constants.DATABASE_VERSION

        //String email, username, password, name;

        var varchar_field = " varchar(512) "
        var primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "
        val JSON_ROOT = "users"

        val ID = "_id"
        val EMAIL = "email"
        val USERNAME = "username"
        val PASSWORD = "password"
        val NAME = "name"
        val COUNTRY = "country"
        val CREATE_DATABASE = ("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL + varchar_field + ", "
                + USERNAME + varchar_field + ", "
                + PASSWORD + varchar_field + ", "
                + NAME + varchar_field + ", "
                + COUNTRY + varchar_field + "); ")

        val DATABASE_DROP = "DROP TABLE IF EXISTS$TABLE_NAME"
    }
}

