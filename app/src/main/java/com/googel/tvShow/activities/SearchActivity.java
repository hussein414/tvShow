package com.googel.tvShow.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import com.googel.tvShow.Listeners.TvShowListener;
import com.googel.tvShow.R;
import com.googel.tvShow.adapters.TvShowsAdapter;
import com.googel.tvShow.databinding.ActivitySearchBinding;
import com.googel.tvShow.models.TvShows;
import com.googel.tvShow.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TvShowListener {

    private ActivitySearchBinding searchBinding;
    private SearchViewModel searchViewModel;
    private List<TvShows> tvShows = new ArrayList<>();
    private TvShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialization();

    }

    private void doInitialization() {

        searchBinding.imageBack.setOnClickListener(v -> onBackPressed());
        searchBinding.searchRecyclerView.setHasFixedSize(true);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowsAdapter = new TvShowsAdapter(tvShows, this);
        searchBinding.searchRecyclerView.setAdapter(tvShowsAdapter);
        searchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {

                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged( Editable editable ) {
                if (!editable.toString().trim().isEmpty()) {
                    timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablePages = 1;
                                searchTvShow(editable.toString());
                            });
                        }
                    }, 800);
                } else {
                    tvShows.clear();
                    tvShowsAdapter.notifyDataSetChanged();
                }

            }
        });
        searchBinding.searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled( @NonNull RecyclerView recyclerView, int dx, int dy ) {
                super.onScrolled(recyclerView, dx, dy);
                if (!searchBinding.searchRecyclerView.canScrollVertically(1)) {
                    if (!searchBinding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1;
                            searchTvShow(searchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        searchBinding.inputSearch.requestFocus();
    }

    private void searchTvShow( String query ) {
        toggleLoading();
        searchViewModel.searchTvShow(query, currentPage).observe(this, tvShowsResponse -> {
            if (tvShowsResponse != null) {
                totalAvailablePages = tvShowsResponse.getTotalPages();
                if (tvShowsResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(tvShowsResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            searchBinding.setIsLoading(searchBinding.getIsLoading() == null
                    || !searchBinding.getIsLoading());
            searchBinding.setIsLoadingMore(searchBinding.getIsLoadingMore() == null
                    || !searchBinding.getIsLoadingMore());
        }
    }

    @Override
    public void OnTvShowClicked( TvShows tvShow ) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetailActivity.class);
        intent.putExtra("tvShows", tvShow);
        startActivity(intent);
    }
}