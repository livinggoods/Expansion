package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class UserTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="user";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;

    //String email, username, password, name;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "_id";
    public static final String EMAIL= "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    String [] columns=new String[]{ID, EMAIL, USERNAME, PASSWORD, NAME, COUNTRY};
    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + primary_field + ", "
            + EMAIL + varchar_field + ", "
            + USERNAME + varchar_field + ", "
            + PASSWORD + varchar_field + ", "
            + NAME + varchar_field + ", "
            + COUNTRY + varchar_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public UserTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion);
        db.execSQL(DATABASE_DROP);
    }

    public long addUser(User user) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, user.getId());
        cv.put(EMAIL, user.getEmail());
        cv.put(USERNAME, user.getUsername());
        cv.put(PASSWORD, user.getPassword());
        cv.put(NAME, user.getName());
        cv.put(COUNTRY, user.getCountry());

        // long id=db.insert(TABLE_NAME,null,cv);

        long id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return id;

    }
    public List<User> getUserData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<User> userList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            User user=new User();

            user.setId(cursor.getInt(0));
            user.setEmail(cursor.getString(1));
            user.setUsername(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setName(cursor.getString(4));
            user.setCountry(cursor.getString(5));

            userList.add(user);
        }
        db.close();
        return userList;
    }

    public User getUserById (int id){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setEmail(cursor.getString(1));
        user.setUsername(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        user.setName(cursor.getString(4));
        user.setCountry(cursor.getString(5));
        return user;
    }

    public Cursor getUserCursor() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }


    public Cursor fetchUser (String username, String password){
        SQLiteDatabase db = getReadableDatabase();
        Cursor myCursor = db.query(TABLE_NAME, columns, EMAIL + "='" + username +
                "' AND " + PASSWORD + " = '" + password + "'", null, null, null, null);
        if (myCursor != null){
            myCursor.moveToFirst();
        }
        return myCursor;
    }

    public JSONObject getUsersJson() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        JSONObject results = new JSONObject();

        JSONArray resultSet = new JSONArray();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            int totalColumns = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i =0; i < totalColumns; i++){
                if (cursor.getColumnName(i) != null){
                    try {
                        if (cursor.getString(i) != null){
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        }else{
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    }catch (Exception e){
                    }
                }
            }
            resultSet.put(rowObject);
            try {
                results.put("users", resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        db.close();
        return results;
    }
}

