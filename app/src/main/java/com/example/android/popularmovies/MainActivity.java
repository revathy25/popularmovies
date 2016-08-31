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

public class MainActivity extends AppCompatActivity {

	GridView gridView;

	static final String[] MOBILE_OS = new String[] { "Android", "iOS",
			"Windows", "Blackberry" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView1);

		gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CharSequence toastText = ((TextView) v.findViewById(R.id.grid_item_label)).getText();
				Toast.makeText(
						getApplicationContext(),toastText
						, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this, DetailActivity.class)
						.putExtra("ImageClicked",toastText);
				startActivity(intent);

			}
		});

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