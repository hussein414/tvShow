package com.googel.tvShow.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.googel.tvShow.Listeners.TvShowListener;
import com.googel.tvShow.R;
import com.googel.tvShow.adapters.TvShowsAdapter;
import com.googel.tvShow.databinding.ActivityMainBinding;
import com.googel.tvShow.models.TvShows;
import com.googel.tvShow.viewmodels.MostPopularTvShowsViewModels;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TvShowListener {

    private MostPopularTvShowsViewModels viewModels;
    private ActivityMainBinding mainBinding;
    private final List<TvShows> tvShows = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePage = 1;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        mainBinding.TvShowRecyclerView.setHasFixedSize(true);
        viewModels = new ViewModelProvider(this).get(MostPopularTvShowsViewModels.class);
        tvShowsAdapter = new TvShowsAdapter(tvShows, this);
        mainBinding.TvShowRecyclerView.setAdapter(tvShowsAdapter);
        mainBinding.TvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled( @NonNull RecyclerView recyclerView, int dx, int dy ) {
                super.onScrolled(recyclerView, dx, dy);
                if (mainBinding.TvShowRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePage) {
                        currentPage += 1;
                        getMostPopularTvShows();
                    }
                }
            }
        });
        mainBinding.imageWatchList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), WatchListActivity.class)));
        mainBinding.imageSearch.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SearchActivity.class)));
        getMostPopularTvShows();
    }

    private void getMostPopularTvShows() {
        toggleLoading();
        viewModels.getMostPopularTvShows(currentPage).observe(this, mostPopularTvShowsResponse -> {
            toggleLoading();
            if (mostPopularTvShowsResponse != null) {
                totalAvailablePage = mostPopularTvShowsResponse.getTotalPages();
                if (mostPopularTvShowsResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTvShowsResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            mainBinding.setIsLoading(mainBinding.getIsLoading() == null
                    || !mainBinding.getIsLoading());
            mainBinding.setIsLoadingMore(mainBinding.getIsLoadingMore() == null
                    || !mainBinding.getIsLoadingMore());
        }
    }

    @Override
    public void OnTvShowClicked( TvShows tvShow ) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetailActivity.class);
        intent.putExtra("tvShows", tvShow);
        startActivity(intent);

    }
}