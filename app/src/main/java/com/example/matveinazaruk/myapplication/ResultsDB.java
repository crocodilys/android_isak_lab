package com.example.matveinazaruk.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by crocodilys on 11.5.15.
 */
public class ResultsDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "resultsdb";
    private static final String TABLE_RESULTS = "results";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_RESULT = "result";

    public ResultsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESULTS_TABLE = "CREATE TABLE " + TABLE_RESULTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_RESULT + " INTEGER" + ")";
        db.execSQL(CREATE_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        onCreate(db);
    }

    public void addResult(String name, int result) {
        SQLiteDatabase db = this.getWritableDatabase();

        int id;
        int oldResult;
        Cursor cursor = db.query(TABLE_RESULTS, new String[]{KEY_ID, KEY_RESULT},
                KEY_NAME + "=?", new String[]{name}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            id = cursor.getInt(0);
            oldResult = cursor.getInt(1);
            ContentValues values = new ContentValues();
            values.put(KEY_RESULT, result + oldResult);
            db.update(TABLE_RESULTS, values, KEY_ID + "=?", new String[] {String.valueOf(id)});
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_RESULT, result);
            values.put(KEY_NAME, name);
            db.insert(TABLE_RESULTS, null, values);
        }
    }

    public Cursor getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RESULTS, new String[] {KEY_ID, KEY_NAME, KEY_RESULT}, null, null, null, null, null);
    }
}
