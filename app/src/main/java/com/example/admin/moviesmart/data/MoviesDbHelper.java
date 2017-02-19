package com.example.admin.moviesmart.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 1/9/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_DETAILS_TABLE = "CREATE TABLE " + MoviesContract.MoviesData.TABLE_NAME +
                " (" + MoviesContract.MoviesData.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY ," +
                MoviesContract.MoviesData.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +
                MoviesContract.MoviesData.COLUMN_MOVIE_SUMMARY + " TEXT NOT NULL," +
                MoviesContract.MoviesData.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MoviesContract.MoviesData.COLUMN_MOVIE_RATING + " REAL" + ")";


        final String SQL_TRAILER_TABLE = "CREATE TABLE " + MoviesContract.MoviesData.TABLE_NAME_2 +
                "( " + MoviesContract.MoviesData.COLUMN_TRAILER_ID + " INTEGER,"
                + MoviesContract.MoviesData.COLUMN_TRAILER_VIDEO_ID + " TEXT ,"
                + MoviesContract.MoviesData.COLUMN_TRAILER_IMAGE_PATH + " TEXT ," +
                MoviesContract.MoviesData.COLUMN_TRAILER_NAME + " TEXT ," +
                "FOREIGN KEY(" + MoviesContract.MoviesData.COLUMN_TRAILER_ID
                + ")" + " REFERENCES " + MoviesContract.MoviesData.TABLE_NAME + "(" + MoviesContract.MoviesData.COLUMN_MOVIE_ID + ")" + "" +
                " ON DELETE CASCADE  ON UPDATE CASCADE  )";

        final String SQL_REVIEWS_TABLE = "CREATE TABLE " + MoviesContract.MoviesData.TABLE_NAME_3 + "( " +
                MoviesContract.MoviesData.COLUMN_Review_ID + " INTEGER ,"
                + MoviesContract.MoviesData.COLUMN_REVIEW_CONTENT_ID + " TEXT ,"
                + MoviesContract.MoviesData.COLUMN_REVIEW_NAME + " TEXT ," +
                MoviesContract.MoviesData.COLUMN_REVIEW_DETAIL + " TEXT ,"
                + "FOREIGN KEY(" + MoviesContract.MoviesData.COLUMN_Review_ID
                + ")" + " REFERENCES " + MoviesContract.MoviesData.TABLE_NAME + "(" + MoviesContract.MoviesData.COLUMN_MOVIE_ID + ")" + "" +
                " ON DELETE CASCADE  ON UPDATE CASCADE )";

        db.execSQL(SQL_CREATE_DETAILS_TABLE);
        db.execSQL(SQL_TRAILER_TABLE);
        db.execSQL(SQL_REVIEWS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + MoviesContract.MoviesData.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST" + MoviesContract.MoviesData.TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXIST" + MoviesContract.MoviesData.TABLE_NAME_3);

        onCreate(db);
    }
}
