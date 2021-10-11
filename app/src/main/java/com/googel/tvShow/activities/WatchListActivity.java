package com.googel.tvShow.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.googel.tvShow.Listeners.WatchListListener;
import com.googel.tvShow.R;
import com.googel.tvShow.adapters.WatchlistAdapter;
import com.googel.tvShow.databinding.ActivityWatchListBinding;
import com.googel.tvShow.models.TvShows;
import com.googel.tvShow.utilitiels.TempDataHolder;
import com.googel.tvShow.viewmodels.WatchListModels;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchListListener {

    private ActivityWatchListBinding watchListBinding;
    private WatchListModels models;
    private WatchlistAdapter watchlistAdapter;
    private List<TvShows> watchlist;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        watchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);
        doInitialization();
    }

    private void doInitialization() {
        models = new ViewModelProvider(this).get(WatchListModels.class);
        watchListBinding.imageBack.setOnClickListener(v -> onBackPressed());
        watchlist = new ArrayList<>();
        LoadWatchList();
    }

    private void LoadWatchList() {
        watchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(models.LoadWatchList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    watchListBinding.setIsLoading(false);
                    if (watchlist.size() > 0) {
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist, this);
                    watchListBinding.WatchListRecyclerView.setAdapter(watchlistAdapter);
                    watchListBinding.WatchListRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATE) {
            LoadWatchList();
            TempDataHolder.IS_WATCHLIST_UPDATE = false;
        }
    }

    @Override
    public void onTvShowClicked( TvShows tvShows ) {
        Intent intent = new Intent(getApplicationContext(), TvShowDetailActivity.class);
        intent.putExtra("tvShows", tvShows);
        startActivity(intent);
    }

    @Override
    public void removeTvShowFormWatchlist( TvShows tvShows, int position ) {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(models.removeWatchlist(tvShows)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchlist.remove(position);
                    watchlistAdapter.notifyItemRemoved(position);
                    watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                    disposable.dispose();
                })
        );
    }
}