package com.example.android.popularmovies;

import com.example.android.popularmovies.adapter.ImageAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private final String LOG_TAG = MainActivity.class.getSimpleName();

	GridView gridView;
	ImageAdapter mImageAdapter;

	//static final String[] MOBILE_OS = new String[] { "Android", "iOS",
	//		"Windows", "Blackberry" };
	private ArrayList<MovieData> popularMovies;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG, "****onCreate savedInstanceState:" + savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView1);
		mImageAdapter = new ImageAdapter(this ,R.layout.mobile, getPopularMoviesData(savedInstanceState));
		executeFetchMovieTask();

		gridView.setAdapter(mImageAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				//CharSequence toastText = "toast from initial load";//((TextView) v.findViewById(R.id.grid_item_label)).getText();
				MovieData gridItemData = (MovieData)mImageAdapter.getItem(position);
				CharSequence toastText = gridItemData.getMovieName();
						Toast.makeText(
						getApplicationContext(),toastText
						, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, DetailActivity.class)
						.putExtra("ImageClicked",gridItemData);
				startActivity(intent);

			}
		});

	}

	private ArrayList<MovieData> getPopularMoviesData(Bundle savedInstanceState) {
		if(popularMovies == null ) {
			Log.v(LOG_TAG, "(popularMovies == null");
			popularMovies = getHardcodedPopularMoviesData();
		}
		else if (!savedInstanceState.containsKey("popularmovies")){
			Log.v(LOG_TAG, "****!savedInstanceState.containsKey");
			popularMovies = getHardcodedPopularMoviesData();
		}
		else {
			popularMovies = savedInstanceState.getParcelableArrayList("popularmovies");
		}
		Log.v(LOG_TAG, "****getPopularMoviesData: " + popularMovies);
		return popularMovies;
	}

	private ArrayList<MovieData> getHardcodedPopularMoviesData() {
		popularMovies = new ArrayList<MovieData>();
		MovieData emptyMovieData = new MovieData("","","", "","","");
		popularMovies.add(emptyMovieData);
		/*
		MovieData m1 = new MovieData("/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg","Suicide Squad","2016", "08.03/16","5.88",
				"From DC Comics comes the Suicide Squad, an antihero team of incarcerated supervillains who act as deniable assets for the United States government, undertaking high-risk black ops missions in exchange for commuted prison sentences.");
		popularMovies.add(m1);
		MovieData m2 = new MovieData("/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg","Guardians of the Galaxy","2014","07.30/14","7.96",
				"Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser.");
		popularMovies.add(m2);
		*/
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
		} /*
		else if (id == R.id.action_refresh){
			toastText="You selected Refresh menu! getting data from API!";
			showToast(getApplicationContext(),toastText);
			executeFetchMovieTask();
			return true;
		} */

		return super.onOptionsItemSelected(item);
	}

	private void executeFetchMovieTask(){
		FetchMoviesTask movieTask = new FetchMoviesTask(MainActivity.this, this.mImageAdapter);
		String sortPreference = Utility.getPreferredSortOrder(getApplicationContext());
		movieTask.execute(sortPreference);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.v(LOG_TAG, "****onSaveInstanceState: " + popularMovies);
		outState.putParcelableArrayList("popularmovies", popularMovies);
		super.onSaveInstanceState(outState);
	}

	private void showToast(Context context, CharSequence text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
	}


	//Start of inner class

}