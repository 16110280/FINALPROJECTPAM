package com.example.android.moviedatabase.utilities;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import com.example.android.moviedatabase.Movie;

public final class MovieDbJsonUtils {

    public static ArrayList<Movie> getMovieDataFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_PLOT = "overview";
        final String MDB_VOTE = "vote_average";
        final String MDB_TITLE = "title";
        final String MDB_POSTER = "poster_path";

        final String MDB_MESSAGE_CODE = "status_code";

        ArrayList<Movie> parsedMovieData = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        if (movieJson.has(MDB_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(MDB_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(MDB_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String releaseDate;
            String overview;
            String posterPath;
            double voteAverage;

            JSONObject movieInfo = movieArray.getJSONObject(i);

            title = movieInfo.getString(MDB_TITLE);
            releaseDate = movieInfo.getString(MDB_RELEASE_DATE);
            posterPath = movieInfo.getString(MDB_POSTER);
            voteAverage = movieInfo.getDouble(MDB_VOTE);
            overview = movieInfo.getString(MDB_PLOT);

            parsedMovieData.add(new Movie(title, releaseDate, posterPath, voteAverage, overview));
        }

        return parsedMovieData;
    }
}
