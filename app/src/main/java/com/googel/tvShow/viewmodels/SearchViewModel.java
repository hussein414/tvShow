package com.googel.tvShow.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.googel.tvShow.repositories.SearchTvShowsRepository;
import com.googel.tvShow.responses.TvShowsResponse;

public class SearchViewModel extends ViewModel {
    private SearchTvShowsRepository searchTvShowsRepository;

    public SearchViewModel() {
        searchTvShowsRepository = new SearchTvShowsRepository();
    }

    public LiveData<TvShowsResponse> searchTvShow( String query, int page ) {
        return searchTvShowsRepository.searchTvShows(query, page);
    }
}
