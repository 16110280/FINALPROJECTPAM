package com.example.android.moviedatabase;

public class Movie {
    private String mTitle;
    private String mReleaseDate;
    private String mMoviePosterUrl;
    private double mVoteAverage;
    private String mPlotSynopsis;

    public Movie(String title, String releaseDate, String moviePosterUrl, double voteAverage, String plotSynopsis) {
        mTitle = title;
        mReleaseDate = releaseDate;
        mMoviePosterUrl = moviePosterUrl;
        mVoteAverage = voteAverage;
        mPlotSynopsis = plotSynopsis;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getMoviePosterUrl() {
        return mMoviePosterUrl;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }
}
