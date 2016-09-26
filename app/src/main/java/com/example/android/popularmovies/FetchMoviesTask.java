package com.example.android.popularmovies;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.android.popularmovies.adapter.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rgunasek on 9/26/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieData>> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    public static final String API_RELEASE_DATE_FORMAT = "yyyy-MM-dd";

    private final Context mContext;
    private final ImageAdapter mImageAdapter;
   //private final ProgressDialog dialog;

    public FetchMoviesTask(Context vContext, ImageAdapter vImageAdapter) {
        mContext = vContext;
        mImageAdapter = vImageAdapter;
       // dialog = new ProgressDialog(mContext);
    }
    /* ########## Start of JSON Parsing Helper methods ###### */

    private ArrayList<MovieData> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MOVIE_RESULT_LIST = "results";
        final String MOVIE_IMAGE_PATH = "poster_path";
        final String MOVIE_TITLE = "title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_SYNOPSIS = "overview";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_ID = "id";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieResultArray = moviesJson.getJSONArray(MOVIE_RESULT_LIST);
        ArrayList<MovieData> result = new ArrayList<MovieData>();
        for(int i = 0; i < movieResultArray.length(); i++) {

            JSONObject movieJson = movieResultArray.getJSONObject(i);
            String imagepath = movieJson.getString(MOVIE_IMAGE_PATH);
            String title = movieJson.getString(MOVIE_TITLE);
            String apiReleaseDate = movieJson.getString(MOVIE_RELEASE_DATE);
            String voteAverage = movieJson.getString(MOVIE_VOTE_AVERAGE);
            String synopsis = movieJson.getString(MOVIE_SYNOPSIS);
            String releaseyear = Utility.getYearFromDate(apiReleaseDate,API_RELEASE_DATE_FORMAT);
            String detailPageFormatedDate = Utility.getDetailPageFormattedDate(apiReleaseDate,API_RELEASE_DATE_FORMAT);
            Log.v(LOG_TAG, "***detailPageFormatedDate:" + detailPageFormatedDate);
            MovieData data = new MovieData(imagepath,title,releaseyear,detailPageFormatedDate,voteAverage,synopsis);
            result.add(data);
        }

        for (MovieData data : result) {
            Log.v(LOG_TAG, "****Movie entry: " + data);
        }
        return result;

    }
    /* ########## End of JSON Parsing Helper methods ###### */
    protected ArrayList<MovieData> doInBackground(String... params) {

        // If there's no sort preference return null as we do not have the api url to build upon.
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            final String SORTORDER = params[0];
            Log.v(LOG_TAG, "SORTORDER:" + SORTORDER);
            final String MOVIEDB_BASE_URL = BuildConfig.MOVIE_BASE_URL + SORTORDER;
            //"http://api.themoviedb.org/3/movie/popular";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI *" + builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movie Json string:" + movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
    /** progress dialog to show user that the backup is processing. */
    /** application context. */
    @Override
    protected void onPreExecute() {
        //this.dialog.setMessage("Please wait");
        //this.dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPostExecute( ArrayList<MovieData> result) {
        Log.v(LOG_TAG, "In post Execute movie task:" + result);
        if (result != null) {
            Log.v(LOG_TAG, "In post Execute movie task: MovieData Size" + result.size());
            mImageAdapter.clear();
            for(MovieData movieData : result) {
                Log.v(LOG_TAG, "****In adding movie to adapter - MovieData: " + movieData);
                mImageAdapter.add(movieData);
            }
            Log.v(LOG_TAG, "In post Execute movie task: mImageAdapter Size" + mImageAdapter.getCount());
            Log.v(LOG_TAG, "In post Execute movie task: result.get(0):" + result.get(0).getMovieName()+ "mImageAdapter.get(0):" + mImageAdapter.getItem(0).toString());
            mImageAdapter.notifyDataSetChanged();
            Log.v(LOG_TAG, "***********notified data change to adapter" );
        } else {
            Log.v(LOG_TAG, "In post Execute Movie data from API was null" );
        }

       // if (dialog.isShowing()) {
       //     dialog.dismiss();
       // }
    }
}

