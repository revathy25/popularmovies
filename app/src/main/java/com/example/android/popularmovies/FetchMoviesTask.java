package com.example.android.popularmovies;


import android.net.Uri;
import android.util.Log;

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


public class FetchMoviesTask  {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    /* ########## Start of JSON Parsing Helper methods ###### */

    private ArrayList<MovieData> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MOVIE_RESULT_LIST = "results";
        final String MOVIE_IMAGE_PATH = "poster_path";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_SYNOPSIS = "overview";
        final String MOVIE_ID = "id";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieResultArray = moviesJson.getJSONArray(MOVIE_RESULT_LIST);
        ArrayList<MovieData> result = new ArrayList<MovieData>();
        for(int i = 0; i < movieResultArray.length(); i++) {

            JSONObject movieJson = movieResultArray.getJSONObject(i);
            String imagepath = movieJson.getString(MOVIE_IMAGE_PATH);
            String title = movieJson.getString(MOVIE_TITLE);
            MovieData data = new MovieData(imagepath, title);
            result.add(data);
        }

        for (MovieData data : result) {
            Log.v(LOG_TAG, "Movie entry: " + data);
        }
        return result;

    }
    /* ########## End of JSON Parsing Helper methods ###### */
    protected ArrayList<MovieData> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            final String SORTBYPOPULAR = "popular";
            final String MOVIEDB_BASE_URL = BuildConfig.MOVIE_BASE_URL + SORTBYPOPULAR;
            //"http://api.themoviedb.org/3/movie/popular";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "***Built URI *" + builtUri.toString());
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
/*
    @Override
    protected void onPostExecute( ArrayList<MovieData> result) {
        if (result != null) {
            for (MovieData data : result) {
                Log.v(LOG_TAG, "onPostExecute Movie entry: " + data);
            }
            popularMovies = result;

            //mForecastAdapter.clear();
            mImageAdapter.updateGridData(result);// new ImageAdapter(getApplicationContext(),result);
            gridView.setAdapter(mImageAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    CharSequence toastText = "toast from post execute! Grid item count:"+ mImageAdapter.getCount();//((TextView) v.findViewById(R.id.grid_item_label)).getText();
                    Toast.makeText(
                            getApplicationContext(),toastText
                            , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                            .putExtra("ImageClicked",toastText);
                    startActivity(intent);

                }
            });

        }

    } */
}
