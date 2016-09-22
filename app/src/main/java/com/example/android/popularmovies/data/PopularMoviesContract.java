package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rgunasek on 9/13/2016.
 */
public class PopularMoviesContract  {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    /*
    public static final String PATH_TOP_RATED = "top_rated"; */
    public static final String PATH_POPULAR = "popular";


    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // movie db , movie id returned from the api, ex : "id": 278
        public static final String COLUMN_MOVIE_DB_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_DB_TITLE = "title";
        public static final String COLUMN_MOVIE_DB_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_DB_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_DB_RELEASE_DATE = "release_date";
     //optional fields
        public static final String COLUMN_MOVIE_DB_POPULARITY = "popularity";
        public static final String COLUMN_MOVIE_DB_VOTE_COUNT = "vote_count";
        public static final String COLUMN_MOVIE_DB_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

    /* Inner class that defines the table contents of the polpular table */
    public static final class PopularEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        // Table name
        public static final String TABLE_NAME = "popular";

        // The popular order column will have the integer value of the array index
        // of the movie returned when calling popular api.
        public static final String COLUMN_POPULAR_ORDER = "popular_order";

        // movie db , movie id returned from the api, ex : "id": 278
        public static final String COLUMN_MOVIE_DB_MOVIE_ID = "movie_id";

        public static Uri buildPopularUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the top rated table
    public static final class TopRatedEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;

        public static final String TABLE_NAME = "top_rated";

        // The top rated order column will have the integer value of the array index
        // of the movie returned when calling top rated  api.
        public static final String COLUMN_TOP_RATED_ORDER = "top_rated_order";

        // movie db , movie id returned from the api, ex : "id": 278
        public static final String COLUMN_MOVIE_DB_MOVIE_ID = "movie_id";

    }
*/

}
