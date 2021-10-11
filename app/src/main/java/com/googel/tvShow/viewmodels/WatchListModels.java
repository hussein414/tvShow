package com.googel.tvShow.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.googel.tvShow.database.TvShowDataBase;
import com.googel.tvShow.models.TvShows;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class WatchListModels extends AndroidViewModel {
    private final TvShowDataBase tvShowDataBase;

    public WatchListModels( @NonNull Application application ) {
        super(application);
        tvShowDataBase = TvShowDataBase.getTvShowDataBase(application);
    }
    public Flowable<List<TvShows>> LoadWatchList(){
        return tvShowDataBase.tvShowDao().getWatchList();
    }
    public Completable removeWatchlist(TvShows tvShows){
        return tvShowDataBase.tvShowDao().removeWatchList(tvShows);
    }
}
