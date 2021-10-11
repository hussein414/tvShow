package com.googel.tvShow.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.googel.tvShow.network.ApiClient;
import com.googel.tvShow.network.ApiService;
import com.googel.tvShow.responses.TvShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTvShowsRepository {
    private final ApiService apiService;
    public MostPopularTvShowsRepository(){
        apiService= ApiClient.getRetrofit().create(ApiService.class);
    }
    public LiveData<TvShowsResponse>getMostPopularTvShows(int page){
        MutableLiveData<TvShowsResponse> data =new MutableLiveData<>();
        apiService.getMostPopularTvShows(page).enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse( @NonNull Call<TvShowsResponse> call,@NonNull Response<TvShowsResponse> response ) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowsResponse> call,@NonNull Throwable t ) {
                data.setValue(null);
            }
        });
        return data;
    }
}
