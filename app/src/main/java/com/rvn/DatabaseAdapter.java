package com.rvn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "highscore_db.db";

    public static final String HIGHSCORE_COL_ID = "id";
    public static final String HIGHSCORE_COL_NAME = "name";
    public static final String HIGHSCORE_COL_SCORE = "score";

    public static final String HIGHSCORE_TABLE_NAME = "Metier";

    public static final String HIGHSCORE_TABLE_CREATE =
            "CREATE TABLE " + HIGHSCORE_TABLE_NAME + " (" +
                    HIGHSCORE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    HIGHSCORE_COL_NAME + " TEXT, " +
                    HIGHSCORE_COL_SCORE + " INTEGER);";

    private SQLiteDatabase mDB;
    private MyOpenHelper mOpenHelper;


    public DatabaseAdapter(Context context) {
        mOpenHelper = new MyOpenHelper(context, DB_NAME,null, DB_VERSION);
    }
    public void open() {mDB = mOpenHelper.getWritableDatabase();}
    public void close() {mDB.close();}

    private class MyOpenHelper extends SQLiteOpenHelper {
        public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
            super(context, name, cursorFactory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) { db.execSQL(HIGHSCORE_TABLE_CREATE); }
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion) {
            db.execSQL("drop table " + HIGHSCORE_TABLE_NAME + ";");
            onCreate(db);
        }
    }


    public List<Highscore> getBestHighscore() { return getBestHighscore(5); }
    public List<Highscore> getBestHighscore(int nb){
        List<Highscore> Highscores = new ArrayList<Highscore>();
        Cursor c =  mDB.query(HIGHSCORE_TABLE_NAME,
                new String[] {HIGHSCORE_COL_ID, HIGHSCORE_COL_NAME, HIGHSCORE_COL_SCORE},
                null,
                null,
                null,
                null,
                HIGHSCORE_COL_SCORE+" desc", Integer.toString(nb) );
        c.moveToFirst();
        while(!c.isAfterLast()){
            Highscores.add( new Highscore(
                            c.getString(0),
                            c.getInt(1),
                            c.getInt(2)
                    )
            );
            c.moveToNext();
        }
        return Highscores;
    }

    public void addHighscore(Highscore hs){
        ContentValues values = new ContentValues();
        values.put(HIGHSCORE_COL_NAME, hs.getName());
        values.put(HIGHSCORE_COL_SCORE, hs.getScore());
        mDB.insert(HIGHSCORE_TABLE_NAME, null, values);
    }
    public int removeClient(int id) {
        return mDB.delete(HIGHSCORE_TABLE_NAME, HIGHSCORE_COL_ID + " = " + id, null);
    }
}

