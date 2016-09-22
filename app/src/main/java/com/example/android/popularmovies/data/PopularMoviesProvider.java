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
    private static final int POPULAR = 1;

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

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PopularMoviesContract.PATH_POPULAR, POPULAR);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int update(Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri,
                      String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case POPULAR:
                //TODO
            default:
              // If the URI is not recognized, you should do some error handling here.
        }

        return null;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}