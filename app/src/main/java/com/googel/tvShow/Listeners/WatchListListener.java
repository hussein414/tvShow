package com.googel.tvShow.Listeners;

import com.googel.tvShow.models.TvShows;

public interface WatchListListener {
    void onTvShowClicked( TvShows tvShows );
    void removeTvShowFormWatchlist(TvShows tvShows,int position);
}
