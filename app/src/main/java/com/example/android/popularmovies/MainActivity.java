package com.example.android.popularmovies;

import com.example.android.popularmovies.adapter.ImageAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	GridView gridView;

	//static final String[] MOBILE_OS = new String[] { "Android", "iOS",
	//		"Windows", "Blackberry" };
	private ArrayList<MovieData> popularMovies;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView1);

		//gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));
		gridView.setAdapter(new ImageAdapter(this , getPopularMoviesData()));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CharSequence toastText = "";//((TextView) v.findViewById(R.id.grid_item_label)).getText();
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
			return true;
		} else if (id == R.id.action_refresh){
			toastText="You selected Refresh menu!";
			showToast(getApplicationContext(),toastText);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showToast(Context context, CharSequence text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
	}

}