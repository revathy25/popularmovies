package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MovieData;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	//private final String[] mobileValues;
	private ArrayList<MovieData> gridViewData;
	private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

	public ImageAdapter(Context context, ArrayList<MovieData> movieData) {
		this.context = context;
		this.gridViewData = movieData;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.mobile, null);
			MovieData gridItemData = (MovieData)gridViewData.get(position);
			// set value into textview
			/*
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			//textView.setText(mobileValues[position]);
			textView.setText(gridItemData.getMovieName()); */
			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			Picasso.with(context).load(IMAGE_BASE_URL+gridItemData.getImageRelativePath()).into(imageView);
/*
			String mobile = mobileValues[position];

			if (mobile.equals("Windows")) {
				imageView.setImageResource(R.drawable.windows_logo);
			} else if (mobile.equals("iOS")) {
				imageView.setImageResource(R.drawable.ios_logo);
			} else if (mobile.equals("Blackberry")) {
				imageView.setImageResource(R.drawable.blackberry_logo);
			} else {
				imageView.setImageResource(R.drawable.android_logo);
			}
*/
		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		if(gridViewData != null){
			return gridViewData.size();

		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void updateGridData(ArrayList<MovieData> movieData){
		this.gridViewData = movieData;
	}

}
