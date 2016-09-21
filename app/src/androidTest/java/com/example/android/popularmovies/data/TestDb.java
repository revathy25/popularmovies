/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(PopularMoviesDBHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }


    public void testCreateDb() throws Throwable {

        //assertEquals("In testCreateDb ******", true , false);

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(PopularMoviesContract.PopularEntry.TABLE_NAME);
        tableNameHashSet.add(PopularMoviesContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(PopularMoviesDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new PopularMoviesDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals("DB is open",true, db.isOpen());
        Log.i(LOG_TAG,"DB is open:"+db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        Log.v(LOG_TAG,"Select Table Cursor"+c);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            String column = c.getString(0);
            boolean columnRemoved =  tableNameHashSet.remove(column);
           // assertEquals("Table Found:"+column, true , false);
            Log.i(LOG_TAG, "Table Found:"+column);
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the Popular entry and Movie entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        Cursor c2 = db.rawQuery("PRAGMA table_info(" + PopularMoviesContract.PopularEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c2.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> popularColumnHashSet = new HashSet<String>();
        popularColumnHashSet.add(PopularMoviesContract.PopularEntry._ID);
        popularColumnHashSet.add(PopularMoviesContract.PopularEntry.COLUMN_MOVIE_DB_MOVIE_ID);
        popularColumnHashSet.add(PopularMoviesContract.PopularEntry.COLUMN_POPULAR_ORDER);

        int columnNameIndex = c2.getColumnIndex("name");
        do {
            String columnName = c2.getString(columnNameIndex);
            Log.i(LOG_TAG,"columnName="+columnName);
            popularColumnHashSet.remove(columnName);
        } while(c2.moveToNext());

       assertTrue("Error: The database doesn't contain all of the required popular entry columns:"+popularColumnHashSet.size(),
                popularColumnHashSet.isEmpty());


        db.close();
    }

    public void testMovieTable() {
        insertMovie();
    }


    public void testPopularTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        long movieRowId = insertMovie();

        // Make sure we have a valid row ID.
        assertFalse("Error: Movie Not Inserted Correctly", movieRowId == -1L);

        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues popularValues = TestUtilities.createPopularValues(movieRowId);

        long popularRowId = db.insert(PopularMoviesContract.PopularEntry.TABLE_NAME, null, popularValues);
        assertTrue(popularRowId != -1);

        Cursor popularCursor = db.query(
                PopularMoviesContract.PopularEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from popular query", popularCursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("testInsertReadDb popularEntry failed to validate",
                popularCursor, popularValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from popular query",
                popularCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        popularCursor.close();
        dbHelper.close();
    }


    public long insertMovie() {
        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createSampleMovieValues();

        long movieRowId;
        movieRowId = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: movieRowId is -1", movieRowId != -1);

        Cursor cursor = db.query(
                PopularMoviesContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from Movie query", cursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",
                cursor, testValues);

        assertFalse( "Error: More than one record returned from Movie query",
                cursor.moveToNext() );

        cursor.close();
        db.close();
        return (Long)testValues.get(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID);
    }

}
