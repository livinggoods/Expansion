package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Interview;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class InterviewTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="interview";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";


    //applicant, recruitment, motivation, community, mentality, selling, health, investment,
    // interpersonal, commitment, total, selected, addedBy,
    // dateAdded, synced
    public static final String ID = "id";
    public static final String APPLICANT= "applicant";
    public static final String RECRUITMENT = "recruitment";
    public static final String MOTIVATION = "motivation";
    public static final String COMMUNITY = "community";
    public static final String MENTALITY = "mentality";
    public static final String SELLING = "selling";
    public static final String HEALTH = "health";
    public static final String INVESTMENT = "investment";
    public static final String INTERPERSONAL = "interpersonal";
    public static final String COMMITMENT = "commitment";
    public static final String TOTAL = "total";
    public static final String SELECTED = "selected";
    public static final String SYNCED = "synced";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String DATE_ADDED = "date_added";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + primary_field + ", "
            + APPLICANT + integer_field + ", "
            + RECRUITMENT + integer_field + ", "
            + MOTIVATION + integer_field + ", "
            + COMMUNITY + integer_field + ", "
            + MENTALITY + integer_field + ", "
            + SELLING + integer_field + ", "
            + HEALTH + integer_field + ", "
            + INVESTMENT + integer_field + ", "
            + INTERPERSONAL + integer_field + ", "
            + COMMITMENT + integer_field + ", "
            + TOTAL + integer_field + ", "
            + SELECTED + integer_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + DATE_ADDED + integer_field + ", "
            + SYNCED + integer_field + ", "
            + ")";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public InterviewTable(Context context) {
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

    public long addData(Interview interview) {

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(APPLICANT, interview.getApplicant());
        cv.put(RECRUITMENT, interview.getRecruitment());
        cv.put(MOTIVATION, interview.getMotivation());
        cv.put(COMMUNITY, interview.getCommunity());
        cv.put(MENTALITY, interview.getMentality());
        cv.put(SELLING, interview.getSelling());
        cv.put(HEALTH, interview.getHealth());
        cv.put(INVESTMENT, interview.getInvestment());
        cv.put(INTERPERSONAL, interview.getInterpersonal());
        cv.put(TOTAL, interview.getTotal());
        cv.put(SELECTED, interview.getSelected());
        cv.put(ADDED_BY, interview.getAddedBy());
        cv.put(COMMENT, interview.getComment());
        cv.put(COMMITMENT, interview.getCommitment());
        cv.put(DATE_ADDED, interview.getDateAdded());
        cv.put(SYNCED, interview.getSynced());

        long id=db.insert(TABLE_NAME,null,cv);

        db.close();
        return id;

    }

    public List<Interview> getInterviewData() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, APPLICANT, RECRUITMENT, MOTIVATION, COMMUNITY,MENTALITY,
                SELLING, HEALTH, INVESTMENT, INTERPERSONAL, TOTAL, SELECTED, ADDED_BY, COMMENT,
                COMMITMENT, DATE_ADDED, SYNCED};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Interview> interviewList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Interview interview = new Interview();

            interview.setId(cursor.getInt(0));
            interview.setApplicant(cursor.getInt(1));
            interview.setRecruitment(cursor.getInt(2));
            interview.setMotivation(cursor.getInt(3));
            interview.setCommunity(cursor.getInt(4));
            interview.setMentality(cursor.getInt(5));
            interview.setSelling(cursor.getInt(6));
            interview.setHealth(cursor.getInt(7));
            interview.setInvestment(cursor.getInt(8));
            interview.setInterpersonal(cursor.getInt(9));
            interview.setTotal(cursor.getInt(10));
            interview.setSelected(cursor.getInt(11));
            interview.setAddedBy(cursor.getInt(12));
            interview.setComment(cursor.getString(13));
            interview.setCommitment(cursor.getInt(14));
            interview.setDateAdded(cursor.getInt(15));
            interview.setSynced(cursor.getInt(16));

            interviewList.add(interview);
        }

        db.close();

        return interviewList;
    }
    public Cursor getInterviewDataCursor() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, APPLICANT, RECRUITMENT, MOTIVATION, COMMUNITY,MENTALITY,
                SELLING, HEALTH, INVESTMENT, INTERPERSONAL, TOTAL, SELECTED, ADDED_BY, COMMENT,
                COMMITMENT, DATE_ADDED, SYNCED};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        return cursor;
    }
}

