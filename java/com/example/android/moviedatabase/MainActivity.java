package com.example.android.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.moviedatabase.utilities.NetworkUtils;
import com.example.android.moviedatabase.utilities.MovieDbJsonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private static final int SPAN_COUNT = 2;
    private static final int ORDER_BY_POPULAR = 0;
    private static final int ORDER_BY_RATED = 1;
    private static int orderBy = ORDER_BY_POPULAR;

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageTextView;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageTextView = (TextView) findViewById(R.id.ma_tv_error_message);

        // Finds the RecyclerView, initializes the LayoutManager and sets it in the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.ma_recyclerview_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData(orderBy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_sort_by_highest_rated) {
            mMovieAdapter.setMoviePosterId(null);
            item.setChecked(true);
            orderBy = ORDER_BY_RATED;
            loadMovieData(orderBy);
            return true;
        }

        if (id == R.id.menu_sort_by_most_popular) {
            mMovieAdapter.setMoviePosterId(null);
            item.setChecked(true);
            orderBy = ORDER_BY_POPULAR;
            loadMovieData(orderBy);
            return true;
        }

        if (id == R.id.menu_refresh) {
            mMovieAdapter.setMoviePosterId(null);
            loadMovieData(orderBy);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(int order_by) {
        showMovieDataView();
        new FetchMovieDataTask().execute(order_by);
    }

    private void showMovieDataView() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        String intentTitle = getResources().getResourceName(R.string.intent_title);
        String intentPlot = getResources().getResourceName(R.string.intent_plot);
        String intentReleaseDate = getResources().getResourceName(R.string.intent_release_date);
        String intentVoting = getResources().getResourceName(R.string.intent_voting);
        String intentPoster = getResources().getResourceName(R.string.intent_poster);

        intentToStartDetailActivity.putExtra(intentTitle, movie.getTitle());
        intentToStartDetailActivity.putExtra(intentReleaseDate, movie.getReleaseDate());
        intentToStartDetailActivity.putExtra(intentPlot, movie.getPlotSynopsis());
        intentToStartDetailActivity.putExtra(intentVoting, movie.getVoteAverage());
        intentToStartDetailActivity.putExtra(intentPoster, movie.getMoviePosterUrl());

        startActivity(intentToStartDetailActivity);
    }

    public class FetchMovieDataTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Integer... params) {
            if (orderBy < 0) {
                return null;
            }

            int order_by_settings = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(order_by_settings);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                ArrayList<Movie> jsonMovieData = MovieDbJsonUtils
                        .getMovieDataFromJson(MainActivity.this, jsonMovieResponse);
                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMoviePosterId(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

}
