package com.googel.tvShow.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.googel.tvShow.models.TvShows;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TvShowDao {
    @Query("SELECT * FROM  tvShows")
    Flowable<List<TvShows>> getWatchList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addWatchList( TvShows tvShows );

    @Delete
    Completable removeWatchList( TvShows tvShows );

    @Query("SELECT * FROM tvShows WHERE id = :tvShowId")
    Flowable<TvShows>getTvShowFromWatchlist(String tvShowId);
}
