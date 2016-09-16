package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.PopularMoviesContract.MovieEntry;
/*
import com.example.android.popularmovies.data.PopularMoviesContract.PopularEntry;
import com.example.android.popularmovies.data.PopularMoviesContract.TopRatedEntry;
*/

/**
 * Created by rgunasek on 9/13/2016.
 */
public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "popularmovies.db";

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID+ " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOVIE_DB_TITLE + " TEXT NOT NULL " +
                MovieEntry.COLUMN_MOVIE_DB_POSTER_PATH + " TEXT UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_DB_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_DB_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULAR_ORDER + " INTEGER UNIQUE , " +
                MovieEntry.COLUMN_TOP_RATED_ORDER + " INTEGER UNIQUE , " +
                MovieEntry.COLUMN_MOVIE_DB_POPULARITY + " REAL , " +
                MovieEntry.COLUMN_MOVIE_DB_VOTE_COUNT + " INTEGER , " +
                MovieEntry.COLUMN_MOVIE_DB_VOTE_AVERAGE + " REAL " +
                " );";

/*
        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " + PopularEntry.TABLE_NAME + " (" +

                PopularEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // foriegn key for movie id
                PopularEntry.COLUMN_MOVIE_DB_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                PopularEntry.COLUMN_POPULAR_ORDER + " INTEGER UNIQUE NOT NULL, " +
                // Set up  foreign key relationship to movie table
                " FOREIGN KEY (" + PopularEntry.COLUMN_MOVIE_DB_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID + ") " +
                " );";

        final String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE " + TopRatedEntry.TABLE_NAME + " (" +

                TopRatedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // foriegn key for movie id
                TopRatedEntry.COLUMN_MOVIE_DB_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                TopRatedEntry.COLUMN_TOP_RATED_ORDER + " INTEGER UNIQUE NOT NULL, " +
                // Set up  foreign key relationship to movie table
                " FOREIGN KEY (" + TopRatedEntry.COLUMN_MOVIE_DB_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID + ") " +
                " );";
*/
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        /*
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /*
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TopRatedEntry.TABLE_NAME);
        */
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
