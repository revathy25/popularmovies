package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;


import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }


    static ContentValues createPopularValues(long movieId) {
        ContentValues testValues = new ContentValues();
        testValues.put(PopularMoviesContract.PopularEntry.COLUMN_MOVIE_DB_MOVIE_ID, movieId);
        testValues.put(PopularMoviesContract.PopularEntry.COLUMN_POPULAR_ORDER, 1);
        return testValues;
    }


    static ContentValues createSampleMovieValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID, 118340L);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_TITLE , "Guardians of the Galaxy");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_RELEASE_DATE , "2014-07-30");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_POSTER_PATH ,"/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_OVERVIEW, "Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_POPULARITY , 10.63);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_VOTE_COUNT, 5089);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_VOTE_AVERAGE, 7.96);
        return testValues;
    }


}
