package com.example.android.popularmovies.moviesgrid;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.MoviePoster;
import com.example.android.popularmovies.moviedetails.MovieDetailsActivity;
import com.example.android.popularmovies.utilities.ImagesDimensionUtils;
import com.example.android.popularmovies.utilities.UrlsUtils;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MoviesGridFragment extends Fragment implements MoviesGridContract.View {
    
    private final String TAG = MoviesGridFragment.class.getSimpleName();
    
    private MoviesGridAdapter mAdapter;
    
    private RecyclerView mRecyclerView;
    
    private MoviesGridContract.Presenter mPresenter;
    
    public MoviesGridFragment() {
        // Required empty public constructor
    }
    
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movies_grid_fragment, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.movies_recycler_view);
        return root;
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    
    @Override
    public void setPresenter(final MoviesGridContract.Presenter presenter) {
        mPresenter = presenter;
    }
    
    @Override
    public int getChildLayoutPosition(final View view) {
        return mRecyclerView.getChildLayoutPosition(view);
    }
    
    @Override
    public void setPosters(final MoviePoster[] posters) {
        initLayoutManager();
        mAdapter = new MoviesGridAdapter(posters);
        mRecyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void showMovieDetails(final String movieId) {
        Context context = getContext();
        Intent startChildActivityIntent = new Intent(context, MovieDetailsActivity.class);
        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, movieId);
        context.startActivity(startChildActivityIntent);
    }
    
    @Override
    public MoviePoster getPoster(final int position) {
        return mAdapter.getPoster(position);
    }
    
    @Override
    public Resources getResporces() {
        return getResources();
    }
    
    public static MoviesGridFragment newInstance() {
        return new MoviesGridFragment();
    }
    
    public void initLayoutManager() {
        int screenOrientation = getResporces().getConfiguration().orientation;
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), ImagesDimensionUtils.getColumnsForOrientation(screenOrientation));
        mRecyclerView.setLayoutManager(layoutManager);
    }
    
    private class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {
        
        private final int mViewWidth;
        
        private final int mViewHeight;
        
        private final MoviePoster[] mPosters;
        
        MoviesGridAdapter(@NonNull final MoviePoster[] posters) {
            mPosters = posters; //todo: check not null - creashes when key is wrong
            mViewWidth = ImagesDimensionUtils.getGridElementWidth(getContext());
            mViewHeight = ImagesDimensionUtils.getGridElementHeight(getContext());
        }
        
        @Override
        public MoviesGridAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final boolean attachToRoot = false;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_entry, parent, attachToRoot);
            view.setOnClickListener(mPresenter);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(final MoviesGridAdapter.ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder, position: " + position);
            MoviePoster poster = mPosters[position];
            if (poster != null) {
                holder.setPoster(poster);
            }
        }
        
        @Override
        public int getItemCount() {
            return mPosters.length;
        }
        
        public MoviePoster getPoster(int position) {
            return mPosters[position];
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            
            private final ImageView mMoviePoster;
            
            ViewHolder(final View view) {
                super(view);
                mMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mViewWidth, mViewHeight);
                mMoviePoster.setLayoutParams(layoutParams);
            }
            
            void setPoster(@NonNull MoviePoster poster) {
                Log.d(TAG, "Picasso load: " + poster.getPoster_path());
                Picasso.with(itemView.getContext())
                        .load(UrlsUtils.getPosterUrl(poster))
                        .into(mMoviePoster);
            }
        }
    }
}
