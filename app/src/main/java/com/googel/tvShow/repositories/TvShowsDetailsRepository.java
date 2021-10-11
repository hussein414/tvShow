package com.googel.tvShow.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.googel.tvShow.network.ApiClient;
import com.googel.tvShow.network.ApiService;
import com.googel.tvShow.responses.TvShowsDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowsDetailsRepository {
    private final ApiService apiService;

    public TvShowsDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowsDetailsResponse> getTvShowsDetails( String TvShowId ) {
        MutableLiveData<TvShowsDetailsResponse> data = new MutableLiveData<>();
        apiService.getTvShowsDetails(TvShowId).enqueue(new Callback<TvShowsDetailsResponse>() {
            @Override
            public void onResponse( @NonNull Call<TvShowsDetailsResponse> call, @NonNull Response<TvShowsDetailsResponse> response ) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure( @NonNull Call<TvShowsDetailsResponse> call, @NonNull Throwable t ) {
                data.setValue(null);
            }
        });
        return data;
    }
}
