package com.googel.tvShow.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.googel.tvShow.repositories.MostPopularTvShowsRepository;
import com.googel.tvShow.responses.TvShowsResponse;

public class MostPopularTvShowsViewModels extends ViewModel {
    private final MostPopularTvShowsRepository mostPopularTvShowsRepository;
    public MostPopularTvShowsViewModels(){
        mostPopularTvShowsRepository=new MostPopularTvShowsRepository();
    }
    public LiveData<TvShowsResponse> getMostPopularTvShows(int page){
        return mostPopularTvShowsRepository.getMostPopularTvShows(page);
    }
}
