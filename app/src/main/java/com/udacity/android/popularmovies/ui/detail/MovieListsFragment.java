package com.udacity.android.popularmovies.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.udacity.android.popularmovies.R;
import com.udacity.android.popularmovies.model.Trailer;
import com.udacity.android.popularmovies.ui.MovieOnClickListener;
import com.udacity.android.popularmovies.ui.adapter.BaseMovieListTypeAdapter;
import com.udacity.android.popularmovies.ui.adapter.MovieListTypeAdapterFactory;
import com.udacity.android.popularmovies.utilities.MovieInstanceProviderUtil;

/**
 * Fragment that displayed either the list of Trailers or Reviews.
 *
 */
public class MovieListsFragment extends Fragment {

    private static final String LOG_TAG = MovieListsFragment.class.getSimpleName();

    private static final String MOVIE_ID_KEY = "movie_id_key";
    private static final String MOVIE_LIST_LAYOUT_ID = "movie_list_id_key";

    private int mMovieId;
    private int mLayoutResId;

    public static MovieListsFragment getInstance(int movieId, int layoutResId) {
        MovieListsFragment fragment = new MovieListsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_KEY, movieId);
        bundle.putInt(MOVIE_LIST_LAYOUT_ID, layoutResId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_list, container, false);

        if (getArguments() != null) {
            mMovieId = getArguments().getInt(MOVIE_ID_KEY);
            mLayoutResId = getArguments().getInt(MOVIE_LIST_LAYOUT_ID);
        }


        BaseMovieListTypeAdapter adapter = MovieListTypeAdapterFactory.create(mLayoutResId, null);
        setupRecyclerViewTrailer(view, adapter);

        DetailActivityViewModelFactory factory = MovieInstanceProviderUtil
                .provideDetailActivityViewModelFactory(getActivity().getApplicationContext(), mMovieId);
        DetailActivityViewModel viewModel = new ViewModelProvider(this, factory)
                .get(DetailActivityViewModel.class);
        viewModel.getMovie().observe(this, movie -> {
            switch (mLayoutResId) {
                case R.id.trailers:
                    adapter.swapData(movie.trailers);
                    break;
                case R.id.reviews:
                    adapter.swapData(movie.reviews);
                    break;
            }
        });

        return view;
    }

    private void setupRecyclerViewTrailer(View view, RecyclerView.Adapter adapter) {
        RecyclerView recyclerView = view.findViewById(R.id.list_details_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }


}
