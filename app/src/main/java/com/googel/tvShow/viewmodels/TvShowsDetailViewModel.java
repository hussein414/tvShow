package com.googel.tvShow.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.googel.tvShow.database.TvShowDataBase;
import com.googel.tvShow.models.TvShows;
import com.googel.tvShow.repositories.TvShowsDetailsRepository;
import com.googel.tvShow.responses.TvShowsDetailsResponse;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class TvShowsDetailViewModel extends AndroidViewModel {
    private final TvShowsDetailsRepository tvShowsDetailsRepository;
    private final TvShowDataBase tvShowDataBase;

    public TvShowsDetailViewModel( @NonNull Application application ) {
        super(application);
        tvShowsDetailsRepository = new TvShowsDetailsRepository();
        tvShowDataBase = TvShowDataBase.getTvShowDataBase(application);
    }

    public LiveData<TvShowsDetailsResponse> getTvShowDetail( String TvShowId ) {
        return tvShowsDetailsRepository.getTvShowsDetails(TvShowId);
    }

    public Completable addToWatchlist( TvShows tvShows ) {
        return tvShowDataBase.tvShowDao().addWatchList(tvShows);
    }

    public Flowable<TvShows> getTvShowFromWatchlist( String tvShowId ) {
        return tvShowDataBase.tvShowDao().getTvShowFromWatchlist(tvShowId);
    }

    public Completable removeTvShowFromWatchlist(TvShows tvShows){
        return tvShowDataBase.tvShowDao().removeWatchList(tvShows);
    }
}
