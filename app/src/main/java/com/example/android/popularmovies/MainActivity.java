package com.example.android.popularmovies;

import com.example.android.popularmovies.adapter.ImageAdapter;
import com.example.android.popularmovies.datasync.PopularMoviesSyncAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	GridView gridView;
	ImageAdapter mImageAdapter;

	//static final String[] MOBILE_OS = new String[] { "Android", "iOS",
	//		"Windows", "Blackberry" };
	private ArrayList<MovieData> popularMovies;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		PopularMoviesSyncAdapter.initializeSyncAdapter(this);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView1);
		mImageAdapter = new ImageAdapter(this , getPopularMoviesData());
		gridView.setAdapter(mImageAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CharSequence toastText = "toast from initial load";//((TextView) v.findViewById(R.id.grid_item_label)).getText();
				Toast.makeText(
						getApplicationContext(),toastText
						, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, DetailActivity.class)
						.putExtra("ImageClicked",toastText);
				startActivity(intent);

			}
		});

	}

	private ArrayList<MovieData> getPopularMoviesData() {
		popularMovies = new ArrayList<MovieData>();
		MovieData m1 = new MovieData("/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg","Suicide Squad");
		popularMovies.add(m1);
		MovieData m2 = new MovieData("/lFSSLTlFozwpaGlO31OoUeirBgQ.jpg","Jason Bourne");
		popularMovies.add(m2);
		MovieData m3 = new MovieData("/5N20rQURev5CNDcMjHVUZhpoCNC.jpg","Captain America: Civil War");
		popularMovies.add(m3);
		MovieData m4 = new MovieData("/tgfRDJs5PFW20Aoh1orEzuxW8cN.jpg","Mechanic: Resurrection");
		popularMovies.add(m4);
		MovieData m5 = new MovieData("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","Interstellar");
		popularMovies.add(m5);
		MovieData m6 = new MovieData("/oDL2ryJ0sV2bmjgshVgJb3qzvwp.jpg","Teenage Mutant Ninja Turtles");
		popularMovies.add(m6);
		return popularMovies;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		CharSequence toastText;

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			toastText="You selected settings menu!";
			showToast(getApplicationContext(),toastText);
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_refresh){
			toastText="You selected Refresh menu! getting data from API!";
			showToast(getApplicationContext(),toastText);
			FetchMoviesTask movieTask = new FetchMoviesTask();
			movieTask.execute("testParam");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showToast(Context context, CharSequence text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
	}


	//Start of inner class
	public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieData>> {
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

		@Override
		protected void onPostExecute( ArrayList<MovieData> result) {
			if (result != null) {
				//mForecastAdapter.clear();
				mImageAdapter = new ImageAdapter(getApplicationContext(),result);
				gridView.setAdapter(mImageAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
											int position, long id) {
						CharSequence toastText = "toast from post execute!";//((TextView) v.findViewById(R.id.grid_item_label)).getText();
						Toast.makeText(
								getApplicationContext(),toastText
								, Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MainActivity.this, DetailActivity.class)
								.putExtra("ImageClicked",toastText);
						startActivity(intent);

					}
				});

			}
		}
	}

}