package com.example.android.moviedatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private final MovieAdapterOnClickHandler mClickHandler;

    private static final String MOVIEDB_BASEURL =
            "https://image.tmdb.org/t/p/w500";

    private ArrayList<Movie> mMovie;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMoviePosterImageView = (ImageView) view.findViewById(R.id.ma_iv_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie moviePosterId = mMovie.get(adapterPosition);
            mClickHandler.onClick(moviePosterId);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie moviePoster = mMovie.get(position);
        String URL = MOVIEDB_BASEURL + moviePoster.getMoviePosterUrl();
        Picasso.get().load(URL).into(movieAdapterViewHolder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovie) {
            return 0;
        }
        return mMovie.size();
    }

    public void setMoviePosterId(ArrayList<Movie> moviePosters) {
        mMovie = moviePosters;
        notifyDataSetChanged();
    }

}
