package com.example.android.popularmovies;

/**
 * Created by rgunasek on 9/1/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class MovieData implements Parcelable{
    String imageRelativePath;
    String movieName;

    public MovieData(String vImageRelativePath, String vMovieName)
    {
        this.imageRelativePath = vImageRelativePath;
        this.movieName = vMovieName;
    }

    private MovieData(Parcel in){
        imageRelativePath = in.readString();
        movieName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getImageRelativePath() {
        return imageRelativePath;
    }

    public String getMovieName() {
        return movieName;
    }

    public String toString() { return imageRelativePath + "--" + movieName ; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageRelativePath);
        parcel.writeString(movieName);
    }

    public final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel parcel) {
            return new MovieData(parcel);
        }

        @Override
        public MovieData[] newArray(int i) {
            return new MovieData[i];
        }

    };
}