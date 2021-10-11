package com.googel.tvShow.network;

import com.googel.tvShow.responses.TvShowsDetailsResponse;
import com.googel.tvShow.responses.TvShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TvShowsResponse> getMostPopularTvShows( @Query("page") int page );

    @GET("show-details")
    Call<TvShowsDetailsResponse> getTvShowsDetails( @Query("q") String tvShowId );

    @GET("search")
    Call<TvShowsResponse> searchTvShows( @Query("q") String query, @Query("page") int page );
}
