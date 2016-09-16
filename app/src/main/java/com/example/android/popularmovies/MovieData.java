package com.example.android.popularmovies;

/**
 * Created by rgunasek on 9/1/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class MovieData implements Parcelable{
    String imageRelativePath;
    String movieName;
    String releaseDate;
    String voteAverage;
    String plotSynopsis;

    public MovieData(String vImageRelativePath, String vMovieName, String vReleaseDate,String vVoteAverage,String vPlotSynopsis )
    {
        this.imageRelativePath = vImageRelativePath;
        this.movieName = vMovieName;
        this.releaseDate = vReleaseDate;
        this.voteAverage = vVoteAverage;
        this.plotSynopsis = vPlotSynopsis;
    }

    private MovieData(Parcel in){
        imageRelativePath = in.readString();
        movieName = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
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

    public String getReleaseDate() { return releaseDate; }

    public String getVoteAverage() { return voteAverage; }

    public String getPlotSynopsis() { return plotSynopsis; }

    @Override
    public String toString() {
        return "MovieData [imageRelativePath=" + imageRelativePath + ", movieName=" + movieName + ", releaseDate="
                + releaseDate + ", voteAverage=" + voteAverage + ", plotSynopsis=" + plotSynopsis + "]";
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageRelativePath);
        parcel.writeString(movieName);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(plotSynopsis);
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