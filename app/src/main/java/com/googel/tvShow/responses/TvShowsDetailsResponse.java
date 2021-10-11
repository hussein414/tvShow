package com.googel.tvShow.responses;

import com.googel.tvShow.models.TvShowsDetails;
import com.google.gson.annotations.SerializedName;

public class TvShowsDetailsResponse {
    @SerializedName("tvShow")
    private TvShowsDetails tvShowsDetails;

    public TvShowsDetails getTvShowsDetails() {
        return tvShowsDetails;
    }
}
