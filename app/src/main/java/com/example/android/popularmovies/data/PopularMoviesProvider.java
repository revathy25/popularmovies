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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;

public class PopularMoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PopularMoviesDBHelper mOpenHelper;
    private static final String ACSENDING_ORDER = " ASC";

    private static final int MOVIE = 1;
    private static final int POPULAR = 2;
    private static final int MOVIE_BY_MOVIE_ID = 3;

    private static final SQLiteQueryBuilder sMovieByPopularSettingQueryBuilder;

    static{
        sMovieByPopularSettingQueryBuilder = new SQLiteQueryBuilder();

        sMovieByPopularSettingQueryBuilder.setTables(
                PopularMoviesContract.PopularEntry.TABLE_NAME + " INNER JOIN " +
                        PopularMoviesContract.MovieEntry.TABLE_NAME +
                        " ON " +PopularMoviesContract.PopularEntry.TABLE_NAME +
                        "." + PopularMoviesContract.PopularEntry.COLUMN_MOVIE_DB_MOVIE_ID +
                        " = " + PopularMoviesContract.MovieEntry.TABLE_NAME +
                        "." + PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID);
    }

    private static final String sMovieByIdSelection =
            PopularMoviesContract.MovieEntry.TABLE_NAME+
                    "." + PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID + " = ? ";

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIE+ "/#", MOVIE_BY_MOVIE_ID);
        //Adding Movie as base path
        matcher.addURI(authority, PopularMoviesContract.PATH_POPULAR, POPULAR);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return PopularMoviesContract.MovieEntry.CONTENT_TYPE;
            case POPULAR:
                return PopularMoviesContract.PopularEntry.CONTENT_TYPE;
            case MOVIE_BY_MOVIE_ID:
                return PopularMoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {

        Cursor returnCursor = null;
        switch (sUriMatcher.match(uri)) {
            case POPULAR:
                returnCursor = getMoviesSortedByPopularity();
                break;
            case MOVIE_BY_MOVIE_ID:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.MovieEntry.TABLE_NAME,
                        null,
                        sMovieByIdSelection,
                        new String[]{PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri)},
                        null,
                        null,
                        null
                );
                break;
            default:
                // If the URI is not recognized, you should do some error handling here.
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long movieDB_ID = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
                if ( movieDB_ID > 0 )
                    returnUri = PopularMoviesContract.MovieEntry.buildMovieUri(movieDB_ID);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case POPULAR: {
                long _id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = PopularMoviesContract.PopularEntry.buildPopularUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int update(Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        //we do not need update for now since plannning to delete all records and reinsert it.
        return 0;
    }

    @Override
    public int delete(Uri uri,
                      String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        if ( null == selection ) selection = "1";
        int numRowsDeleted = 0;
        switch (match) {
            case MOVIE: {
                numRowsDeleted = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case POPULAR: {
                numRowsDeleted = db.delete(PopularMoviesContract.PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIE_BY_MOVIE_ID: {
                String Movie_ID = PopularMoviesContract.MovieEntry.getMovieIdFromUri(uri);
                selectionArgs = new String[]{Movie_ID};
                numRowsDeleted = db.delete(PopularMoviesContract.MovieEntry.TABLE_NAME, sMovieByIdSelection, selectionArgs );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numRowsDeleted;
    }

    private Cursor getMoviesSortedByPopularity() {

        String sortOrder = PopularMoviesContract.PopularEntry.COLUMN_POPULAR_ORDER+ACSENDING_ORDER;

        String projection[] = new String[]{ PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_MOVIE_ID
                                            ,PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_TITLE
                                            ,PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_POSTER_PATH
                                            ,PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_OVERVIEW
                                            ,PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_RELEASE_DATE
                                            ,PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_POPULARITY
                                            ,PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_VOTE_COUNT
                                            ,PopularMoviesContract.MovieEntry.COLUMN_MOVIE_DB_VOTE_AVERAGE
                                            ,PopularMoviesContract.PopularEntry.COLUMN_POPULAR_ORDER
        };

        return sMovieByPopularSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}